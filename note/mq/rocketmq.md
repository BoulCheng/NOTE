


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
        - MessageId的长度总共有16字节，其中包含了消息存储主机地址（IP地址和端口），消息Commit Log offset
    - 可以携带具有业务标识的Key
        - 如果消息的properties中设置了UNIQ_KEY这个属性，就用 topic + “#” + UNIQ_KEY的value作为 key 来做写入操作。如果消息设置了KEYS属性（多个KEY以空格分隔），也会用 topic + “#” + KEY 来做索引
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
    - 消息发送的高可用 * 
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
    - Producer发送消息，启动时先跟NameServer集群中的其中一台建立长连接，并从NameServer中获取当前发送的Topic存在哪些Broker上，轮询从队列列表中选择一个队列，然后与队列所在的Broker建立长连接从而向Broker发消息 *
    
### design
#### 消息存储
- 消息存储整体架构
    - CommitLog
        - 日志数据文件 消息主体以及元数据的存储文件
        - 文件名长度为20位，左边补零，剩余为起始偏移量
        - 消息主要是顺序写入日志文件，当文件满了，写入下一个文件
    - ConsumeQueue(消息消费队列)
        - consumequeue文件可以看成是基于topic的commitlog索引文件，故consumequeue文件夹的组织方式如下：topic/queue/file三层组织结构(一个队列由多个队列文件构成)
        - 逻辑消费队列，消息消费队列，引入的目的主要是提高消息消费的性能
        - ConsumeQueue（逻辑消费队列）作为消费消息的索引，保存了指定Topic下的队列消息在CommitLog中的起始物理偏移量offset，消息大小size和消息Tag的HashCode值
        - 同样consumequeue文件采取定长设计，每一个条目共20个字节，分别为8字节的commitlog物理偏移量、4字节的消息长度、8字节tag hashcode，单个文件由30W个条目组成，可以像数组一样随机访问每一个条目，每个ConsumeQueue文件大小约5.72M
        - 由于RocketMQ是基于主题topic的订阅模式，Consumer即可根据ConsumeQueue来查找待消费的消息
    - IndexFile
        - （索引文件）提供了一种可以通过key或时间区间来查询消息的方法
        - IndexFile的底层存储设计为在文件系统中实现HashMap结构，故rocketmq的索引文件其底层实现为hash索引
    - tip
        - RocketMQ采用的是混合型的存储结构，即为Broker单个实例下所有的队列共用一个日志数据文件（即为CommitLog）来存储
        - RocketMQ的混合型存储结构(多个Topic的消息实体内容都存储于一个CommitLog中)针对Producer和Consumer分别采用了数据和索引部分相分离的存储结构
        - 当无法拉取到消息后，可以等下一次消息拉取，同时服务端也支持长轮询模式，如果一个消息拉取请求未拉取到消息，Broker允许等待30s的时间，只要这段时间内有新消息到达，将直接返回给消费端
        - RocketMQ的具体做法是，使用Broker端的后台服务线程—ReputMessageService不停地分发请求并异步构建ConsumeQueue（逻辑消费队列）和IndexFile（索引文件）数据。 
        - 同一个top下有多个消息队列文件？ 消息顺序?
            
- 页缓存与内存映射
    - 页缓存（PageCache)是OS对文件的缓存，用于加速对文件的读写
    - OS使用PageCache机制对读写访问操作进行了性能优化，将一部分的内存用作PageCache
    - 对于数据的读取，如果一次读取文件时出现未命中PageCache的情况，OS从物理磁盘上访问读取文件的同时，会顺序对其他相邻块的数据文件进行预读取
    - ConsumeQueue逻辑消费队列存储的数据较少，并且是顺序读取，在page cache机制的预读取作用下，Consume Queue文件的读性能几乎接近读内存，即使在有消息堆积情况下也不会影响性能
    
    - RocketMQ主要通过MappedByteBuffer对文件进行读写操作，其中，利用了NIO中的FileChannel模型将磁盘上的物理文件直接映射到用户态的内存地址中，将对文件的操作转化为直接对内存地址进行操作，从而极大地提高了文件的读写效率
    - 减少了在操作系统内核地址空间的缓冲区和用户应用程序地址空间的缓冲区之间来回进行拷贝的性能开销
    - 正因为需要使用内存映射机制，故RocketMQ的文件存储都使用定长结构来存储，方便一次将整个文件映射至内存
    
- 消息刷盘
    - 对于数据的写入，OS会先写入至Page Cache内，随后将Page Cache内的数据刷盘至物理磁盘上
    - 同步刷盘：只有在消息真正持久化至磁盘后RocketMQ的Broker端才会真正返回给Producer端一个成功的ACK响应
    - 异步刷盘：能够充分利用OS的PageCache的优势，只要消息写入PageCache即可将成功的ACK返回给Producer端
        - 消息刷盘采用后台异步线程提交的方式进行，降低了读写延迟，提高了MQ的性能和吞吐量。
        
- 通信机制  
    - 基本通讯流程
        - (2) 消息生产者Producer作为客户端发送消息时候，需要根据消息的Topic从本地缓存的TopicPublishInfoTable获取路由信息。如果没有则更新路由信息会从NameServer上重新拉取，同时Producer会默认每隔30s向NameServer拉取一次路由信息 *
        - (3) 消息生产者Producer根据2）中获取的路由信息选择一个队列（MessageQueue）进行消息发送；Broker作为消息的接收者接收消息并落盘存储。 * 
        - (4) 消息消费者Consumer根据2）中获取的路由信息，并再完成客户端的负载均衡后，选择其中的某一个或者某几个消息队列来拉取消息并进行消费。 * 
        
        - 一个良好的网络通信模块在MQ中至关重要，它将决定RocketMQ集群整体的消息传输能力与最终的性能
        - RocketMQ消息队列自定义了通信协议并在Netty的基础之上扩展了通信模块
    - 协议设计与编解码 *
    - 消息的通信方式和流程 *
    - Reactor多线程设计(netty) *

- 消息过滤- tag
    - 在Consumer端订阅消息时再做消息过滤的，RocketMQ这么做是在于其Producer端写入消息和Consumer端订阅消息采用分离存储的机制来实现的，Consumer端订阅消息是需要通过ConsumeQueue这个消息消费的逻辑队列拿到一个索引，然后再从CommitLog里面读取真正的消息实体内容
    
    - Tag过滤方式
        - Consumer端在订阅消息时除了指定Topic还可以指定TAG
        - ConsumeQueue的存储结构其中有8个字节存储的Message Tag的哈希值，基于Tag的消息过滤正式基于这个字段值的。
        - 从 ConsumeQueue读取到一条记录后，会用它记录的消息tag hash值去做过滤，由于在服务端只是根据hashcode进行判断，无法精确对tag原始字符串进行过滤，故在消息消费端拉取到消息后，还需要对消息的原始tag字符串进行比对，如果不同，则丢弃该消息，不进行消息消费(这一步需不需要用户处理 *)
    - SQL92的过滤方式
        - BloomFilter
        
- 负载均衡
    - RocketMQ中的负载均衡都在Client端完成
    - Producer端发送消息的负载均衡 * 
        - sendLatencyFaultEnable开关变量，如果开启，在随机递增取模的基础上，再过滤掉not available的Broker代理
        - latencyFaultTolerance是指对之前失败的，按一定的时间做退避,实现消息发送高可用的核心关键所在
    - Consumer端订阅消息的负载均衡
        - Consumer端的两种消费模式（Push/Pull）都是基于拉模式来获取消息的
        - 而在Push模式只是对pull模式的一种封装，其本质实现为消息拉取线程在从服务器拉取到一批消息后，然后提交到消息消费线程池后，又“马不停蹄”的继续向服务器再次尝试拉取消息。如果未拉取到消息，则延迟一下又继续拉取
        - 均需要Consumer端在知道从Broker端的哪一个消息队列—队列中去获取消息。因此，有必要在Consumer端来做负载均衡，即Broker端中多个MessageQueue分配给同一个ConsumerGroup中的哪些Consumer消费
        
        - Consumer端的心跳包发送
            - 向RocketMQ集群中的所有Broker实例发送心跳包(消息消费分组名称、消息通信模式-广播或集群)，所有Broker实例从Name Server获取
        - Consumer端实现负载均衡的核心类—RebalanceImpl
            - 该Topic主题下的消息消费队列集合(ConsumeQueue)
            - 该消费组下消费者Id列表
            - processQueueTable
        - 集群模式
            - 消息消费队列在同一消费组不同消费者之间的负载均衡，其核心设计理念是在一个消息消费队列在同一时间只允许被同一消费组内的一个消费者消费，一个消息消费者能同时消费多个消息队列
- 事务消息
    - 本地事务和发送消息是一个原子操作
    - 采用了2PC的思想来实现了提交事务消息，同时增加一个补偿逻辑来处理二阶段超时或者失败的消息
    
    - 2PC
        - 一阶段发送的消息(half消息)对用户是不可见的，
            - 对消息的Topic和Queue等属性进行替换(topic替换为特殊内部topic)，同时将原来的Topic和Queue信息存储到消息的属性中，正因为消息主题被替换，故消息并不会转发到该原主题的消息消费队列，消费者无法感知消息的存在，不会消费; 由于消费组未订阅该主题，故消费端无法消费half类型的消息;
                - 延时消息的实现机制类似原理
        - 二阶段
            - Op消息(commit or rollback发送的消息)
                - Op消息标识事务消息已经确定的状态（Commit或者Rollback）。如果一条事务消息没有对应的Op消息，说明这个事务的状态还无法确定（可能是二阶段失败了）
                - 引入Op消息后，事务消息无论是Commit或者Rollback都会记录一个Op操作
                - RocketMQ将Op消息写入到全局一个特定的Topic，这个Topic是一个内部的Topic（像Half消息的Topic一样），不会被用户消费
                - Op消息的内容为对应的Half消息的存储的Offset，这样通过Op消息能索引到Half消息进行后续的回查操作
            - commit
                - 需要让消息对用户可见
                - 需要构建出Half消息的ConsumeQueue二级索引
                - 读取出Half消息，并将Topic和Queue替换成真正的目标的Topic和Queue，之后通过一次普通消息的写入操作来生成一条对用户可见的消息
            - rollback
                - 本身一阶段的消息对用户是不可见的，其实不需要真正撤销消息（实际上RocketMQ也无法去真正的删除一条消息，因为是顺序写文件的）;但是区别于这条消息没有确定状态（Pending状态，事务悬而未决），需要一个操作来标识这条消息的最终状态  
        - 处理二阶段失败(补偿机制)
            - 回查
                - Broker端通过对比Half消息和Op消息进行事务消息的回查并且推进CheckPoint（记录那些事务消息的状态是确定的）
                - Broker端对未确定状态的消息发起回查，将消息发送到对应的Producer端（同一个Group的Producer），由Producer根据消息来检查本地事务的状态，进而执行Commit或者Rollback
                - rocketmq并不会无休止的的信息事务状态回查，默认回查15次，如果15次回查还是无法得知事务状态，rocketmq默认回滚该消息
                - RocketMQ会开启一个定时任务，从一阶段特殊内部topic中拉取消息进行消费，根据生产者组获取一个服务提供者发送回查事务状态请求，根据事务状态来决定是提交或回滚消息
                
- 消息查询
    - MessageId查询消息
        - 读取消息的过程用其中的 commitLog offset 和 size 去 commitLog 中找到真正的记录并解析成一个完整的消息返回
    - Message Key查询消息
        - 基于RocketMQ的IndexFile索引文件来实现，类似JDK中HashMap的实现
        - 文件大小是固定的，等于40+500W*4+2000W*20= 420000040个字节大小
        - 如果消息的properties中设置了UNIQ_KEY这个属性，就用 topic + “#” + UNIQ_KEY的value作为 key 来做写入操作。如果消息设置了KEYS属性（多个KEY以空格分隔），也会用 topic + “#” + KEY 来做索引
        - IndexFile索引文件存放的真正的 key 是有 topic前缀的
        - 读取消息的过程就是用topic和key找到IndexFile索引文件中的一条记录，根据其中的commitLog offset从CommitLog文件中读取消息的实体内容