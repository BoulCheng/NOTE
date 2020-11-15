


#### concept

- Message Model
    - Topic的消息可以分片存储于不同的Broker
    - Message Queue 用于存储消息的物理地址，每个Topic中的消息地址存储于多个 Message Queue 中 ?
- Producer
    - 同步发送、异步发送、顺序发送、单向发送
    
- Consumer
    - 拉取式消费、推动式消费
- Topic
    - 表示一类消息的集合, 每条消息只能属于一个主题，是RocketMQ进行消息订阅的基本单位
- Broker Server
    - 消息中转角色，负责存储消息、转发消息
    - 也存储消息相关的元数据，包括消费者组、消费进度偏移和主题和队列消息等
- Name Server
    - 生产者或消费者能够通过名字服务查找 各主题相应的Broker IP列表
    - 多个Namesrv实例组成集群，但相互独立，没有信息交换。
    
- Producer Group
    - 如果发送的是事务消息且原始生产者在发送之后崩溃，则Broker服务器会联系同一生产者组的其他生产者实例以提交或回溯消费
- Consumer Group
    - 消费者组的消费者实例必须订阅完全相同的Topic
    - 费者组使得在消息消费方面，实现负载均衡和容错的目标变得非常容易
    
    - 集群消费
        - 相同Consumer Group的每个Consumer实例平均分摊消息
    - 广播消费
        - 相同Consumer Group的每个Consumer实例都接收全量的消息
- 普通顺序消息
    - 消费者通过同一个消费队列收到的消息是有顺序的
- 严格顺序消息
    - 消费者收到的所有消息均是有顺序的
    
- Message
    - 每个消息拥有唯一的Message ID
    - 可以携带具有业务标识的Key
    - 系统提供了通过Message ID和Key查询消息的功能
    
- Tag
    - 为消息设置的标志，用于同一主题下区分不同类型的消息
    - 消费者可以根据Tag实现对不同子主题的不同消费逻辑，实现更好的扩展性。
    
    
#### features
- 消息的订阅是指某个消费者关注了某个topic中带有某些tag的消息，进而从该topic消费数据。
- 消息顺序
    - 全局顺序是指某个Topic下的所有消息都要保证顺序
    - 分区顺序 对于指定的一个 Topic，所有消息根据 sharding key 进行区块分区。 同一个分区内的消息按照严格的 FIFO 顺序进行发布和消费
        - Sharding key 是顺序消息中用来区分不同分区的关键字段，和普通消息的 Key 是完全不同的概念
- 消息过滤
    - RocketMQ的消费者可以根据Tag进行消息过滤，也支持自定义属性过滤
    - 消息过滤目前是在Broker端实现的，优点是减少了对于Consumer无用消息的网络传输，缺点是增加了Broker的负担、而且实现相对复杂
- 消息可靠性
    - 刷盘方式是同步还是异步
    - 同步双写势必会影响性能，适合对消息可靠性要求极高的场合 (单点故障)
    - 3.0版本开始支持同步双写
    - 
- 消息语义-至少一次
    - Consumer先Pull消息到本地，消费完成后，才向服务器返回ack，如果没有消费一定不会ack消息，所以RocketMQ可以很好的支持此特性
- 回溯消费
    - 重新消费
    - Broker在向Consumer投递成功消息后，消息仍然需要保留
    - RocketMQ支持按照时间回溯消费，时间维度精确到毫秒
    
- 事务消息
    - 应用本地事务和发送消息操作可以被定义到全局事务中，要么同时成功，要么同时失败
    - 提供类似 X/Open XA 的分布事务功能，通过事务消息能达到分布式事务的最终一致
- 定时消息
    - 定时消息（延迟队列）是指消息发送到broker后，不会立即被消费，等待特定时间投递给真正的topic
    - messageDelayLevel messageDelayLevel是broker的属性，不属于某个topic
    - 发消息时，设置delayLevel等级即可：msg.setDelayLevel(level)
    - 并根据delayTimeLevel存入特定的queue，queueId = delayTimeLevel – 1，即一个queue只存相同延迟的消息，保证具有相同发送延迟的消息能够顺序消费
    - 需要注意的是，定时消息会在第一次写入和调度写入真实topic时都会计数，因此发送数量、tps都会变高
- 消息重试-重新消费
    - RocketMQ会为每个消费组都设置一个重试队列 针对消费组，而不是针对每个Topic设置的
    - 暂时保存因为各种异常而导致Consumer端无法消费的消息
    - 会为重试队列设置多个重试级别，每个重试级别都有与之对应的重新投递延时，重试次数越多投递延时就越大
- 消息重投
    - 同步消息失败会重投，异步消息有重试，oneway没有任何保证
    - 可能会造成消息重复，消息重复在RocketMQ中是无法避免的问题(kafka，broker 给每个 producer 都分配了一个 ID ，并且 producer 给每条被发送的消息分配了一个序列号来避免产生重复的消息)
    - 另外，生产者主动重发、consumer负载变化也会导致重复消息
    - 消息重试策略
        - retryTimesWhenSendFailed 不会选择上次失败的broker，尝试向其他broker发送，最大程度保证消息不丢。超过重投次数，抛出异常，由客户端保证消息不丢。当出现RemotingException、MQClientException和部分MQBrokerException时会重投。
        - retryTimesWhenSendAsyncFailed 异步重试不会选择其他broker，仅在同一个broker上做重试，不保证消息不丢。
        - retryAnotherBrokerWhenNotStoreOK 消息刷盘（主或备）超时或slave不可用（返回状态非SEND_OK），是否尝试发送到其他broker，默认false。十分重要消息可以开启。
- 流量控制
    - 生产者流控 ？
        - 生产者流控，不会尝试消息重投。
    - 消费者流控 ？
        - 消费者流控的结果是降低拉取频率。
-  死信队列
    - 当一条消息初次消费失败，消息队列会自动进行消息重试；达到最大重试次数后，若消费依然失败，则表明消费者在正常情况下无法正确地消费该消息，此时，消息队列 不会立刻将消息丢弃，而是将其发送到该消费者对应的特殊队列中   
    - 可以通过使用console控制台对死信队列中的消息进行重发来使得消费者实例再次进行消费
    
#### architecture
- Producer 
    - 分布式集群方式部署
    - 通过MQ的负载均衡模块选择相应的Broker集群队列进行消息投递
    - 投递的过程支持快速失败并且低延迟。
    - Producer与NameServer集群中的其中一个节点（随机选择）建立长连接，定期从NameServer获取Topic路由信息，并向提供Topic 服务的Master建立长连接，且定时向Master发送心跳。Producer完全无状态，可集群部署
    
- Consumer
    - 分布式集群方式部署
    - push推，pull拉两种模式
    - 集群方式和广播方式的消费
    - 实时消息订阅机制
    - Consumer与NameServer集群中的其中一个节点（随机选择）建立长连接，定期从NameServer获取Topic路由信息，并向提供Topic服务的Master、Slave建立长连接，且定时向Master、Slave发送心跳
    
- NameServer
    - 一个非常简单的Topic路由注册中心 其角色类似Dubbo中的zookeeper
    - 支持Broker的动态注册与发现
    - 接受Broker集群的注册信息并且保存下来作为路由信息的基本数据。然后提供心跳检测机制，检查Broker是否还存活
    - 每个NameServer将保存关于Broker集群的整个路由信息和用于客户端查询的队列信息。然后Producer和Conumser通过NameServer就可以知道整个Broker集群的路由信息，从而进行消息的投递和消费
    - NameServer通常也是集群的方式部署，各实例间相互不进行信息通讯
    - NameServer是一个几乎无状态节点，可集群部署，节点之间无任何信息同步。
      
- BrokerServer
    - Broker主要负责消息的存储、投递和查询以及服务高可用保证
        - HA, Master与Slave 的对应关系通过指定相同的BrokerName，不同的BrokerId 来定义，BrokerId为0表示Master，非0表示Slave
        - Master也可以部署多个
        - 每个Broker与NameServer集群中的所有节点建立长连接，定时注册Topic信息到所有NameServer
        - 注意：当前RocketMQ版本在部署架构上支持一Master多Slave，但只有BrokerId=1的从服务器才会参与消息的读负载。

- 工作流程
    - Producer发送消息，启动时先跟NameServer集群中的其中一台建立长连接，并从NameServer中获取当前发送的Topic存在哪些Broker上，轮询从队列列表中选择一个队列，然后与队列所在的Broker建立长连接从而向Broker发消息
    