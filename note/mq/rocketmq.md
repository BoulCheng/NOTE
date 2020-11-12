


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
- 