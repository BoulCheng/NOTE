

- 降低资源竞争思想
    - LongAdder ConcurrentHashMap
    
- 异步方法注解

- CLH 、MCS


- 读多写少的场景用乐观锁  读过多导致写无法执行


- mapreduce 

- synchronized 与 LockSupport 的区别 ； semaphore和mutex的区别

- 
    - CPU上线文切换，切换寄存器、程序计数器

    - 进程上线文切换，切换虚拟内存、用户栈

    - 线程上下文切换，2种情况：（1）线程私有数据（比如线程栈、程序计数器等）；（2）、（1）+ 线程资源 ；

    - 系统调用：需要进行线程上下文切换，但不是进程上下文切换