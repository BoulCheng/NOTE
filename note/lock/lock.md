#锁

- 悲观锁
    - 悲观的假设更新的同时一定会有其他线程同时更新，即假设每次去更新的时候数据都会被修改(即更新非常频繁)
    - 更新时加锁，加锁期间其他线程无法访问资源，只有当更新操作完成后并释放资源锁定后其他线程才可以访问
    - 写多场景，写时虽然需要获取锁，但无需自旋重试
    - synchronized
    - 缺点
        - 当资源被锁定时间比较长时，仅仅是读访问也可能等待比较长时间
- 乐观锁
    - 乐观的假设更新的同时不会有其他线程同时更新，即假设去更新的时刻数据不会被修改(即更新不频繁的场景)
    - 更新时无锁，其他线程依然可以同时正常读访问，但写访问会失败然后重试
    - 通过版本号机制和CAS无锁算法实现，CAS保证原子性 (具体如何保证原子性)
    - 读多场景, 更新时无锁，多线程同时读访问提高并发，即冲突真的很少发生的时候
    - java.util.concurrent.atomic
    - 缺点
        - ABA的问题，就是说：假如一个值原来是A，变成了B，又变成了A，那么CAS检查时会发现它的值没有发生变化，但是实际上却变化了
            - 预期执行一次，但实际因为其他不可控因素导致执行了多次可能会产生错误结果，如用户金额变化操作A： 100 -> 50 ; 操作B: 50 -> 100 ; 但操作A因不可控因素执在操作B前后都执行了一次就会导致错误
            - AtomicStampedReference AtomicMarkableReference 解决
        - 自旋时间长开销大
            - 限制自旋次数，防止进入死循环
        - 只能保证一个共享变量的原子操作
            - 多个共享变量进行操作，可以使用加锁方式(悲观锁)保证原子性
            - 多个共享变量合并成一个共享变量进行CAS操作


- 死锁
    - 死锁的定义：“死锁是指两个或两个以上的进程在执行过程中，由于竞争资源或者由于彼此通信而造成的一种阻塞的现象，若无外力作用，它们都将无法推进下去。”那么我们换一个更加规范的定义：“集合中的每一个进程都在等待只能由本集合中的其他进程才能引发的事件，那么该组进程是死锁的。”
        - 死锁条件里面的竞争资源，可以是线程池里的线程、网络连接池的连接，数据库中数据引擎提供的锁，等等一切可以被称作竞争资源的东西
      
- 死锁检测
    - Jstack 
    - JConsole
    
- 死锁预防
    - 以确定的顺序获得锁
        - 简单按照锁对象的hashCode进行排序（单纯按照hashCode顺序排序会出现“环路等待”）
        - 银行家算法
    - 超时放弃
        - 超时放弃
        - Lock接口提供了boolean tryLock(long time, TimeUnit unit) throws InterruptedException
        - 获取锁超时以后，主动释放之前已经获得的所有的锁。通过这种方式，也可以很有效地避免死锁
        


- Dead Lock
```
zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$ cat LockDemo.java
/**
 * @author Yuanming Tao
 * Created on 2019/9/12
 * Description
 */
public class LockDemo {
    public static void main(String[] args) {

        final Object a = new Object();
        final Object b = new Object();
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                synchronized (a) {
                    try {
                        System.out.println("now i in threadA-locka");
                        Thread.sleep(1000l);
                        synchronized (b) {
                            System.out.println("now i in threadA-lockb");
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        });

        Thread threadB = new Thread(new Runnable() {
            public void run() {
                synchronized (b) {
                    try {
                        System.out.println("now i in threadB-lockb");
                        Thread.sleep(1000l);
                        synchronized (a) {
                            System.out.println("now i in threadB-locka");
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        });

        threadA.start();
        threadB.start();
    }
}

zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$ ls -l
total 32
-rw-r--r--   1 zlb  staff   996  9 12 15:32 LockDemo$1.class
-rw-r--r--   1 zlb  staff   996  9 12 15:32 LockDemo$2.class
-rw-r--r--   1 zlb  staff   549  9 12 15:32 LockDemo.class
-rw-r--r--   1 zlb  staff  1376  9 12 15:32 LockDemo.java
zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$ java LockDemo
now i in threadA-locka
now i in threadB-lockb



```
```
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ jps
4736
4758 RemoteMavenServer
9207 LockDemo
4747 Launcher
9213 Jps
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ jstack 9207
2019-09-12 15:35:54
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.171-b11 mixed mode):

"Attach Listener" #12 daemon prio=9 os_prio=31 tid=0x00007fc2d0800000 nid=0xc07 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"DestroyJavaVM" #11 prio=5 os_prio=31 tid=0x00007fc2cf887000 nid=0x2703 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Thread-1" #10 prio=5 os_prio=31 tid=0x00007fc2d0024800 nid=0x4303 waiting for monitor entry [0x000070000557c000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at LockDemo$2.run(LockDemo.java:34)
	- waiting to lock <0x00000007955f0f68> (a java.lang.Object)
	- locked <0x00000007955f0f78> (a java.lang.Object)
	at java.lang.Thread.run(Thread.java:748)

"Thread-0" #9 prio=5 os_prio=31 tid=0x00007fc2d0023800 nid=0x3c03 waiting for monitor entry [0x0000700005479000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at LockDemo$1.run(LockDemo.java:18)
	- waiting to lock <0x00000007955f0f78> (a java.lang.Object)
	- locked <0x00000007955f0f68> (a java.lang.Object)
	at java.lang.Thread.run(Thread.java:748)

"Service Thread" #8 daemon prio=9 os_prio=31 tid=0x00007fc2d001e800 nid=0x3a03 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread2" #7 daemon prio=9 os_prio=31 tid=0x00007fc2d001e000 nid=0x4703 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" #6 daemon prio=9 os_prio=31 tid=0x00007fc2d001d000 nid=0x4903 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #5 daemon prio=9 os_prio=31 tid=0x00007fc2cf022800 nid=0x4a03 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" #4 daemon prio=9 os_prio=31 tid=0x00007fc2d0019000 nid=0x360b runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=31 tid=0x00007fc2cf813000 nid=0x5103 in Object.wait() [0x0000700004d64000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x0000000795588ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
	- locked <0x0000000795588ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:212)

"Reference Handler" #2 daemon prio=10 os_prio=31 tid=0x00007fc2d0015000 nid=0x5203 in Object.wait() [0x0000700004c61000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x0000000795586bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:502)
	at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
	- locked <0x0000000795586bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"VM Thread" os_prio=31 tid=0x00007fc2d0010800 nid=0x2d03 runnable

"GC task thread#0 (ParallelGC)" os_prio=31 tid=0x00007fc2d000d800 nid=0x2307 runnable

"GC task thread#1 (ParallelGC)" os_prio=31 tid=0x00007fc2d000e000 nid=0x2203 runnable

"GC task thread#2 (ParallelGC)" os_prio=31 tid=0x00007fc2cf003000 nid=0x5403 runnable

"GC task thread#3 (ParallelGC)" os_prio=31 tid=0x00007fc2cf804800 nid=0x2b03 runnable

"VM Periodic Task Thread" os_prio=31 tid=0x00007fc2cf886800 nid=0x4503 waiting on condition

JNI global references: 5


Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x00007fc2cf856ab8 (object 0x00000007955f0f68, a java.lang.Object),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x00007fc2cf859558 (object 0x00000007955f0f78, a java.lang.Object),
  which is held by "Thread-1"

Java stack information for the threads listed above:
===================================================
"Thread-1":
	at LockDemo$2.run(LockDemo.java:34)
	- waiting to lock <0x00000007955f0f68> (a java.lang.Object)
	- locked <0x00000007955f0f78> (a java.lang.Object)
	at java.lang.Thread.run(Thread.java:748)
"Thread-0":
	at LockDemo$1.run(LockDemo.java:18)
	- waiting to lock <0x00000007955f0f78> (a java.lang.Object)
	- locked <0x00000007955f0f68> (a java.lang.Object)
	at java.lang.Thread.run(Thread.java:748)

Found 1 deadlock.

zhenglubiaodeMacBook-Pro:~ zlb$
```

- 线程池死锁
- 扩大线程池线程数 or 任务结果之间不再互相依赖。
```

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Yuanming Tao
 * Created on 2019/9/12
 * Description
 */
public class threadPool {

    static final ExecutorService executorService =
            Executors.newSingleThreadExecutor();


    public static void main(String[] args) {
        Future<Long> f1 = executorService.submit(new Callable<Long>() {

            public Long call() throws Exception {
                System.out.println("start f1");
                Thread.sleep(1000);//延时
                Future<Long> f2 =
                        executorService.submit(new Callable<Long>() {

                            public Long call() throws Exception {
                                System.out.println("start f2");
                                return -1L;
                            }
                        });
                System.out.println("result" + f2.get());
                System.out.println("end f1");
                return -1L;
            }
        });
    }


}

```