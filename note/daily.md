

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