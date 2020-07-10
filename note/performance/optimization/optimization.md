

##### intern
- 在字符串常量中，默认会将对象放入常量池，也会去查看字符串常量池中是否有等于(equals)该对象的字符串；在字符串变量中，对象是会创建在堆内存中。
如果调用intern方法，会去查看字符串常量池中是否有等于该对象的字符串，如果没有则在常量池中新增该对象，并返回该对象引用；
如果有，则返回常量池中的字符串。堆内存中原有的对象由于没有引用指向它，将会通过垃圾回收器回收
```
String str1= "abc"; // 在字符串常量中
String str2= new String("abc"); // 在字符串变量中
String str3= str2.intern(); // 如果调用intern方法
assertSame(str1==str2);
assertSame(str2==str3);
assertSame(srt1==str3)

        String str5 = "abc";
        String str52 = "abc";

        if(str1==str12) {
            System.out.print("str1==str12");
        }
```

```
        long t = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < 100000; i++) {
            str += i;
        }
        System.out.println(System.currentTimeMillis() - t); // 32241 ms

        long t2= System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            stringBuilder.append(i);
        }
        System.out.println(System.currentTimeMillis() - t2); // 2 ms


```
```
String str = "abcdef";

for(int i=0; i<1000; i++) {
      str = str + i;
}

// 编译器优化后  
// 即使使用+号作为字符串的拼接，也一样可以被编译器优化成StringBuilder的方式。
// 但再细致些，你会发现在编译器优化的代码中，每次循环都会生成一个新的StringBuilder实例，同样也会降低系统的性能。
String str = "abcdef";

for(int i=0; i<1000; i++) {
        	  str = (new StringBuilder(String.valueOf(str))).append(i).toString();
}
```


##### regex 回溯问题
- 正则表达式使用一些特定的元字符来检索、匹配以及替换符合规则的字符串。
构造正则表达式语法的元字符，由普通字符、标准字符、限定字符（量词）、定位字符（边界字符）组成
- 匹配模式
    - 贪婪模式 会导致回溯问题， 顾名思义，就是在数量匹配中，如果单独使用+、 ? 、* 或{min,max} 等量词，正则表达式会匹配尽可能多的内容
    - 懒惰模式 后面加 "?"
    - 独占模式 后面加 "+"
- 避免回溯的方法就是：使用懒惰模式和独占模式。
```
什么是回溯
假如现在待匹配的字符串是：1111a

正则表达式为：[\d,a]+a

此时对[\d,a]是默认的贪婪模式，匹配的大致流程：
第一个字符1是否满足[\d,a]，满足
第二个字符1是否满足[\d,a]，满足
…..
最后一个字符a是否满足[\d,a]，满足
后面没有字符匹配正则表达式最后的a了，匹配失败，！！回溯！！
回到最后一个字符，匹配正则表达式的a，匹配成功（此处如果匹配失败，会继续回到倒数第二个字符去匹配，依次类推，所以如果字符串很长，会出现回溯很多）

如果是非贪婪模式（勉强模式）：[\d,a]+？a
首先第一个字符匹配[\d,a]，因为是非贪婪的，所以第二个字符就开始匹配a，匹配失败，再用第二个字符匹配[\d,a]，依次类推

独占模式：[\d,a]++a
以前都没听过或者使用过这个模式，挺新鲜的，它其实就是不会回溯的贪婪模式，回头看贪婪模式的步骤，如果是独占模式，在倒数第二步匹配失败的时候，就会直接返回匹配失败，不会再回溯去尝试，所以其实是个很实用很高效的模式
```


##### ArrayList vs LinkedList

- ArrayList elementData被关键字transient修饰
    - ArrayList的数组是基于动态扩增 不是所有被分配的内存空间都存储了数据 为了避免这些没有存储数据的内存空间被序列化 
    - 内部提供了两个私有方法writeObject以及readObject来自我完成序列化与反序列化，从而在序列化与反序列化数组时节省了空间和时间
- ArrayList新增元素
    - 加到数组末尾 可能扩容，不会额外的有元素复制排序过程
        - 如果不会触发扩容，ArrayList在大量新增元素的场景下，性能并不会变差，反而比其他List集合的性能要好
    - 添加元素到任意位置 可能扩容，导致在该位置后的所有元素都需要重新排列
- ArrayList删除元素
    - 每次都要进行数组的重组，并且删除的元素位置越靠前，数组重组的开销就越大
  
- LinkedList存储数据的内存地址是不连续的，而是通过指针来定位不连续地址，因此，LinkedList不支持随机快速访问，LinkedList也就不能实现RandomAccess接口
- LinkedList添加到任意两个元素的中间位置和删除，相比ArrayList的添加操作来说，LinkedList的性能优势明显

- LinkedList在添加元素的时候，首先会通过循环查找到添加元素的位置，如果要添加的位置处于List的前半段，就从前往后找；若其位置处于后半段，就从后往前找。因此LinkedList添加元素到头部是非常高效的
- LinkedList避免用for循环，使用foreach 或 iterator
- 需要通过iterator remove的原因
```
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
```
##### Stream
- Java8中添加了一个新的接口类Stream，他和我们之前接触的字节流概念不太一样，Java8集合中的Stream相当于高级版的Iterator，他可以通过Lambda 表达式对集合进行各种非常便利、高效的聚合操作（Aggregate Operation），或者大批量数据操作 (Bulk Data Operation)
- 操作类型
    - 中间操作(间操作称为懒操作)只对操作进行了记录，即只会返回一个流，不会进行计算操作，而终结操作是实现了计算操作
    - 中间操作又可以分为无状态（Stateless）与有状态（Stateful）操作，前者是指元素的处理不受之前元素的影响，后者是指该操作只有拿到所有元素之后才能继续下去
    - 终结操作又可以分为短路（Short-circuiting）与非短路（Unshort-circuiting）操作，前者是指遇到某些符合条件的元素就可以得到最终结果，后者是指必须处理完所有元素才能得到最终结果
- 处理大数据的集合时，应该尽量考虑将应用部署在多核CPU环境下，并且使用Stream的并行迭代方式进行处理
- 在串行处理操作中
    - Stream在执行每一步中间操作时，并不会做实际的数据操作处理，而是将这些中间操作串联起来，最终由终结操作触发，生成一个数据处理链表，通过Java8中的Spliterator迭代器进行数据处理；此时，每执行一次迭代，就对所有的无状态的中间操作(StatelessOp)进行数据处理，而对有状态的中间操作，就需要迭代处理完所有的数据，再进行处理操作；最后就是进行终结操作的数据处理。
- 在并行处理操作中
    - Stream对中间操作基本跟串行处理方式是一样的，但在终结操作中，Stream将结合ForkJoin框架对集合进行切片处理，ForkJoin框架将每个切片的处理结果Join合并起来。最后就是要注意Stream的使用场景
##### 线程
- 在HotSpot VM的线程模型中，Java线程一对一映射为内核线程。Java线程的创建与销毁会消耗一定的计算机资源，增加系统的性能开销
- 大量创建线程给系统带来性能问题，内存和CPU资源将被线程抢占，如果处理不当，会发生内存溢出、CPU使用率超负荷等问题
- 确定线程数
    - CPU密集型任务 N（CPU核心数）+1
        - CPU核心数多出来的一个线程是为了防止线程偶发的缺页中断，或者其它原因导致的任务暂停而带来的影响。一旦任务暂停，CPU就会处于空闲状态，而在这种情况下多出来的一个线程就可以充分利用CPU的空闲时间
    - I/O密集型任务 2N
        - 线程在处理I/O的时间段内不会占用CPU来处理
    - 线程数=N（CPU核数）*（1+WT（线程等待时间）/ST（线程时间运行时间））
        - VisualVM来查看WT/ST
    - 从“N+1”和“2N”两个公式中选出一个适合的，计算出一个大概的线程数量，之后通过实际压测，逐渐往“增大线程数量”和“减小线程数量”这两个方向调整，然后观察整体的处理时间变化，最终确定一个具体的线程数量
    - 每个线程都会消耗栈空间 -Xss1M 设置每个线程的栈大小
- IO所需要的CPU资源非常少。大部分工作是分派给DMA完成的。
    - 计算机内部不只cpu一个部件 比如cpu将io事件分派给DMA(Direct Memory Access)芯片帮它完成 这样上下文切换才有真正的意义
    - cpu计算文件地址 => 委派DMA读取文件 => DMA接管总线 => CPU的A进程阻塞，挂起 => CPU切换到B进程 => DMA读取完文件后通知CPU(一个中断异常) => CPU切换回A进程操作文件   
- 实现线程主要有三种方式：轻量级进程和内核线程一对一相互映射实现的1:1线程模型、用户线程和内核线程实现的N:1线程模型以及用户线程和轻量级进程混合实现的N:M线程模型。
    - 1:1线程模型
        - 内核线程（Kernel-Level Thread, KLT）是由操作系统内核支持的线程；内核通过调度器对线程进行调度，并负责完成线程的切换
        - fork()函数创建一个子进程来代表一个内核中的线程，相当于复制了一个主进程；采用fork()创建子进程的方式来实现并行运行，会产生大量冗余数据，即占用大量内存空间，又消耗大量CPU时间用来初始化内存空间以及复制数据
        - clone()系统调用 创建轻量级进程（Light Weight Process，即LWP）；LWP是跟内核线程一对一映射的，每个LWP都是由一个内核线程支持；将部分父进程的资源的数据结构进行复制，复制内容可选，且没有被复制的资源可以通过指针共享给子进程。因此，轻量级进程的运行单元更小，运行速度更快
        - 1:1线程模型由于跟内核是一对一映射，所以在线程创建、切换上都存在用户态和内核态的切换，性能开销比较大。除此之外，它还存在局限性，主要就是指系统的资源有限，不能支持创建大量的LWP
    - N:1
        - 在用户空间完成了线程的创建、同步、销毁和调度，已经不需要内核的帮助了，也就是说在线程创建、同步、销毁的过程中不会产生用户态和内核态的空间切换
        - 于操作系统不能感知用户态的线程，因此容易造成某一个线程进行系统调用内核线程时被阻塞，从而导致整个进程被阻塞
    - N:M
        - 支持用户态线程通过LWP与内核线程连接，用户态的线程数量和内核态的LWP数量是N:M的映射关系
- java Thread
    - JDK 1.8 Thread.java 中 Thread#start 方法的实现，实际上是通过Native调用start0方法实现的；在Linux下， JVM Thread的实现是基于pthread_create实现的，而pthread_create实际上是调用了clone()完成系统调用创建线程的
    - 一个用户线程映射到一个内核线程，即1:1线程模型
    - 由于线程是通过内核调度，从一个线程切换到另一个线程就涉及到了上下文切换。
- 协程
    - 协程和线程密切相关，协程可以认为是运行在线程上的代码块，协程提供的挂起操作会使协程暂停执行，而不会导致线程阻塞
    - Kilim协程框架 通过协程框架在Java中使用协程。在有严重阻塞的场景下，协程的性能更胜一筹。其实，I/O阻塞型场景也就是协程在Java中的主要应用
    - 而Go语言是使用了N:M线程模型实现了自己的调度器，它在N个内核线程上多路复用（或调度）M个协程，协程的上下文切换是在用户态由协程调度器完成的，因此不需要陷入内核，相比之下，这个代价就很小了。
    - 可以将协程看作是一个类函数或者一块函数中的代码，我们可以在一个主线程里面轻松创建多个协程

##### 优化垃圾回收机制
- 对内存要求苛刻的情况下，需要提高对象的回收效率；在CPU使用率高的情况下，需要降低高并发时垃圾回收的频率
- 程序计数器、虚拟机栈和本地方法栈这3个区域是线程私有的，随着线程的创建而创建，销毁而销毁；栈中的栈帧随着方法的进入和退出进行入栈和出栈操作，每个栈帧中分配多少内存基本是在类结构确定下来的时候就已知的，因此这三个区域的内存分配和回收都具有确定性
- 堆中的回收主要是对象的回收，方法区的回收主要是(废弃常量和)无用的类的回收
- 一个对象到 GC Roots 没有任何引用链相连时，就证明此对象是不可用的, 如果执行一次finalize方法后依然没有引用链相连则回收
    - 弱引用 只被弱引用关联的对象 下一次垃圾回收就会被回收
    - 从而使程序能更加灵活的控制对象的生命周期
- 垃圾回收线程在JVM中是自动执行的，Java程序无法强制执行。我们唯一能做的就是通过调用System.gc方法来"建议"执行垃圾收集器，但是否可执行，什么时候执行？仍然不可预期
- Available Collectors
    - https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/collectors.html#sthref28
- GC性能衡量指标
    - 吞吐量
        - 应用程序耗时/系统总运行时间 
        - 系统总运行时间 = 应用程序耗时 + gc耗时
    - 停顿时间  
        - stop the world
        - 垃圾收集器正在运行时，应用程序的暂停时间
        - 串行回收器 单次停顿时间长；并发回收器，由于垃圾收集器和应用程序交替运行，程序的单次停顿时间就会变短，但其效率很可能不如独占垃圾收集器，系统的吞吐量也很可能会降低
    - 垃圾回收频率
        - 增大堆内存空间可以有效降低垃圾回收发生的频率，但同时也意味着堆积的回收对象越多，最终也会增加回收时的停顿时间
```
-XX:+PrintGC 输出GC日志
-XX:+PrintGCDetails 输出GC的详细日志
-XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）
-XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
-XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
-Xloggc:../logs/gc.log 日志文件的输出路径
```
- GC日志分析工具
    - GCViewer
    - GCeasy
- GC调优策略
    - 如果在堆内存中存在较多的长期存活的对象，此时增加年轻代空间，反而会增加Minor GC的时间。如果堆中的短期对象很多，那么扩容新生代，单次Minor GC时间不会显著增加
    - 因此，单次Minor GC时间更多取决于GC后存活对象的数量，而非Eden区的大小，所以可以增加年轻代大小、减少长期对象
    - 降低Full GC的频率 通常情况下，由于堆内存空间不足或老年代对象太多，会触发Full GC，频繁的Full GC会带来上下文切换，增加系统的性能开销
        - 减少创建大对象
        - 增大堆内存空间 设置初始化堆内存为最大堆内存
- 垃圾收集器的种类很多，我们可以将其分成两种类型，一种是响应速度快，一种是吞吐量高。通常情况下，CMS和G1回收器的响应速度快，Parallel Scavenge回收器的吞吐量高
- 在JDK1.8环境下，默认使用的是Parallel Scavenge（年轻代）+Serial Old（老年代）垃圾收集器

##### 优化JVM内存分配
- JVM内存表现出的性能问题
    - JVM内存溢出事故。这方面的事故往往是应用程序创建对象导致的内存回收对象难，一般属于代码编程问题
    - JVM内存分配不合理带来的性能表现并不会像内存溢出问题这么突出。可以说如果你没有深入到各项性能指标中去，是很难发现其中隐藏的性能损耗。
        - JVM内存分配不合理最直接的表现就是频繁的GC，这会导致上下文切换等性能问题，从而降低系统的吞吐量、增加系统的响应时间
        - 经历Minor GC年龄+1 ， 直接分配到老年代的对象内存大小阀值
        
```
appledeiMac:~ apple$ java -XX:+PrintFlagsFinal  -version | grep NewRatio
    uintx NewRatio                                  = 2                                   {product}
java version "1.8.0_221"
Java(TM) SE Runtime Environment (build 1.8.0_221-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.221-b11, mixed mode)
appledeiMac:~ apple$
appledeiMac:~ apple$ java -XX:+PrintFlagsFinal  -version | grep XX:SurvivorRatio
java version "1.8.0_221"
Java(TM) SE Runtime Environment (build 1.8.0_221-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.221-b11, mixed mode)
appledeiMac:~ apple$ java -XX:+PrintFlagsFinal  -version | grep SurvivorRatio
    uintx InitialSurvivorRatio                      = 8                                   {product}
    uintx MinSurvivorRatio                          = 3                                   {product}
    uintx SurvivorRatio                             = 8                                   {product}
    uintx TargetSurvivorRatio                       = 50                                  {product}
java version "1.8.0_221"
Java(TM) SE Runtime Environment (build 1.8.0_221-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.221-b11, mixed mode)
appledeiMac:~ apple$
```
- NewRatio=2=老年代/年轻代 即 在JDK1.7中，默认情况下年轻代和老年代的比例是1:2，我们可以通过–XX:NewRatio重置该配置项。
- SurvivorRatio=8=Eden/Survivor 即年轻代中的Eden和To Survivor、From Survivor的比例是8:1:1，我们可以通过-XX:SurvivorRatio重置该配置项
- 在JDK1.7中如果开启了-XX:+UseAdaptiveSizePolicy配置项，JVM将会动态调整Java堆中各个区域的大小以及进入老年代的年龄，–XX:NewRatio和-XX:SurvivorRatio将会失效，而JDK1.8是默认开启-XX:+UseAdaptiveSizePolicy配置项的
- 参考指标
    - GC频率
    - 内存： 堆的大小和堆中年轻代和老年代的比例
    - 吞吐量 tps
    - 响应时间 RT
- 调优
    - 调大堆内存大小减少FullGC
        - -Xmssize Sets the initial size (in bytes) of the heap
            - This value must be a multiple of 1024 and greater than 1 MB
            - If you do not set this option, then the initial size will be set as the sum of the sizes allocated for the old generation and the young generation
            - The initial size of the heap for the young generation can be set using the -Xmn option or the -XX:NewSize option.
        - -Xmxsize The -Xmx option is equivalent to -XX:MaxHeapSize.
            - Specifies the maximum size (in bytes) of the memory allocation pool in bytes
            - This value must be a multiple of 1024 and greater than 2 MB
            - -Xms and -Xmx are often set to the same value.
    - 调整年轻代减少MinorGC
        - -Xmnsize Sets the initial and maximum size (in bytes) of the heap for the young generation (nursery). 
        - The young generation region of the heap is used for new objects. GC is performed in this region more often than in other regions.
        - If the size for the young generation is too small, then a lot of minor garbage collections will be performed. If the size is too large, then only full garbage collections will be performed, which can take a long time to complete
        - Oracle recommends that you keep the size for the young generation between a half and a quarter of the overall heap size.
        - Instead of the -Xmn option to set both the initial and maximum size of the heap for the young generation, you can use -XX:NewSize to set the initial size and -XX:MaxNewSize to set the maximum size.
    - 设置Eden、Survivor区比例：在JVM中，如果开启 AdaptiveSizePolicy，则每次 GC 后都会重新计算 Eden、From Survivor和 To Survivor区的大小，计算依据是 GC 过程中统计的 GC 时间、吞吐量、内存占用量。这个时候SurvivorRatio默认设置的比例会失效
        - -XX:-UseAdaptiveSizePolicy关闭该项配置,固定Eden区的占用比例，来调优JVM的内存分配性能
        
##### 
```
top
 查看进程使用系统资源
top -Hp pid
查看单个进程下线程使用系统资源

vmstat
经常被用来观察系统整体的上下文切换

pidstat 
pidstat命令则是观察单个进程并深入到线程级别
pidstat -p 14304 -w  2  指定进程的上下文切换
* Cswch/s:每秒主动任务上下文切换数量
* Nvcswch/s:每秒被动任务上下文切换数量

pidstat -p 14304 -t  2 统计进程的线程信息

jstack
jstack pid 命令查看线程的堆栈信息
通常会结合top -Hp pid 或 pidstat -p pid -t一起查看具体线程的状态，也经常用来排查一些死锁的异常，可以查看到线程ID、线程的状态（wait、sleep、running 等状态）以及是否持有锁等。

jmap

堆信息
堆中对象直方图 对象数量 大小
dump堆内存快照 然后通过mat分析


jstat
监测Java应用程序的实时运行情况 ClassLoad  JIT编译  gc  新生代 老年代 元空间
jstat -gc -t -h2 8850 1000
jstat -options


```
- 案例


##### 32高性能SQL语句
- 慢SQL语句的几种常见诱因
    - 无索引 索引失效导致慢查询
    - 锁等待
        - InnoDB行锁升级为表锁
            - 批量更新操作 (一张表使用大量行锁，会导致事务执行效率下降，从而可能造成其它事务长时间锁等待和更多的锁冲突问题发生，致使性能严重下降，所以MySQL会将行锁升级为表锁)
            - 行锁是基于索引加的锁，如果我们在更新操作时，条件索引失效，那么行锁也会升级为表锁
        - 死锁
        - MyISAM表锁
    - 不恰当的SQL语句
        - 习惯使用<SELECT *>，<SELECT COUNT(*)> SQL语句
        - 大数据表中使用<LIMIT M,N>分页查询
        - 对非索引字段进行排序