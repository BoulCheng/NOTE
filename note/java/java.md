

### 动态代理
- 动态生成接口对应的代理类的class文件(字节流保存)，并生成Class对象
- 每次调用Proxy.newProxyInstance其实是根据调用时传入的InvocationHandler类型的实例对象作为构造函数的参数实例化一个该代理类的对象


- sleep wait park 的区别


- cglib vs jdk

#### jdk
- 最大堆


### ms

- JDK


- 



阅读过，熟悉Spring、Dubbo、RocketMQ、MyBatis、ElasticJob、JDK源码，对其原理有一定的理解；
有中大型分布式、高并发、高可用性系统设计开发经验；
掌握常用数据结构、常用设计模式、网络基础知识；
分布式调度框架ElasticJob的Contributor，修改过其源码应用于生产。


熟练掌握并发包、集合、多线程、类加载，对JVM有一定的掌握
精通MySQL、Redis、Dubbo、RocketMQ
阅读过，熟悉Spring、Dubbo、RocketMQ、MyBatis、ElasticJob、JDK源码，对其原理有一定的理解
掌握Netty、Zookeeper，熟悉Netty一些源码
分布式调度框架ElasticJob的Contributor，修改过其源码应用于生产
熟练掌握常用数据结构，如散列表、跳表、二叉查找树、红黑树、堆等
掌握常用设计模式
掌握网络基础知识
有中大型分布式、高并发、高可用交易系统设计开发经验
有一定的应用性能优化经验
了解docker、k8s、大数据生态
有对技术的热情与钻研精神
有阅读英文文档的习惯

       
阅读框架源码的一些笔记(Markdown链接):
[Spring](https://github.com/BoulCheng/spring-framework/tree/v5.2.5.RELEASE-comment)
[RocketMQ](https://github.com/BoulCheng/rocketmq/commits/release-4.7.1-comment)
[Dubbo](https://github.com/BoulCheng/dubbo-2.6.4)
[MyBatis](https://github.com/BoulCheng/mybatis-3/tree/mybatis-3.5.5-comment)
[ElasticJob](https://github.com/BoulCheng/elastic-job-2.1.5/tree/sourceCodeAnalysis)
[Netty](https://github.com/BoulCheng/netty-4.1.36.Final/tree/develop)
[Zookeeper](https://github.com/BoulCheng/zookeeper/tree/release-3.6.1-explore)
[JDK](https://github.com/BoulCheng/JDK8/commits/master)







1、
在以下方面有深入理解
    并发技术，Lock、CAS、各类线程池的实现原理、Java内存模型
    集合框架及其底层的数据结构
    MySQL，InnoDB存储引擎，索引、锁、事务
    RPC，Dubbo服务调用过程、集群容错、负载均衡算法、序列化方式、协议设计
    MQ，RocketMQ消息存储、发送、消费、事务消息，通信机制多线程模型
    网络通信框架Netty，NIO优化、线程模型、心跳机制、ioRatio、池化技术、粘包问题
    分布式，分布式事务、分布式ID、分布式锁
    Redis，redis数据结构、集群原理，缓存穿透、击穿、雪崩
    网络，OSI参考模型，IP、TCP、HTTP、HTTPS协议
    IO，IO多路复用、Zero-copy、mmap、Page cache的应用，包括在Netty、RocketMQ、Redis、InnoDB中的应用
    ORM，MyBatis与Spring整合的原理(包括事务)、Spring Boot自动配置原理
    Spring，AOP、IOC、事务源码级理解，循环引用问题，事务与切面同时使用 
    设计模式，各种设计模式在Spring、MyBatis中的实践应用
    
2、    
阅读过，熟悉Spring、Dubbo、RocketMQ、MyBatis、ElasticJob、Netty、JDK一些源码
分布式调度框架ElasticJob的Contributor，修改过其源码应用于生产
阅读框架源码的个人笔记(Markdown链接):
[RocketMQ](https://github.com/BoulCheng/rocketmq/commits/release-4.7.1-comment)
[Dubbo](https://github.com/BoulCheng/dubbo-2.6.4)
[Spring](https://github.com/BoulCheng/spring-framework/tree/v5.2.5.RELEASE-comment)
[MyBatis](https://github.com/BoulCheng/mybatis-3/tree/mybatis-3.5.5-comment)
[ElasticJob](https://github.com/BoulCheng/elastic-job-2.1.5/tree/sourceCodeAnalysis)
[Netty](https://github.com/BoulCheng/netty-4.1.36.Final/tree/develop)
[JDK]()







1. 数据来源：
1.1 T+1数据来源： 通过DataWorks数据集成的数据同步任务进行离线数据同步，定时数据同步任务每天凌晨将业务库数据同步到DataWorks的MaxCompute对应的表里，MaxCompute提供海量数据存储和计算服务，然后通过MaxCompute提供的java sdk查询计算数据；
1.2 实时数据来源： 业务库 -> canal -> 通道库 -> canal -> mq(kafka) -> flink(java写udf函数 用于流式计算 比如解析kafka消息) -> 到DataWorks的MaxCompute对应的表。

2. 取数方式有自助取数、SQL取数、人工取数：自助取数是取数人根据取数模型自定义取数规则，然后自动解析生成SQL查询，采用流式查询避免把全量数据查询放到本地内存进而可能发生OOM的风险，同时采用异步的方式，在数据文件生成好通知取数人；SQL取数是根据取数人自己写的sql直接查询数据库，可支持多表关联查询；人工取数则是由数据分析师人工处理。另外分析模块从各个纬度对取数行为进行统计分析。

3. 后期重构取数系统：依赖数仓管理平台，在数仓的ADS层构建。数仓管理平台系统主要服务于数据部门的数仓重构建设，有术语、数据域、业务过程、指标、模型等模块，其中模型与数仓中表一一对应，模型中字段与指标关联，重构后的取数只需选择指标即可取出所需的数据。





=====AAAAAAAAAAA=======



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
    IO，IO多路复用、Zero-copy、mmap、Page cache的应用，包括Netty、RocketMQ、Redis、MySQL
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
[JDK](https://github.com/BoulCheng/JDK8/commits/master)

==== 

深入理解各种开源框架的原理和机制
有阅读开源框架源码的习惯，对技术有强烈的钻研精神，分布式调度框架ElasticJob的Contributor
具备作为技术骨干掌握核心系统设计开发的经验
工作严谨、责任心强


JAVA基础扎实，熟练掌握并发包、集合框架及其数据结构、线程池、nio，精通多线程编程；
对JVM有一定的理解，包括内存模型、JVM内存结构、类加载机制以及性能优化；
熟练掌握关系型数据库MySQL，精通事务型存储引擎InnoDB，对索引优化、sql加锁机制、事务有深入理解；
深入理解各种开源框架的原理和机制，包括Netty、RocketMQ、Dubbo、Spring、MyBatis
掌握分布式系统的设计，包括CAP定理、BASE理论、分布式事务、分布式锁、缓存、MQ、RPC、网络通信，能合理应用分布式常用技术解决问题
掌握多线程编码及性能优化，有高并发、高性能、高可用系统设计和开发经验；
掌握各种设计模式，能进行可扩展性、可维护性的软件设计，有代码重构的能力，注重系统代码质量
有阅读开源框架源码的习惯，对技术有强烈的钻研精神，有独立、主动学习技术的能力
工作严谨、责任心强，并具备良好的沟通协作能力