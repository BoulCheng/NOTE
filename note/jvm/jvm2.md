


#### gc频繁问题如何解决

#### JVM内存结构

#### Java内存模型
- Java线程之间的通信由Java内存模型（Java Memory Model，简称JMM）控制。JMM决定一个线程对共享变量的写入何时对另一个线程可见

#### Java线程模型

#### 对象的内存结构

#### 对象的创建过程


#### 栈帧 局部变量表 操作数栈

#### GC

#### 指令重排
- 


#### 堆外直接内存
- 堆内内存是由JVM所管控的Java进程内存，我们平时在Java中创建的对象都处于堆内内存中，并且它们遵循JVM的内存管理机制，JVM会采用垃圾回收机制统一管理它们的内存。那么堆外内存就是存在于JVM管控之外的一块内存区域，因此它是不受JVM的管控
- 堆外内存优势在 IO 操作上，如对于网络 IO，使用 Socket 发送数据时，能够节省堆内存到堆外内存的数据拷贝
- ByteBuffer.allocateDirect()
    - 直接内存申请空间其实是比较消耗性能,需要本地方法通过系统调用完成
    - 不受JVM堆的大小限制，会受到本机总内存（包括RAM及SWAP区或者分页文件）的大小及处理器寻址空间的限制,可能会抛出OutOfMemoryError异常，直接内存的最大大小可以通过-XX:MaxDirectMemorySize来设置，默认是64M
    - 由于申请内存前可能会调用 System.gc()，所以谨慎设置 -XX:+DisableExplicitGC 这个选项，这个参数作用是禁止代码中显示触发的 Full GC
- 回收
    - gc自动回收
        - GC 时会扫描 DirectByteBuffer 对象是否有有效引用指向该对象，如没有，在回收 DirectByteBuffer 对象的同时且会回收其占用的堆外内存
        - 虚引用（Phantom Reference）
            - 也称为幽灵引用或者幻影引用
            - 一个对象是否有虚引用的存在，完全不会对其生存时间构成影响，也无法通过虚引用来取得一个对象实例。
            - 为一个对象设置虚引用关联的唯一目的就是能在这个对象被收集器回收时收到一个系统通知
                - 它并不被期待用来取得目标对象的引用，而目标对象被回收前，它的引用会被放入一个 ReferenceQueue 对象中，从而达到跟踪对象垃圾回收的作用
            - GC过程中如果发现某个对象除了只有PhantomReference引用它之外，并没有其他的地方引用它了，那将会把这个引用放到java.lang.ref.Reference.pending队列里，ReferenceHandler这个守护线程会处理pending队列里，执行一些后置处理，这里是调用Cleaner的clean方法
            - 而DirectByteBuffer构造方法内创建了一个Cleaner对象， Cleaner继承了PhantomReference，其referent为DirectByteBuffer，也是通过Cleaner调用unsafe.freeMemory(address)来释放直接内存
    - 手动回收
        - DirectByteBuffer 实现了 DirectBuffer 接口，这个接口有 cleaner 方法可以获取 cleaner 对象。
        ```
        public static void clean(final ByteBuffer byteBuffer) {  
          if (byteBuffer.isDirect()) {  
              ((DirectBuffer)byteBuffer).cleaner().clean();  
          }  
        }
        ```
        - Netty 中的堆外内存池就是使用反射来实现手动回收方式进行回收的。
        - 当堆空间非常富余，直接内存占用虽然很高，但并不会自发引起 GC——哪怕直接内存已经用满。 如果不触发 GC，直接内存可能就会溢出。因此，只能人为去触发 GC，从而回收直接内存。


[https://cloud.tencent.com/developer/article/1543037]

- javac 解析器 (热点检测)即使编译器(逃逸分析 标量替换栈上分配)  类加载 new对象 TLAB
- JVM内存结构
- Java内存模型 
- gc
-  


### 反射
Java的反射是指程序在运行期可以拿到一个对象的所有信息。
反射是为了解决在运行期，对某个实例一无所知的情况下，如何调用其方法

JVM动态加载class的特性：JVM在执行Java程序的时候，并不是一次性把所有用到的class全部加载到内存，而是第一次需要用到class时才加载
JVM为每个加载的class及interface创建了对应的Class实例来保存class及interface的所有信息；
获取一个class对应的Class实例后，就可以获取该class的所有信息；
通过Class实例获取class信息的方法称为反射（Reflection）；
JVM总是动态加载class，可以在运行期根据条件来控制加载class

Reflection enables Java code to discover information about the fields, methods and constructors of loaded classes, and to use reflected fields, methods, and constructors to operate on their underlying counterparts, within security restrictions.
The API accommodates applications that need access to either the public members of a target object (based on its runtime class) or the members declared by a given class. It also allows programs to suppress default reflective access control.
程序中一般的对象的类型都是在编译期就确定下来的，而 Java 反射机制可以动态地创建对象并调用其属性，这样的对象的类型在编译期是未知的。所以我们可以通过反射机制直接创建对象，即使这个对象的类型在编译期是未知的

反射的核心是 JVM 在运行时才动态加载类或调用方法/访问属性，它不需要事先（写代码的时候或编译期）知道运行对象是谁。


### 字节码增强
- ASM虽然可以达到修改字节码的效果，但是代码实现上更偏底层，是一个个虚拟机指令的组合，不好理解、记忆，和Java语言的编程习惯有较大差距
- 利用Javassist实现字节码增强时，可以无须关注字节码刻板的结构，其优点就在于编程简单。直接使用java编码的形式，而不需要了解虚拟机指令，就能动态改变类的结构或者动态生成类
- Javassist源代码级API只需要很少的字节码知识，甚至不需要任何实际字节码知识，因此实现起来更容易、更快
- Javassist使用反射机制，这使得它比运行时使用Classworking技术的ASM慢
- 总之，如果有人需要更简单的方法来动态操作或创建Java类，那么应该使用Javassist API 。如果需要注重性能地方，应该使用ASM库

- ASM和JavaAssist的Demo，都有一个共同点：两者例子中的目标类都没有被提前加载到JVM中，如果只能在类加载前对类中字节码进行修改，那将失去其存在意义，毕竟大部分运行的Java系统，都是在运行状态的线上系统
    - AspectJ的织入编译器，“编译期：切面在目标类编译时被织入。这种方式需要特殊的编译器
- JVM运行时去动态的重新加载类(JVM运行时对类的重加载)
    - 在JVM运行时，改变类的字节码并重新载入类信息
    - 如果只能在类加载前对类进行强化，那字节码增强技术的使用场景就变得很窄了。期望的效果是：在一个持续运行并已经加载了所有类的JVM中，还能利用字节码增强技术对其中的类行为做替换并重新加载, 在一个JVM中，先加载了一个类，然后又对其进行字节码增强并重新加载
    - Instrument
        - 它需要依赖JVMTI的Attach API机制实现(Java 虚拟机工具接口（JVMTI）)
            - 可以借助JVMTI的一部分能力，帮助动态重载类信息。JVM TI（JVM TOOL INTERFACE，JVM工具接口）是JVM提供的一套对JVM进行操作的工具接口
        - instrument是JVM提供的一个可以修改已加载类的类库，在JDK 1.6以前，instrument只能在JVM刚启动开始加载类时生效，而在JDK 1.6之后，instrument支持了在运行时对类定义的修改
        - 要使用instrument的类修改功能，我们需要实现它提供的ClassFileTransformer接口，定义一个类文件转换器。接口中的transform()方法会在类文件被加载时调用，而在transform方法里，我们可以利用上文中的ASM或Javassist对传入的字节码进行改写或替换，生成新的字节码数组后返回。
    - Agent
        - Agent就是JVMTI的一种实现，Agent有两种启动方式，一是随Java进程启动而启动，经常见到的java -agentlib就是这种方式；二是运行时载入，通过attach API，将模块（jar包）动态地Attach到指定进程id的Java进程内
        - 随着进程启动的Premain方式(在执行 main 函数之前，JVM 会先运行-javaagent所指定 jar 包内 Premain-Class 这个类的 premain 方法(“类加载期：切面在目标类加载到JVM时被织入。这种方式需要特殊的类加载器（ClassLoader），它可以在目标类被引入应用之前增强该目标类的字节码。AspectJ 5的加载时织入（load-time weaving，LTW）))的Agent更偏向是一种初始化加载时的修改方式，而Attach API的loadAgent()方法，能够将打包好的Agent jar包动态Attach到目标JVM上，是一种运行时注入Agent、修改字节码的方式
        
### 代理
- CGLIB来创建一个继承实现类的子类，用Asm库动态修改子类的代码来实现AOP效果
    - 缺点:不能对final修饰的类或方法进行增强
      
### 字节码文件
- Java之所以可以“一次编译，到处运行”，一是因为JVM针对各种操作系统、平台都进行了定制，二是因为无论在什么平台，都可以编译生成固定格式的字节码（.class文件）供JVM使用。因此，也可以看出字节码对于Java生态的重要性。之所以被称之为字节码，是因为字节码文件由十六进制值组成，而JVM以两个十六进制值为一组，即以字节为单位进行读取
- JVM规范要求每一个字节码文件都要由十部分按照固定的顺序组成
    - 魔数放在文件开头，JVM可以根据文件的开头来判断这个文件是否可能是一个.class文件，如果是，才会继续进行之后的操作
    - 1[https://tech.meituan.com/2019/09/05/java-bytecode-enhancement.html]