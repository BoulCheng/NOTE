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