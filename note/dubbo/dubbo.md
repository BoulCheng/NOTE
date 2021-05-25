


- netty handler过滤器 编解码 粘包拆包

- 与spring-cloud的对比 服务治理
    - dubbo使用长连接小数据量的模式提供服务使用的，适用同步调用场景多； 后台业务逻辑复杂、时间长而导致异步逻辑比较多的话，可能Dubbo并不合适
    - http协议的通信对于应用的负载量会否真正成为瓶颈点（Spring Cloud也并不是和http+JSON强制绑定的，如有必要Thrift、protobuf等高效的RPC、序列化协议同样可以作为替代方案）
    - 鉴于服务发现对服务化架构的重要性，再补充一点：Dubbo 实践通常以ZooKeeper 为注册中心（Dubbo 原生支持的Redis 方案需要服务器时间同步，且性能消耗过大）。针对分布式领域著名的CAP理论（C——数据一致性，A——服务可用性，P——服务对网络分区故障的容错性），Zookeeper 保证的是CP ，但对于服务发现而言，可用性比数据一致性更加重要 ，而 Eureka 设计则遵循AP原则
    - 其实相比于Dubbo，Spring Cloud可以说是一个更完备的微服务解决方案，它从功能性上是Dubbo的一个超集
- 与spring的结合

- 消费端线程池-cached线程池  提供方-fixed线程池 

- 服务降级

- 服务引用
    - 通过注册中心引用远程服务，最后都会得到一个 Invoker 实例。如果有多个注册中心，多个服务提供者，这个时候会得到一组 Invoker 实例，此时需要通过集群管理类 Cluster 将多个 Invoker 合并成一个实例
        - @see RegistryProtocol#refer(Class, URL)
            - 涉及注册中心如zookeeper
            - 注册服务消费者，在 consumers 目录下新节点
            - 订阅 providers、configurators、routers 等节点数据
        - @see DubboProtocol#refer(Class, URL)
            - 涉及网络通信远程调用如netty
            - 与netty服务器端建立好连接 因此具备了远程调用能力    
        
    - 合并后的 Invoker 实例已经具备调用本地或远程服务的能力了，但并不能将此实例暴露给用户使用，这会对用户业务代码造成侵入。此时框架还需要通过代理工厂类 (ProxyFactory) 为服务接口生成代理类，并让代理类去调用 Invoker 逻辑。避免了 Dubbo 框架代码对业务代码的侵入，同时也让框架更容易使用

- 服务导出
    - {@link RegistryProtocol#export(Invoker)}
    - {@link DubboProtocol#export(Invoker)}

- DubboInvoker
    - 一个DubboInvoker 对应一个服务提供方(不同注册中心不同提供方对应不同DubboInvoker) 具有远程调用能力,  {@link DubboInvoker#clients} 代表与该服务提供方连接的netty客户端

- Dubbo 集群容错
    - 服务目录 Directory
        - 它可以看做是 Invoker 集合，且这个集合中的元素会随注册中心的变化而进行动态调整
        - 实际上服务目录在获取注册中心的服务配置信息后，会为每条配置信息生成一个 Invoker 对象，并把这个 Invoker 对象存储起来，这个 Invoker 才是服务目录最终持有的对象。Invoker 有什么用呢？看名字就知道了，这是一个具有远程调用功能的对象。
    - 服务路由 Router
        - 路由规则在发起一次RPC调用前起到过滤目标服务器地址的作用，过滤后的地址列表，将作为消费端最终发起RPC调用的备选地址
        - 条件路由 
            - [服务消费者匹配条件] => [服务提供者匹配条件]
            - 读写分离 排除预发布机 白名单 黑名单  
        - 标签路由规则
            - 标签路由通过将某一个或多个服务的提供者划分到同一个分组，约束流量只在指定分组中流转，从而实现流量隔离的目的
            - 可以作为蓝绿发布、灰度发布等场景的能力基础       
    - 集群 Cluster
        - 服务消费者需要决定选择哪个服务提供者进行调用。另外服务调用失败时的处理措施也是需要考虑的，是重试呢，还是抛出异常，亦或是只打印异常等
    - 负载均衡 LoadBalance 
 
 
- 拦截服务
    - ProtocolFilterWrapper
    - ProtocolListenerWrapper
     
- 设计模式
    - 提供方导出时双重代理的设计意义
        - 代理模式 重心是为了借用对象的功能完成某一流程，而非对象功能如何
        - Invoker的统一调用方法  到 Wrapper的统一调用方法 到 服务实现类的具体各种方法
    - 服务引用invoker  集群的模式 ？ 
    - 消费方提供方 invoker生成时的责任链模式 多种Protocol的调用 ProtocolFilterWrapper  RegistryProtocol  DubboProtocol
    - 并发控制的责任链模式 ProtocolFilterWrapper中生成的Invoker链
    - 责任链 装饰者模式的区别
    
    
- Provider 端性能配置 threads executes actives retries timeout
            - connections 默认0代表1个
                - dubbo协议
                    - connections 表示该服务消费方服务对每个服务提供者建立的长连接数
            - accepts 默认不限
                - 服务提供方最大可接受连接数
            - executes 默认不限 (有点方法级别的舱壁模式的味道)
                - 服务提供方服务每个方法 并发执行数（或占用线程池线程数）
                - ExecuteLimitFilter 责任链模式
            - actives 默认不限 (有点方法级别的舱壁模式的味道)
                - 每个服务消费方客户端每个服务方法的 并发执行数（或占用连接的请求数）
                - ActiveLimitFilter 责任链模式
            - threads 默认200
                - 服务线程池大小