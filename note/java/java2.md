

- 3.主导多个项目压测及性能调优, 有实际解决CPU过高，内存泄漏溢出等问题;

- AQS

- exceptions, basic input/output, concurrency, regular expressions, and the platform environment.




- CGlib vs JDK



#### unchecked exception VS checked exception
- Here's the bottom line guideline: If a client can reasonably be expected to recover from an exception, make it a checked exception. If a client cannot do anything to recover from the exception, make it an unchecked exception.

- unchecked exception(运行时异常、Runtime异常)
    - RuntimeException and its subclasses are unchecked exceptions
    - Unchecked exceptions do not need to be declared in a method or constructor's throws clause if they can be thrown by the execution of the method or constructor and propagate outside the method or constructor boundary
    - RuntimeException is the superclass of those exceptions that can be thrown during the normal operation of the Java Virtual Machine.
    - 无须显示声明抛出，也可以使用try...catch捕获
    - are the exceptions that are not checked at compiled time

- checked exception
    - **不是**RuntimeException类及其子类的异常实例
    - 必须显示处理
        - try...catch 捕获
        - 声明抛出该异常
    - are the exceptions that are checked at compile time
    - In Java exceptions under Error and RuntimeException classes are unchecked exceptions, everything else under throwable is checked.
- Error: 指在正常情况下, 不太可能出现的问题, 绝大部分的Error 都会导致程式(eg JVM 本身) 处于一种不正常且不可恢复的状态. 所以对于这种情况, 你也不太需要去catch 了, 因为也没什么意义. 常见的如OutOfMemoryError / StackOverflowError 这些, 都是继承自Error.

### gc
- 一个线程OOM后，其他线程还能运行
    - 当一个线程抛出OOM异常后，它所占据的内存资源会全部被释放掉，从而不会影响其他线程的运行
    - 一个线程溢出后，进程里的其他线程还能照常运行
    - 其实发生OOM的线程一般情况下会死亡，也就是会被终结掉，该线程持有的对象占用的heap都会被gc了，释放内存。因为发生OOM之前要进行gc，就算其他线程能够正常工作，也会因为频繁gc产生较大的影响
    
- 原子性 可见性 有序性
特性	    volatile关键字	synchronized关键字	Lock接口	    Atomic变量
原子性	无法保障	            可以保障	        可以保障	    可以保障
可见性	可以保障	            可以保障	        可以保障	    可以保障
有序性	一定程度保障	        可以保障	        可以保障	    无法保障    
- Synchronized和Lock也可以保证可见性，因为它们可以保证任一时刻只有一个线程能访问共享资源，并在其释放锁之前将修改的变量刷新到内存中，
- synchronized和Lock保证每个时刻是有一个线程执行同步代码，相当于是让线程顺序执行同步代码，自然就保证了有序性  


- join()方法常用来保证：确保能够完整的获取到插队线程的处理结果。
- thread.yield()：使得当前线程退让出CPU资源，把CPU调度机会分配给同样线程优先级的线程。
    - 线程t提示资源调度器，线程t愿意放弃其当前使用的处理资源。资源调度器可以忽略这个提示。当资源调度器忽略这个退让提示时，线程还是按照原来的调度进行。
    - yield()方法如果成功，则线程从Running(运行)状态切换至Runnable(就绪)状态
    - 分析：线程之间的退让需要用yield()方法实现，这种退让只能发生在同样优先级级别的线程之间。

- sleep()：让当前正在运行的线程休眠指定毫秒的时间。
- sleep()：休眠的线程并不会失去任何的监视器（可以理解为成锁）。

- wait()：让当前线程等待，直到另一个线程调用了当前对象上的notify()或者notifyAll()方法。
- wait()：在调用时，会释放当前对象的监视器的所有权（可以理解成解锁）。
- 调用wait()类的线程必须拥有这个对象的监视器（可以理解成锁）

- sleep()不会释放任何锁，wait()会释放对象上的锁。sleep()正常恢复的方式只能是等待时间耗尽，wait()除了等待时间耗尽，还可以被其他线程唤醒（notify()和notifyAll
- notify()：如果等待对象监视器的有多个线程，则选取其中一个线程进行唤醒。notify()：选择唤醒哪个线程是任意的，由CPU自己决定
- notifyAll()：唤醒等待对象监视器的所有线程。

- 如果当前线程处于阻塞或等待(因为调用wait、sleep和join造成的)状态时被interrupt了，那么[中断标志位]将被清除，并且收到一个InterruptedException异常。
- interrupt()方法并不是中断线程，而是中断阻或等待状态，或者将线程的[中断标志位]置为true。
    - 对于未阻塞和未等待的线程，interrupt()只是造成[中断标志位]=rue，线程本身运行状态不受影响。
    - 对于阻塞或等待的线程，interrupt()会中断阻塞或等待状态，使其转换成非阻塞或非等待状态，并清除[中断标志位]。
        - 阻塞或等待状态的线程被中断时，只是中断了阻塞或等待状态，即sleep()、wait()和join()，线程本身还在继续运行。
    - 造成阻塞或等待状态的情况有：sleep()、wait()和join()。

- 如果当前线程处于blocked阻塞(因为NIO的InterruptibleChannel进行的I/O操作造成的)状态时被interrupt了，则会关闭channel，[中断标志位]将会被置为true，并且当前线程会收到一个ClosedByInterruptException异常。
- 如果当前线程处于blocked阻塞(因为NIO的Selector造成的)状态时被interrupt了，那么[中断标志位]将被置为true，然后当前线程会立即从选择器区域返回并返回值（可能为非零的值）。
- 如果前面的情况都没有发生，则线程会将[中断标志位]将被置为true


  

- 线程的优先级具有继承传递性。子线程的优先级与父线程优先级一致。
    - 当运行中的某个线程，创建了一个新的线程时，这个新线程的初识优先级与创建它的线程一致。
- 虽然线程优先级有10个级别，但是推荐只使用内置的三个等级。
- 优先级较高的线程比优先级低的线程优先执行。
- 在同一时刻，优先级高的线程优先启动。
- 在多线程运行中，优先级高的线程能得到更多的计算资源。


- Thread state for a runnable thread.  A thread in the runnable
       * state is executing in the Java virtual machine but it may
       * be waiting for other resources from the operating system
       * such as processor.
- RUNNABLE：一个可以运行的线程的状态，可以运行是指这个线程已经在JVM中运行了，但是有可能正在等待其他的系统资源。也称之为就绪状态、可运行状态。



synchronized关键字主要有两大类6小类用法：
同步代码块
    加锁对象是本地变量的同步代码块
        - 同步代码块:加锁对象为类本地变量,通过类的多个对象调用同步代码块,并不能保证同步性。
        - 同步代码块:加锁对象为类本地变量,通过类的单个对象调用同步代码块,能够保证同步性。
    加锁对象是类静态变量的同步代码块
        - 同步代码块:加锁对象为类静态变量,通过类的多个对象调用同步代码块,能够保证同步性。
    加锁对象是共享变量的同步代码块
        - 同步代码块:加锁对象为共享变量,通过类的多个对象调用同步代码块,能够保证同步性。
    加锁对象是类对象的同步代码块(关于类对象指的是Class的对象)
        - 同步代码块:加锁对象为类对象,通过类的多个对象调用同步代码块,能够保证同步性。
同步方法
    修饰方法是普通方法的同步方法
        - 同步方法:修饰方法为普通方法,通过类的多个对象调用同步方法,不能够保证同步性。          
    修饰方法是类静态方法的同步方法
        - 同步方法:修饰方法为静态方法,通过类的多个对象调用同步方法，能够保证同步性。

- Condition          
obj.wati()/obj.notify()/obj.notifyAll常用来与synchronized关键字合作，进行线程状态控制。
同样的，Condition接口也提供了一系列比obj.wati()/obj.notify()/obj.notifyAll更加灵活和丰富的方法和Lock接口合作，进行线程状态控制。

以上所有的情形中，在await()方法返回之前，当前线程必须重新获取与当前Condition对象相关联的Lock锁。
为了线程调度目的，当前线程会变为不可用的，直到出现以下四种情形之一：
其他线程调用了当前Condition对象的signal()方法，并且当前线程恰好被选为被唤醒线程。
其他线程调用了当前Condition对象的signalAll()方法
其他线程调用了当前线程的interrupt()方法，而当前线程支持线程中断。
产生虚假唤醒。

awaitUninterruptibly
该等待不可中断。

 signal();
这个被唤醒的线程必须在从awit()方法返回之前重新获取锁。


#### function 
- Function
- Consumer
- Predicate
    - Represents a predicate (boolean-valued function) of one argument.
- Supplier
    - Represents a supplier of results.
- BiConsumer
- BiFunction

#### stream
- 一种对 Java 集合运算和表达的高阶抽象
- 要处理的元素集合看作一种流， 流在管道中传输， 并且可以在管道的节点上进行处理， 比如filter, map, reduce, find, match, sorted等操作
    - 中间操作（intermediate operation）的处理，
    - 最后由最终操作(terminal operation)得到前面处理的结果
- 内部迭代 中间操作都会返回流对象本身 可以对操作进行优化
- 编程风格


- Stream对象可以通过集合，文件, String, Stream, StreamBuilder等来构建
- filter
- map
    - 将一种类型的值转换为另外一种类型的值
- reduce
    - 是一个聚合方法，是将Stream中的元素进行某种操作，得到一个元素
```
System.out.println(Stream.of(1, 2, 3).reduce(0, (acc, element) -> acc + element));

```        
- flatMap
    - 将多个Stream连接成一个Stream，这时候不是用新值取代Stream的值，与map有所区别，这是重新生成一个Stream对象取而代之
```
List<String> words = new ArrayList<String>();
words.add("your");
words.add("name");


public static Stream<Character> characterStream(String s){  
    List<Character> result = new ArrayList<>();  
    for (char c : s.toCharArray()) 
        result.add(c);
    return result.stream();  
}
  
Stream<Stream<Character>> result = words.map(w -> characterStream(w));  
  
Stream<Character> letters = words.flatMap(w -> characterStream(w));  

//如果使用的是map方法，返回的是[ ...['y', 'o', 'u', 'r'], ['n', 'a', 'm', 'e']]
   
  
//如果使用的是flatMap方法，返回的是['y', 'o', 'u', 'r', 'n', 'a', 'm', 'e']
```    
- distinct
- sorted
    - 内部的元素实现Comparable 或 接收一个Comparator的参数
- forEach      
- collect
    - 聚合方法
    - 将Stream的通过一个收集器，转换成一个具体的值，是一个聚合方法
- min max    
    - 聚合方法
- count
- findFirst

    

#### util
- Optional
    - A container object which may or may not contain a non-null value.
    - Optional类来优化避免null导致的NullPointerException的写法
    

### 线程池数
- IO密集型通常设置为2n+1，其中n为CPU核数
    - 线程数 = CPU核心数/(1-阻塞系数)
- CPU密集型通常设置为 n+1    
IO密集型，可以考虑多设置一些线程，主要目的是可以增加IO的并发度，CPU密集型不宜设置过多线程，因为是会造成线程切换，反而损耗性能。
接下来我们以一个实际的场景来说明如何设置线程数量。
一个4C8G的机器上部署了一个MQ消费者，在RocketMQ的实现中，消费端也是用一个线程池来消费线程的，那这个线程数要怎么设置呢？
如果按照 2n + 1 的公式，线程数设置为 9个，但在我们实践过程中发现如果增大线程数量，会显著提高消息的处理能力，说明 2n + 1 对于业务场景来说，并不太合适。
如果套用 线程数 = CPU核心数/(1-阻塞系数) 阻塞系数取 0.8 ，线程数为 20 。阻塞系数取 0.9，大概线程数40，20个线程数我觉得可以。
如果我们发现数据库的操作耗时比较多，此时可以继续提高阻塞系数，从而增大线程数量。
那我们怎么判断需要增加更多线程呢？其实可以用jstack命令查看一下进程的线程栈，如果发现线程池中大部分线程都处于等待获取任务(ThreadPoolExecutor#getTask())，则说明线程够用
如果大部分线程都处于运行状态，可以继续适当调高线程数量。

- 计算线程数
    - 在《Java Concurrency in Practice》一书中，给出了估算线程池大小的公式：
      Nthreads=Ncpu * Ucpu * (1+w/c)，其中
      Ncpu=CPU核心数
      Ucpu=cpu使用率，0~1
      W/C=等待时间与计算时间的比率
      假设cpu100%运转，即撇开CPU使用率这个因素，线程数=Ncpu*(1+w/c)。
    - 在《Java 虚拟机并发编程》线程数=Ncpu/（1-阻塞系数）
    - 现在假设将派系二的公式等于派系一公式，即Ncpu/（1-阻塞系数）=Ncpu*(1+w/c), 得出阻塞系数=w/(w+c)，即阻塞系数=阻塞时间/（阻塞时间+计算时间），而阻塞系数正是这个，派系一和派系二其实是一个公式
                     
- IO密集型：一般情况下，如果存在IO，那么肯定w/c>1（阻塞耗时一般都是计算耗时的很多倍）,但是需要考虑系统内存有限（每开启一个线程都需要内存空间），这里需要上服务器测试具体多少个线程数适合（CPU占比、线程数、总耗时、内存消耗）。如果不想去测试，保守点取1即，Nthreads=Ncpu*(1+1)=2Ncpu。这样设置一般都OK。
    - （常出现于线程中：数据库数据交互、文件上传下载、网络数据传输等等）
- 计算密集型：假设没有等待w=0，则W/C=0. Nthreads=Ncpu。
    - 即对于计算密集型的任务，在拥有N个处理器的系统上，当线程池的大小为N+1时，通常能实现最优的效率。(即使当计算密集型的线程偶尔由于缺失故障或者其他原因而暂停时，这个额外的线程也能确保CPU的时钟周期不会被浪费。)
    - （常出现于线程中：复杂算法）   
    - 即，计算密集型=Ncpu+1，但是这种做法导致的多一个cpu上下文切换是否值得
      
- 内存
每个线程占用内存 计算线程大小 

- 线程池参数设置
参数的设置跟系统的负载有直接的关系，下面为系统负载的相关参数：
tasks，每秒需要处理的的任务数（针对系统需求）
threadtasks，每个线程每钞可处理任务数（针对线程本身）
responsetime，系统允许任务最大的响应时间，比如每个任务的响应时间不得超过2秒。

corePoolSize
系统每秒有tasks个任务需要处理理，则每个线程每钞可处理threadtasks个任务。，则需要的线程数为：tasks/threadtasks，即tasks/threadtasks个线程数。
假设系统每秒任务数为100 ~ 1000，每个线程每钞可处理10个任务，则需要100 / 10至1000 / 10，即10 ~ 100个线程。那么corePoolSize应该设置为大于10，具体数字最好根据8020原则，因为系统每秒任务数为100 ~ 1000，即80%情况下系统每秒任务数小于1000 * 20% = 200，则corePoolSize可设置为200 / 10 = 20。
queueCapacity
任务队列的长度要根据核心线程数，以及系统对任务响应时间的要求有关。队列长度可以设置为 所有核心线程每秒处理任务数 * 每个任务响应时间 = 每秒任务总响应时间 ，即(corePoolSize*threadtasks)*responsetime： (20*10)*2=400，即队列长度可设置为400。
maxPoolSize
当系统负载达到最大值时，核心线程数已无法按时处理完所有任务，这时就需要增加线程。每秒200个任务需要20个线程，那么当每秒达到1000个任务时，则需要（tasks - queueCapacity）/ threadtasks 即(1000-400)/10，即60个线程，可将maxPoolSize设置为60。
队列长度设置过大，会导致任务响应时间过长，切忌以下写法：
LinkedBlockingQueue queue = new LinkedBlockingQueue();
复制代码这实际上是将队列长度设置为Integer.MAX_VALUE，将会导致线程数量永远为corePoolSize，再也不会增加，当任务数量陡增时，任务响应时间也将随之陡增。

### clone
```
package com.zlb.clone;

import java.io.*;

/**
 * @author Yuanming Tao
 * Created on 2019/3/4
 * Description
 */
public class DemoClone implements Cloneable {

    public static void main(String[] args) {
        //1 shallow copy (primitive fields or references to immutable objects 是完全新的(references to immutable objects克隆和原对象还是使用相同的引用，只是immutable objects不可修改，修改后也是新的对象，克隆和原对象不会互相影响)，references to mutable objects 是相同的，这就是浅拷贝)
        //1.1 must implements Cloneable
        //this method creates a new instance of the class of this
        //     * object and initializes all its fields with exactly the contents of
        //     * the corresponding fields of this object, as if by assignment; the
        //     * contents of the fields are not themselves cloned. Thus, this method
        //     * performs a "shallow copy" of this object, not a "deep copy" operation.
        DemoClone demoClone = new DemoClone();
        DemoClone demoClone1;
        try {
            demoClone1 = (DemoClone)demoClone.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        //1.2 Note that all arrays
        //     * are considered to implement the interface {@code Cloneable} and that
        //     * the return type of the {@code clone} method of an array type {@code T[]}
        //     * is {@code T[]} where T is any reference or primitive type
        String[] strArr = new String[]{"1", "2", "3"};
        String[] strArr1 = strArr.clone();

        //2 deep copy
        //By convention, the object returned by this method should be independent
        //     * of this object (which is being cloned).
        //To achieve this independence,
        //     * it may be necessary to modify one or more fields of the object returned
        //     * by {@code super.clone} before returning it
        //Typically, this means
        //     * copying any mutable objects that comprise the internal "deep structure"
        //     * of the object being cloned and replacing the references to these
        //     * objects with references to the copies.
        //If a class contains only
        //     * primitive fields or references to immutable objects(e.g., String), then it is usually
        //     * the case that no fields in the object returned by {@code super.clone}
        //     * need to be modified.
        //2.1 深拷贝需要replacing the references to these mutable objects with references to the copies.
        Teacher teacher = new Teacher("王老师", "英语");
        Student student = new Student("小明", 11, teacher);
        Student clone = null;
        try {
            clone = (Student) student.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return;
        }
        clone.setName("小强");
        clone.setAge(20);
        clone.getTeacher().setName("李老师");

        System.out.println(student.getName() + " " + student.getAge());
        System.out.println(clone.getName() + " " + clone.getAge());
        System.out.println(clone.getTeacher() == student.getTeacher());
        System.out.println(clone.getTeacher().getName() + " " + student.getTeacher().getName());

        //2.2 序列化实现深克隆 Java可以把对象序列化写进一个流里面，反之也可以把对象从序列化流里面读取出来，但这一进一出，这个对象就不再是原来的对象了
        Teacher teacher2 = new Teacher("王老师", "英语");
        StudentSerializable studentSerializable = new StudentSerializable("小明", 11, teacher2);
        StudentSerializable clone2 = null;
        try {
            clone2 = studentSerializable.serializableClone();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println(clone2.getTeacher() == studentSerializable.getTeacher());


    }

     static class Student implements Cloneable {

         private String name;
         private int age;
         private Teacher teacher;

         public Student(String name, int age, Teacher teacher) {
             this.name = name;
             this.age = age;
             this.teacher = teacher;
         }

         /**
          * 重写Object的Clone方法并在方法中调用super.clone()
          * @return
          * @throws CloneNotSupportedException
          */
         @Override
         protected Object clone() throws CloneNotSupportedException {
             Student student = null;
             try {
                 student = (Student) super.clone();
                 //为了实现深克隆
                 //1 克隆对象里面的对象类型也必须实现Cloneable接口
                 //2 并调用clone()，
                 //3 replacing the references to these mutable objects with references to the copies
//                 Teacher teacher = this.teacher.clone();
//                 student.setTeacher(teacher);
             } catch (CloneNotSupportedException e) {
                 e.printStackTrace();
             }
             return student;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public int getAge() {
             return age;
         }

         public void setAge(int age) {
             this.age = age;
         }

         public Teacher getTeacher() {
             return teacher;
         }

         public void setTeacher(Teacher teacher) {
             this.teacher = teacher;
         }
     }

     static class Teacher implements Cloneable, Serializable {
         private static final long serialVersionUID = 1L;

         private String name;
         private String course;

         public Teacher(String name, String course) {
             this.name = name;
             this.course = course;
         }

//         /**
//          * 为了实现深克隆
//          * @return
//          */
//         @Override
//         public Teacher clone() {
//
//             Teacher clone = null;
//             try {
//                 clone = (Teacher) super.clone();
//             } catch (CloneNotSupportedException e) {
//                 e.printStackTrace();
//             }
//             return clone;
//         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public String getCourse() {
             return course;
         }

         public void setCourse(String course) {
             this.course = course;
         }
     }

    static class StudentSerializable implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private int age;
        private Teacher teacher;

        public StudentSerializable(String name, int age, Teacher teacher) {
            this.name = name;
            this.age = age;
            this.teacher = teacher;
        }

        /**
         * 如果一个可序列化的对象包含对某个不可序列化的对象的引用，那么整个序列化操作将会失败，并且会抛出一个NotSerializableException.
         * Teacher必须实现序列化接口
         *
         * 非静态内部类拥有对外部类的所有成员的完全访问权限，包括实例字段和方法。为实现这一行为，非静态内部类存储着对外部类的实例的一个隐式引用。序列化时要求所有的成员变量是Serializable,现在外部的类并没有implements Serializable,所以就抛出java.io.NotSerializableException异常
         * @return
         * @throws IOException
         * @throws ClassNotFoundException
         */
        public StudentSerializable serializableClone() throws IOException, ClassNotFoundException {
            StudentSerializable clone;

            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(this);
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            clone = (StudentSerializable) oi.readObject();

            return clone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }
    }

}

```

### 系统属性和环境变量
- System.getProperty()
    - 系统属性，java平台使用Properties 对象去提供本地系统配置信息
    - 可以使用属性命令行参数方式传递我们属性或配置值给应用程序：java -jar jarName -DpropertyName=value
    
- System.getenv()
    - 环境变量与属性类似，也是键值对形式。大多数操作系统使用环境变量，可以把配置信息传递给应用程序
    - 当创建一个进程是，缺省其继承其父进程的环境变量副本
- 可以在运行时更新属性，环境变量是操作系统变量的不可变副本，不能修改
- 属性仅在java平台中有效，而环境变量是全局的，属于操作系统级——运行在同一台机器上的所有应用都有效



### 注解覆盖
- 编写自定义注解时未写@Inherited的运行结果

子类没有继承到父类类上Annotation

子类实现父类的abstractMethod抽象方法，没有继承到父类抽象方法中的Annotation

子类继承父类的doExtends方法，继承到父类doExtends方法中的Annotation,其信息如下：父类的doExtends方法

子类覆盖父类的doHandle方法，没有继承到父类doHandle方法中的Annotation

- 编写自定义注解时写了@Inherited的运行结果

子类继承到父类类上Annotation,其信息如下：类名上的注解

子类实现父类的abstractMethod抽象方法，没有继承到父类抽象方法中的Annotation

子类继承父类的doExtends方法，继承到父类doExtends方法中的Annotation,其信息如下：父类的doExtends方法

子类覆盖父类的doHandle方法，没有继承到父类doHandle方法中的Annotation

- 可以通过指定@Inherited注解，指明自定义注解是否可以被继承。通过测试结果来看，@Inherited 只是可控制 对类名上注解是否可以被继承。不能控制方法上的注解是否可以被继承，方法级别继承父类方法可以继承注解，实现抽象方法、覆盖方法不行
- Spring 实现事务的注解@Transactional 是可以被继承的，通过查看它的源码可以看到@Inherited。
- Spring注解属性覆盖
    - 较低层次的注解覆盖其元注解的同名属性
    - @AliasFor提供的属性覆盖能力。


###   注解原理       
注解原理：
注解本质是一个继承了Annotation的特殊接口，其具体实现类是JDK动态代理生成的代理类。我们通过反射获取注解时，返回的也是Java运行时生成的动态代理对象。通过代理对象调用自定义注解的方法，会最终调用AnnotationInvocationHandler的invoke方法，该方法会从memberValues这个Map中查询出对应的值，而memberValues的来源是Java常量池



#### 多态的实现

下面从虚拟机运行时的角度来简要介绍多态的实现原理，这里以Java虚拟机（Java Virtual Machine, JVM）规范的实现为例。JVM 与 Linux 的内存关系详解，这个推荐大家看一下。关注Java技术栈微信公众号，在后台回复关键字：JVM，可以获取更多栈长整理的JVM技术干货。

在JVM执行Java字节码时，类型信息被存放在方法区中，通常为了优化对象调用方法的速度，方法区的类型信息中增加一个指针，该指针指向一张记录该类方法入口的表（称为方法表），表中的每一项都是指向相应方法的指针。

方法表的构造如下：

由于Java的单继承机制，一个类只能继承一个父类，而所有的类又都继承自Object类。方法表中最先存放的是Object类的方法，接下来是该类的父类的方法，最后是该类本身的方法。这里关键的地方在于，如果子类改写了父类的方法，那么子类和父类的那些同名方法共享一个方法表项，都被认作是父类的方法。

注意这里只有非私有的实例方法才会出现，并且静态方法也不会出现在这里，原因很容易理解：静态方法跟对象无关，可以将方法地址直接引用，而不像实例方法需要间接引用。

更深入地讲，静态方法是由虚拟机指令invokestatic调用的，私有方法和构造函数则是由invokespecial指令调用，只有被invokevirtual和invokeinterface指令调用的方法才会在方法表中出现。

由于以上方法的排列特性（Object——父类——子类），使得方法表的偏移量总是固定的。例如，对于任何类来说，其方法表中equals方法的偏移量总是一个定值，所有继承某父类的子类的方法表中，其父类所定义的方法的偏移量也总是一个定值。

前面说过，方法表中的表项都是指向该类对应方法的指针，这里就开始了多态的实现：

假设Class A是Class B的子类，并且A改写了B的方法method()，那么在B的方法表中，method方法的指针指向的就是B的method方法入口。

而对于A来说，它的方法表中的method方法则会指向其自身的method方法而非其父类的（这在类加载器载入该类时已经保证，同时JVM会保证总是能从对象引用指向正确的类型信息）。

结合方法指针偏移量是固定的以及指针总是指向实际类的方法域，我们不难发现多态的机制就在这里：

在调用方法时，实际上必须首先完成实例方法的符号引用解析，结果是该符号引用被解析为方法表的偏移量。

虚拟机通过对象引用得到方法区中类型信息的入口，查询类的方法表，当将子类对象声明为父类类型时，形式上调用的是父类方法，此时虚拟机会从实际类的方法表（虽然声明的是父类，但是实际上这里的类型信息中存放的是子类的信息）中查找该方法名对应的指针（这里用“查找”实际上是不合适的，前面提到过，方法的偏移量是固定的，所以只需根据偏移量就能获得指针），进而就能指向实际类的方法了。

我们的故事还没有结束，事实上上面的过程仅仅是利用继承实现多态的内部机制，多态的另外一种实现方式：实现接口相比而言就更加复杂，原因在于，Java的单继承保证了类的线性关系，而接口可以同时实现多个，这样光凭偏移量就很难准确获得方法的指针。所以在JVM中，多态的实例方法调用实际上有两种指令：

invokevirtual指令用于调用声明为类的方法；
invokeinterface指令用于调用声明为接口的方法。
当使用invokeinterface指令调用方法时，就不能采用固定偏移量的办法，只能老老实实挨个找了（当然实际实现并不一定如此，JVM规范并没有规定究竟如何实现这种查找，不同的JVM实现可以有不同的优化算法来提高搜索效率）。

我们不难看出，在性能上，调用接口引用的方法通常总是比调用类的引用的方法要慢。这也告诉我们，在类和接口之间优先选择接口作为设计并不总是正确的，当然设计问题不在本文探讨的范围之内，但显然具体问题具体分析仍然不失为更好的选择。

个人见解：多态机制包括静态多态（编译时多态）和动态多态（运行时多态），静态多态比如说重载，动态多态是在编译时不能确定调用哪个方法，得在运行时确定。动态多态的实现方法包括子类继承父类和类实现接口。当多个子类上转型（不知道这么说对不）时，对象掉用的是相应子类的方法，这种实现是与JVM有关的。

本文分享自微信公众号 - Java技术栈（javastack）

原文出处及转载信息见文内详细说明，如有侵权，请联系 yunjia_community@tencent.com 删除。