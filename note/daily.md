

-9.11
- 唯一索引列 允许多行null值
- eq_ref   all parts of an index and a PRIMARY KEY or UNIQUE NOT NULL index.
- ref a leftmost prefix of the key(index)  or if the key is not a PRIMARY KEY or UNIQUE index  ((in other words, if the join cannot select a single row based on the key value).)
- EXPLAIN Extra type
- exists not exists



- 9.12
- Last_query_cost  show status like 'Last_query_cost';


- 9.17
    - mybatis

- 9.18
    - 日志
    - druid
        - StatFilter，用于统计监控信息
            - 慢SQL记录
            - 合并多个DruidDataSource的监控数据
        - wallfilter
            - 配置防御SQL注入攻击
        - LogFilter
            - 日志配置
            - loggerName
                - 日志级别
                - 日志输出
        - 各种连接池的对比
            - PSCache LRU ExceptionSorter
        - 连接池配置
            - druid只保留了maxActive和minIdle，分别相当于maxPoolSize和minPoolSize。
        - 检测连接是否有效
        - druid-spring-boot-starter
- 9.23
    - idea UML
    - 序列化
    - mybatis 一级二级缓存

- 9.25
    - 缓存比较
        - redis

        - ehcache 一级缓存(本地缓存)

        - memcached
- 9.27
    - dfs bfs
    - jvm内存相关参数
    - redolog binlog undolog

- 9.28
    - mybatis xml映射文件和映射器接口解析
    - mybatis原生使用方式

- 9.29
    - mybatis
        - mybatis 调试
        - mybatis-spring 概要
        - mybatis 插件
    - canal
        - 序列化协议
            - protobuf
        - 各种序列化协议的对比
            - jute
                - zookeeeper是如何处理
                    - rcc.jj --javacc--> Rcc.java --java执行解析zookeeper.jute文件--> 各种序列化和反序列的DTO

- 10.9
    - mybatis-spring 内部原理
    - transaction 注解
- 10.10
    - mybatis连接池

- 10.13
    - mysql 查询 锁的问题
- 10.14
    - TreeMap
    - druid
- 10.15
    - 代理模型
    - MapperFactoryBean
    - mybatis代理
    - druid

- 10.16
    - MapperScannerConfigurer
    - SqlSessionTemplate vs DefaultSqlSession
    - TransactionSynchronizationManager
    - BATCH vs SIMPLE
    - Trascation注解

- 10.29
    - spring 注入方式
    - 事务 aop spring-aop vs aspectJ
    - 责任链
- 10.30 -
    - @transactional 失效
        - 自调用
        - public ？？
        - 调试代理mode选择 <- @EnableTransactionManagement && 自动配置
        - 多层方法调用回滚  <- 事务源码
        - 多个数据源回滚  <- 事务源码
        - aspectJ aop的启用 xml配置和注解方式 禁用自动配置导入配置文件
            - @ImportResource(locations={"classpath:application-context.xml"})
    - spring 注入方式
    - 责任链
    - kafka手动提交消息

- 11.1
    - maven
        - dependency type scope
        - dependencyManagement

    - spring
        - @Import ??
        - @SpringBootApplication ？？
- 11.2
    - spring
        - scope
    - redis
        - lock

- 11.4
    - 双亲委派
    - 隔离
        - tomcat中对每个Web应用都有自己专用的一个WebAppClassLoader类加载器用来隔绝不同应用之间的class文件
        - 同时加载不同版本的同名包
    - aop aspectJ
    - tx-aspectJ
    - tx 自调用 和 public方法问题(?)

- 11.5
    - spring -> mybatis -> datasource

- 11.6
    - 类加载器
        - 对于任意一个类，都需要由加载它的类加载器和这个类本身来一同确立其在Java虚拟机中的唯一性

- 11.9
    - alg sort
    - 类加载器
    - tomcat类加载器
- 11.10
    - tomcat类加载器
    - spring 每次查询是否都是 新建session

- 11.11
    - aop
    - tomcat类加载器源码
    - alg sort
    - @Autowired
- 11.12
    - mq
    - 策略模式
- 11.13
    - mq
- 11.14
    - mq


- 11.6
    - mq
- 11.17
    - mq

- 11.18
    - mq
    - 分布式事务
- 11.19
    - 分布式事务

- 11.20
    - 死锁
    - 分布式事务
    - 快排
    - epoll的多路复用特性

- 11.23
    - mysql锁
- 11.24
    - mysql锁
- 11.25
    - mysql事务
    - 泛型
- 11.26
    - 红黑树右旋转
    - 快排
    - mysql事务

- 11.28/11.29
    - mysql innodb 锁 事务
- 12.2
    - 排序算法
- 12.3
    - 快排
- 12.4
    - bitmap 布隆算法

- 12.5
    - 布隆算法
- 12.6
    - ip
    - contextLoaderListener
- 12.7>12.11
    - 一致性hash算法
    - 逆序对
    - 归并排序
    - mysql两阶段提交

- 12.12
    - 线段树
- 12.13
    - ip协议


- 12.15
    - 一致性hash算法实现 treemap
    - 二叉搜索树的本质
    - 异步注解循环依赖怎样的顺序才会报错
    - 异步注解循环依赖解决方法
        - lazy解决方案原理
            - 涉及到的类 策略模式实现
    - 异步注解@Async为什么会产生循环依赖问题；异步注解是不是一定会产生循环依赖问题；异步注解产生的循环依赖如何解决，解决的原理是什么；为什么spring要这么设计异步注解
- 12.16
    - 高并发
    - LB
        - nginx_zuul
- 12.17
    - nginx_docs_http_request_processing
    - nginx_负载均衡
- 12.18
    - zuul
    - 堆
    - 跨域
    - 延时队列

- 12.21
    - springboot 自动配置
- 12.22
    - springboot 自动配置
    - sjdbc
- 12.23
    -
- 12.24
    - s-jdbc
    - websocket
    - 实时

- 12.25
    - s-jdbc
- 12.26
    - plan总结
- 12.27
    - tcp
    - spring标签解析总结

- 12.27>31
    - 取数系统整理
- 1.4
    - 统一登陆
- 1.5
    - s-jdbc
    - snowflake leaf
- 1.6
    - snowflake时钟回拨问题
    - dfs bfs
    - s-jdbc
    - Leaf-segment
    - ApplicationRunner, CommandLineRunner
        - 选项参数 非选项参数
    - 系统环境变量 系统属性变量 args
- 1.7
    -

- 1.8
    - 游标
    - s-jdbc

- 1.9/10
    - tcp/ip
- 1.11
    - 游标、流式查询
    - Leaf-snowflake 源码
    - https CA

- 1.12
    - mybatis / s-jdbc 整体流程
    - 红锁

- 1.13
    - 状态机
        - 类的行为是基于它的状态改变的，状态模式中，创建表示各种状态的对象和一个行为随着状态对象改变而改变的 context 对象
    - 责任链
        - 一种处理请求的模式，将多个处理器组成一条链，然后让请求在链上传递
            - 找个满足条件handler处理请求
            - 链上每个handler都有机会处理并做一些工作，被称为拦截器（Interceptor）或者过滤器（Filter）
    - https
    - mysql join 原理
    - 归并 动态
    - Semaphore  LockSupport

- 1.14
    - 限流
    - 排序算法
    - threadLocal

- 1.15
    - springboot自动配置
    - TransactionAutoConfiguration 自动配置过程
    - 切面 vs 事务 vs 异步注解   AopConfigUtils#APC_PRIORITY_LIST
    - 切面和事务的 specificInterceptors 以及它们的顺序关系，以及在责任链下 事务对切面的影响
    - SuppressWarnings RetentionPolicy(Retention)  ElementType(Target)
        - 注解支持的元素数据类型: 所有基本类型（int,float,boolean,byte,double,char,long,short） String Class enum Annotation 上述类型的数组
    - 事务自动配置
    - AopAutoConfiguration TransactionAutoConfiguration

    - MethodInterceptor
    - org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor: advice org.springframework.transaction.interceptor.TransactionInterceptor@3a0d48d2
        - TransactionInterceptor
    - InstantiationModelAwarePointcutAdvisor: expression [InsertDBPointcut()]; advice method [public java.lang.Object com.zlb.spring.practice.aop.DBAspect.singleExecute(org.aspectj.lang.ProceedingJoinPoint) throws java.lang.Throwable]; perClauseKind=SINGLETON
        - org.springframework.aop.aspectj.AspectJAroundAdvice: advice method [public java.lang.Object com.zlb.spring.practice.aop.DBAspect.singleExecute(org.aspectj.lang.ProceedingJoinPoint) throws java.lang.Throwable]; aspect name 'DBAspect'
        - InterceptorAndDynamicMethodMatcher
- 1.16
    - 动态规划
- 1.17
    - mysql
    - 切面 vs 事务
- 1.18
    - 切面 vs 事务 advisor链 排序

- 1.19
    - 动态规划
- 1.20
    - dubbo 多种远程调用方式，例如dubbo RPC（二进制序列化 + tcp协议）、http invoker（二进制序列化 + http协议，至少在开源版本没发现对文本序列化的支持）、hessian（二进制序列化 + http协议）、WebServices （文本序列化 + http协议）等等，但缺乏对当今特别流行的REST风格远程调用（文本序列化 + http协议）的支持
    - dubbo 序列化协议 hessian vs kryo

- 2.1
    - TreeSet 遍历
    - jdk 排序

- 2.2
    - 折半插入排序  采用折半查找的方法来加快寻找插入点的速度
        - 对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入
        - 如果待插入的元素与有序序列中的某个元素相等，则将待插入元素插入到相等元素的后面
    - 归并排序

- 2.19-20
    - docker
    - springboot启动原理


- 2.22
    - 事务
        - aop在目标对象的生命周期的织入时机
        - spring aop的织入时机  事务public方法jdk代理由于接口原因只能代理public方法
        - 事务的传播
        - @Lazy ,  expose-proxy + AopContext.currentProxy()
    - H2 Mysql Tidb influxdb es
    - dubbo
        - 调用方式  Dubbo RPC
        - kryo 序列化 优于 hession2的原因
        - rpc框架原理
        - 支持的协议和注册中心
        - xml配置
            - 重试
            - 超时
        - 架构 、 软件架构发胀
            - 自动动态部署
        -
- 2.23
    - dubbo
        - 配置
            - xml配置
                - 配置覆盖关系
            - 配置来源覆盖
        - 集群容错
            - failover 重试
            - 容错模式
            - 负载均衡策略 ？
        - 线程模型和线程池策略
            - netty 断开链接？
        - 结果缓存
        - 上下文信息
            - RpcContext
        - 异步执行
        - 异步调用
        - 参数回调 设计模式
        - sent ?
        - 本地存根
        - 本地伪装 通常用于服务降级
        - 服务调用过程
        - 并发控制
        - 消费端线程池模型
        - Provider 端性能配置 threads executes actives retries timeout
            - connections 默认0代表1个
                - dubbo协议
                    - connections 表示该服务消费方服务对每个服务提供者建立的长连接数
            - accepts 默认不限
                - 服务提供方最大可接受连接数
            - executes 默认不限 (方法级别的舱壁模式)
                - 服务提供方服务每个方法 并发执行数（或占用线程池线程数）
                - ExecuteLimitFilter 责任链模式
            - actives 默认不限 (方法级别的舱壁模式)
                - 每个服务消费方客户端每个服务方法的 并发执行数（或占用连接的请求数）
                - ActiveLimitFilter 责任链模式
            - threads 默认200
                - 服务线程池大小
        - spi  与jdk-spi的区别 依赖注入以及具体实现类的自适应

- 2.24
    - spring 自定义标签解析
    - dubbo核心实现细节
    - spiNettyServer
    - 服务路由 2 3 5 10
    - 服务引用
    - 服务导出
    - dubbo与spring的整合
    
- 2.25
    - 调用过程
    - 协议  dubbo协议
- 2.27
    - 调用过程
    - 调优参数
        - 方法级别的舱壁模式
    - 与spring-cloud的对比
- 2.28
    - 集群、负载均衡[https://github.com/BoulCheng/spring-framework/tree/v5.2.5.RELEASE-comment]
- 3.5
    - rocketmq 消息写入  mmap  pageCache

- 3.6-7
    - netty io
-
    - canal 图片在源码
- 3.8-9
    - rocketmq

- 3.11
    - rocketmq
    - rocketmq 事务消息

- 3.12
    - 时序图的画法
    - netty的各种ChannelHandler

- 3.13
    - CompletableFuture
    - 函数式编程、lambda

- 3.13
    - rocketmq 刷盘 数据同步

- 3.18
    - io

- 4.14
    - rocketmq

- 4.14
    - springboot jar
    - docker
- 324
    - netty  io 二叉树 broker消息消费
- 325
    - ThreadLocal 哈希表 hashmap
- 326
    - hashmap 位运算
    - rocketmq starter mybatis
- 327
    - mybatis-spring
    - mybatis-spring 自动配置


- 紧急的
-

- 328 327
    - mysql日志文件 位图 布隆过滤器
- 329
    - rocketmq 死信队列 tcp
    - 大数据生态 dataworks
- 330
    - 流式查询
    - k8s
    - 粘包
    - mysql自增主键
    - 树
    - innodb B+
    - 哈希索引

- https://draveness.me/tags/%E4%B8%BA%E4%BB%80%E4%B9%88%E8%BF%99%E4%B9%88%E8%AE%BE%E8%AE%A1


- https://help.aliyun.com/document_detail/110778.htm?spm=a2c4g.11186623.2.13.733e5b5965cQip#concept-l1l-s5x-bhb
- mysql两阶段提交
- mysql读取数据 怎么读 最小单位页 按文件系统的最小单位4k读 ？
- Mysql Join算法原理


- kafka VS rocketmq


- linux 系统性能命令
- OS

- 41
    - 列式存储
    - OS
    - redis单线程

- 42
    - 虚拟内存
    - mmap
    - 直接内存
    - redis rdb
- https://zhuanlan.zhihu.com/p/38348646
- [https://docs.oracle.com/javase/tutorial/tutorialLearningPaths.html]
- java.md




==================================================================================================top==================================================================================================

延时队列 和 事务消息的实现机制

https
分布式锁 事务

穿透 击穿 雪崩

io

ConcurrentSkipListMap

集合数据结构
线程池
nio
内存模型 类加载机制
sql加锁 事务
分布式锁 事务 id capbase的应用
高并发 高可用 高性能系统设计
dubbo
rocketmq
netty 池化 粘包
https tcp ip http
事务与切面同时使用
spring整合

=======================================================================================================================================================================================================
- spring事务

- spring设计模式

- mybatis 流式查询 游标查询

- 49
    - mybatis
    - 设计模式

- Spring常见面试题总结（超详细回答）
- 提交源码注释
- 修改github仓库可见性
- 拍照
- pdf转word




- 简历技术核心熟练
- 架构师视角拔高总结 从厚到薄
- 面试题
- 算法、数据结构
- TCC优化
- 一致性算法
- 如何设计 有中大型分布式、高并发、高可用性系统设  及应用性能优化
- 事务spring传播
- 分片算法




Thread的线程方法与状态转换
JMM、指令重排、happens-before原则、原子性、可见性与有序性
ForkJoin并发框架
- jvm
- java锁
- 分布式事务
- 了解docker、k8s、大数据生态
- linux java命令
- dubbo
- rocketmq kafka
    - 消息 exactly one
- 网络
- jvm note
- netty zk
    - netty池化技术
- redis
    - rdb aof 集群
- mysql
    - 事务
    - 一条sql

- 自调用失效
- 算法
- netty Ratio
- Sharding-JDBC 分库分表
- kafka

红黑树
cas优化
一致性算法
分片算法
mysql锁
concurrenthashmap 如何保证并发安全
序列化框架性能不同的原理 Protobuf

dubbo调用和zookeeper更新
zab raft算法
分片算法
mysql事务、锁、mysql一条更新sql、加不加事务的区别
concurrent hashmap如何保证put初始value安全
netty如何处理大量连接的心跳
序列化protobuf的比较及序列化的过程 哪些地方可能会产生性能问题




=======================================================================================================================================================================================================

1、深入理解各种开源框架原理和机制
2、有阅读开源框架源码的习惯，对技术有强烈的钻研精神，分布式调度框架ElasticJob的Contributor
3、有作为技术骨干掌握核心系统设计开发的经验
4、工作严谨、责任心强

1、JAVA基础扎实，熟练掌握并发包、集合框架及其数据结构、线程池、nio，精通多线程编程；
2、对JVM有一定的理解，包括内存模型、JVM内存结构、类加载机制以及性能优化；
3、熟练掌握关系型数据库MySQL，精通事务型存储引擎InnoDB，对索引优化、sql加锁机制、事务有深入理解；
4、深入理解各种开源框架的原理和机制，包括Netty、RocketMQ、Dubbo、Spring、MyBatis
5、掌握分布式系统的设计，包括CAP定理、BASE理论、分布式事务、分布式锁、分布式缓存、MQ、RPC、网络通信，能合理应用分布式常用技术解决问题
6、掌握多线程编码及性能优化，有高并发、高性能、高可用系统设计和开发经验；
7、掌握各种设计模式，能进行可扩展性、可维护性的软件设计，有代码重构的能力，注重系统代码质量
8、有阅读开源框架源码的习惯，对技术有强烈的钻研精神，有独立、主动学习技术的能力
9、工作严谨、责任心强，并具备良好的沟通协作能力

1. 在以下方面有深入理解：
并发技术，Lock、CAS、线程池、Java内存模型、线程安全
集合框架及其底层的数据结构
MySQL，InnoDB存储引擎，索引、锁、事务
RPC，Dubbo服务调用过程、集群容错、负载均衡算法、序列化方式、协议设计
MQ，RocketMQ消息存储、发送、消费、事务消息，通信机制多线程模型
网络通信框架Netty，NIO优化、线程模型、心跳机制、ioRatio、池化技术、粘包问题
分布式，分布式事务、分布式锁、分布式ID
Redis，Redis数据结构、集群原理，缓存穿透、击穿、雪崩
网络，OSI参考模型，IP、TCP、HTTP、HTTPS协议
IO，IO多路复用、Zero-copy、mmap、Page cache的应用，包括在Netty、RocketMQ、Redis、MySQL中的应用
ORM，MyBatis与Spring整合的原理、Spring Boot自动配置原理
Spring，AOP、IOC、事务，循环引用问题，事务与切面同时使用
设计模式，各种设计模式在Spring、MyBatis中的实践应用
数据结构，散列表、二叉查找树、红黑树、跳表、堆

2. 开源框架源码
2.1. 阅读过，熟悉Spring、Dubbo、RocketMQ、MyBatis、ElasticJob、Netty、JDK一些源码
2.2. 分布式调度框架ElasticJob的Contributor，修改过其源码应用于生产
3.3. 阅读开源框架源码一些个人笔记(Markdown链接):
[RocketMQ](https://github.com/BoulCheng/rocketmq/commits/release-4.7.1-comment)
[Dubbo](https://github.com/BoulCheng/dubbo-2.6.4)
[Spring](https://github.com/BoulCheng/spring-framework/tree/v5.2.5.RELEASE-comment)
[MyBatis](https://github.com/BoulCheng/mybatis-3/tree/mybatis-3.5.5-comment)
[ElasticJob](https://github.com/BoulCheng/elastic-job-2.1.5/tree/sourceCodeAnalysis)
[Netty](https://github.com/BoulCheng/netty-4.1.36.Final/tree/develop)
[JDK](https://github.com/BoulCheng/