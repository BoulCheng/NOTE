
- netty NIO传输 VS Epoll传输
    - netty NIO传输
        - 基于 java nio
            - 会使用 Linux epoll
        - jdk为了在所有操作系统提供相同的功能做出了妥协
        - NioEventLoop 解决了jdk nio epoll cpu 空轮询的bug
        - Netty的解决策略：
          - [https://www.cnblogs.com/JAYIT/p/8241634.html]
          - [http://songkun.me/2019/07/26/2019-07-26-java-nio-epoll-bug-and-netty-solution/]
          - 1) 根据该BUG的特征，首先侦测该BUG是否发生；
            - 一个参数是Selector#select返回值为0的计数，第二个是多长时间，整体意思就是控制在多长时间内，如果Selector.select不断返回0，说明进入了JVM的bug的模式
          - 接着开启一个新的Selector，并将原有的SelectionKey的事件全部转移到了新的Selector中，老的问题Selector关闭，使用新建的Selector替换
              - 将问题Selector上注册的Channel转移到新建的Selector上；
    - Epoll传输
        - netty 使用 Linux epoll 的独特方式，优于JDK NIO 使用epoll
        - netty 直接调用 jni 方法( call JNI methods ) 驱动epoll() 
        - netty native 方法 
            - EpollEventLoop、Native、NativeStaticallyReferencedJniMethods
            
- ByteBuf 的使用模式
    - 堆缓冲区
    - 直接缓冲区：默认地，当所运行的环境具有sun.misc.Unsafe支持时，返回基于直接内存存储的ByteBuf，否则 返回基于堆内存存储的 ByteBuf;当指定使用 PreferHeapByteBufAllocator 时，则只会返回基 于堆内存存储的 ByteBuf
        - 在通过套接字发送它之前，JVM将会在内部把你的堆缓冲区复制到一个直接缓冲区中
        - 相对于基于堆的缓冲区，它们的分配和释放都较为昂贵
    - 复合缓冲区
        - 它提供了一 个将多个缓冲区表示为单个合并缓冲区的虚拟表示

- ByteBuf 分配
    - ByteBufAllocator
        - PooledByteBufAllocator
            - 前者池化了ByteBuf的实例以提高性能并最大限度地减少内存碎片.此实 现 使 用 了 一 种 称 为 j e m a l l o c 2 的 已 被 大 量 现 代 操 作 系 统 所 采 用 的 高 效 方 法 来 分 配 内 存 
            - 虽然Netty默认使用了PooledByteBufAllocator，但这可以很容易地通过ChannelConfig API或者在引导你的应用程序时指定一个不同的分配器来更改
        - UnpooledByteBufAllocator
            - 后 者 的 实 现 不池化ByteBuf实例，并且在每次它被调用时都会返回一个新的实例.
        
    - Unpooled
        - 创建未池化的 ByteBuf 实例
        
- ByteBufHolder
    - 除了实际的数据负载之外，我们还需要存储各种属性值
    - ByteBufHolder 也为 Netty 的 高级特性提供了支持，如缓冲区池化，其中可以从池中借用 ByteBuf，并且在需要时自动释放
    
- ByteBufUtil 
    - 提供了用于操作 ByteBuf 的静态的辅助方法。因为这个 API 是通用的，并且和池化无关，所以这些方法已然在分配类的外部实现
    
    
- 引用计数
    - 引用计数是一种通过在某个对象所持有的资源不再被其他对象引用时释放该对象所持有的资源来优化内存使用和性能的技术
    - 一个 ReferenceCounted 实现的实例将通常以活动的引用计数为 1 作为开始。只要引用计 数大于 0，就能保证对象不会被释放。当活动引用的数量减少到 0 时，该实例就会被释放。
    - 注意， 虽然释放的确切语义可能是特定于实现的，但是至少已经释放的对象应该不可再用了
    - 引用计数对于池化实现(如 PooledByteBufAllocator)来说是至关重要的，它降低了 内存分配的开销
    - 谁负责释放 一般来说，是由最后访问(引用计数)对象的那一方来负责将它释放。在第 6 章中， 我们将会解释这个概念和 ChannelHandler 以及 ChannelPipeline 的相关性
    
    
    
##### 堆内非池化bytebuf，堆内池化bytebuf，堆外池化bytebuf和堆外非池化bytebuf
- 其中对于堆内的bytebuf来说，其都是通过byte数组来存储数据，对于堆外的bytebuf来说其是通过 directbytebuffer 来保存数据

- 堆内非池化bytebuf
    - UnpooledHeapByteBuf
        ```
        
            static void setByte(byte[] memory, int index, int value) {
                memory[index] = (byte)value;
            }
        
        ```
    - UnpooledUnsafeHeapByteBuf 
        - 继承了UnpooledHeapByteBuf其中主要是重写了UnpooledHeapByteBuf的set和get方法即write和read，UnpooledUnsafeHeapByteBuf通过unsafe获取到我们bytebuf内部数组在内存中的地址然后直接操作地址，这样的效率比我们调用数组的api方法更快
            ```
                static void putByte(byte[] data, int index, byte value) {
                    UNSAFE.putByte(data, BYTE_ARRAY_BASE_OFFSET + (long)index, value);
                }
            
            ```


- 堆外非池化bytebuf
    - UnpooledDirectByteBuf:是直接生产一个directbytebuffer对象，然后释放也是依靠directbytebuffer内部的cleaner对象（我们通过反射获取到cleaner对象然后调用其clean方法,而不是依靠虚引用来调用clean方法因为虚引用回收内存不及时会导致oom）
    - UnpooledUnsafeDirectByteBuf: 和UnpooledDirectByteBuf大部分一样，不一样地方在于他获悉了其directbytebuffer的内置地址，然后通过unsafe操作该对象。这样的好处就是读取速度快
    - UnpooledUnsafeNoCleanerDirectByteBuf: 与UnpooledUnsafeDirectByteBuf相似，区别在于前者没有cleaner对象（因为通过反射调用了DirectByteBuffer的其他构造函数），所以其内存释放需要借助于unsafe，其实上述的cleaner调用clean方法 其底层也是调用unsafe的方法
    
    
- 堆内池化bytebuf
    - .....
    
    
    
    
    
    
    
- 引导    
    - Bootstrap vs ServerBootstrap
    - ServerBootstrap
        - 服务端有两组不同Channel  
            - 一个NioServerSocketChannel
            - 多个NioSocketChannel
        - bossGroup 分配一个EventLoop给 NioServerSocketChannel 处理传入的连接请求并创建 NioSocketChannel
        - workerGroup 已经接受的连接对应一个 NioSocketChannel，为每个NioSocketChannel分配一个EventLoop处理该连接的I/O事件
        
- channel的创建 ChannelHandler ChannelHandlerPipeline ChannelHandlerContext 之间关系
    - ChannelHandlerPipeline 拦截过滤器设计模式 如何过滤 ChannelInBoundHandler ChannelOutBoundHandler 
- netty实践使用代码


    
- Channel 主要api
    - writer
        - 等待被冲刷
    - flush
        - 将数据冲刷到底层传输
    - 多线程调用writerAndFlush 可以保证消息顺序被发送
    
- 套接字非阻塞系统调用(非阻塞I/O?) vs NIO
- NIO vs I/O多路复用    

        
- zero拷贝 
    - 数据不需要从内核空间复制到用户空间
        - 比如将数据从文件系统移动到网络接口，实现高效网络发送数据
        
- ~~ioRatio~~
    - [https://bbs.huaweicloud.com/blogs/137090]
    - [https://zhuanlan.zhihu.com/p/95301195]
    
#### 粘包



- 具体的编解码器 和 ChannelIn(Out)BoundHandler
    - [https://blog.csdn.net/woshizw27/article/details/87999137]
    - ChannelHandler的主要用途
    
    
心跳机制

﻿心跳是在TCP长连接中，客户端和服务端定时向对方发送数据包通知对方自己还在线，保证连接的有效性的一种机制
在服务器和客户端之间一定时间内没有数据交互时, 即处于 idle 状态时, 客户端或服务器会发送一个特殊的数据包给对方, 当接收方收到这个数据报文后, 也立即发送一个特殊的数据报文, 回应发送方, 此即一个 PING-PONG 交互. 自然地, 当某一端收到心跳消息后, 就知道了对方仍然在线, 这就确保 TCP 连接的有效性.
心跳实现

使用TCP协议层的Keeplive机制，但是该机制默认的心跳时间是2小时，依赖操作系统实现不够灵活；

应用层实现自定义心跳机制，比如Netty实现心跳机制；


- netty粘包解决方法
