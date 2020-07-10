    
##### I/O
- 流
    - I/O是获取、交换信息的渠道，而流是完成I/O操作的主要方式。
    - 流是一种信息的转换，数据和流之间相互转化，可以看作是数据的载体，通过流实现I/O操作，实现数据的传输和交换
    - 流是一种抽象概念，它代表了数据的无结构化传递
   
    - java.io包下流
        - 字节流,InputStream、OutputStream
            - 如果是文件的读写操作，就使用FileInputStream/FileOutputStream；
            - 如果是数组的读写操作，就使用ByteArrayInputStream/ByteArrayOutputStream；
            - 如果是普通字符串的读写操作，就使用BufferedInputStream/BufferedOutputStream
            - 对象，ObjectInputStream/ObjectOutputStream
        - 字符流 Reader、Writer (字符和字节之间需要编解码，为了简易所以才有字符流)
            - 字节流转化字符流 InputStreamReader/OutputStreamWriter
            - 文件 FileReader/FileWriter
            - String StringReader/StringWriter
            - character CharArrayReader / CharArrayWriter
- I/O操作
    - 磁盘I/O操作
        - 内存和磁盘之间的数据传输
    - 网络I/O操作
        - 内存和网络之间的数据传输
    - 传统I/O操作流程
        
        1. 用户空间并通过read系统调用向内核发起读请求；
        2. 内核向硬件(硬件设备包括磁盘、网络)发送读指令，并等待读就绪；
        3. 内核把将要读取的数据复制到指向的内核缓存中；
        4. 操作系统内核将数据复制到用户空间缓冲区，然后read系统调用返回。 
    - JVM通过 FileInputStream#read()可以发起read系统调用, 且InputStream#read()是一个while循环操作，它会一直等待数据读取，直到数据就绪才会返回。 (Reads a byte of data from this input stream. This method blocks if no input is yet available.)
    - 问题
        1. 多次内存复制 数据先从外部设备复制到内核空间，再从内核空间复制到用户空间，这就发生了两次内存复制操作，引起上下文切换 用户态和内核态的相互切换
        2. 阻塞
            - 如果没有数据就绪，这个读取操作将会一直被挂起，用户线程将会处于阻塞状态
            - 大量连接请求时，就需要创建大量监听线程，这时如果线程没有数据就绪就会被挂起，然后进入阻塞状态。一旦发生线程阻塞，这些线程将会不断地抢夺CPU资源，从而导致大量的CPU上下文切换，增加系统的性能开销
    - 优化
        - 操作系统优化
        - 语言优化
            - java NIO优化了内存复制以及阻塞导致的严重性能问题
        - NIO
            - 使用Buffer缓冲区优化读写流操作
                - NIO, 依然是面向流的(stream-oriented ), 面向Buffer流,基于块以内存块为基本单位处理数据, Buffer是一块连续的内存块用于缓存读写数据; 传统I/O,面向流,基于流的实现以字节为单位处理数据
                - Buffer可以将文件一次性读入内存再做后续处理，而传统的方式是边读文件边处理数据，BufferedInputStream虽然也有缓存但仍不足
                - 通道Channel是访问缓冲的接口，用于读取缓冲或者写入数据，表示缓冲数据的源头或者目的地
            - DirectByteBuffer优化多次内存拷贝
                - 普通的Buffer分配的是JVM堆内存如HeapByteBuffer，而DirectByteBuffer是直接分配物理内存，可以直接访问物理内存
                - 直接访问物理内存使数据直接在内核空间和外部设备(磁盘、网络)之间传输，减少了用户空间和内核空间之间的内存复制，减少了数据的拷贝
                
                - DirectBuffer申请的是非JVM的物理内存，所以创建和销毁的代价很高。DirectBuffer申请的内存并不是直接由JVM负责垃圾回收，但在DirectBuffer包装类被回收时，会通过Java Reference机制来释放该内存块
            - 通道（Channel）和 多路复用器（Selector） 优化阻塞
                - NIO很多人也称之为Non-block I/O，即非阻塞I/O，因为这样叫，更能体现它的特点
                - 传统的I/O对Socket的输入流进行读取时，读取流会一直阻塞，直到发生以下三种情况的任意一种才会解除阻塞
                    1. 有数据可读；
                    2. 连接释放；
                    3. 空指针或I/O异常。
                - 通道（Channel）
                    - 读取和写入数据都要通过Channel，由于Channel是双向的，所以读、写可以同时进行
                    - 传统I/O的数据读取和写入是从用户空间到内核空间来回复制，而内核空间的数据是通过操作系统层面的I/O接口从磁盘读取或写入; 最开始，在应用程序调用操作系统I/O接口时，是由CPU完成分配，这种方式最大的问题是“发生大量I/O请求时，非常消耗CPU“；之后，操作系统引入了DMA（直接存储器存储），内核空间与磁盘之间的存取完全由DMA负责，但有些情况下会总线冲突
                    - 在实现DMA传输时，是由DMA控制器直接掌管总线，因此，存在着一个总线控制权转移问题。即DMA传输前，CPU要把总线控制权交给DMA控制器，而在结束DMA传输后，DMA控制器应立即把总线控制权再交回给CPU。一个完整的DMA传输过程必须经过DMA请求、DMA响应、DMA传输、DMA结束4个步骤；在DMA连续传输过程中，CPU可以正常工作，CPU一定以某种方式获得了总线的控制权。CPU和DMA时分复用了总线
                    - 通道的出现解决了以上问题，Channel有自己的处理器，可以完成内核空间和磁盘之间的I/O操作，
                - 多路复用器（Selector）
                    - Selector是Java NIO编程的基础。用于检查一个或多个NIO Channel的状态是否处于可读、可写。
                    - Selector是基于事件驱动实现的，我们可以在Selector中注册accpet、read监听事件，Selector会不断轮询注册在其上的Channel，如果某个Channel上面发生监听事件，这个Channel就处于就绪状态，然后进行I/O操作。
                    - 一个线程使用一个Selector，通过轮询的方式，可以监听多个Channel上的事件。
                    - 我们可以在注册Channel时设置该通道为非阻塞，当Channel上没有I/O操作时，该线程就不会一直等待了，而是会不断轮询所有Channel，从而避免发生阻塞
                    - 目前操作系统的I/O多路复用机制都使用了epoll，相比传统的select机制，epoll没有最大连接句柄1024的限制。所以Selector在理论上可以轮询成千上万的客户端。
                - NIO适用于发生大量I/O连接请求的场景
                - 来源网络待确认 (在Linux中，AIO并未真正使用操作系统所提供的异步I/O，它仍然使用poll或epoll，并将API封装为异步I/O的样子，但是其本质仍然是同步非阻塞I/O，加上第三方产品的出现，Java网络编程明显落后，所以没有成为主流)
            