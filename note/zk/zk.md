


- zkCli
    - ls 指定节点下第一级的所有子节点
        - 第一次部署zk 默认在根节点"/"下有 "/zookeeper"保留节点
    - get 获取节点的数据内容和属性信息 
        - mZxid 最后一次更新该节点的事务ID
        - cZxid 创建该节点的事务ID
    - delete 
        - 删除指定节点 该节点必须没有子节点存在
- server
    - zookeeper规定所有非叶子节点必须为持久节点
    
- curator
    - 事件监听
        - 标准的观察模式
            - 使用Watcher 监听器
            - 使用Watcher监听器实例的方式也很简单，在Curator的调用链上，加上usingWatcher方法即可
            - Watcher监听器是一次性的，如果要反复使用，就需要反复的使用usingWatcher提前注册。
        - 缓存监听模式
            - 一种本地缓存视图的Cache机制
            - 能够自动处理反复注册监听
            - 当感知到zk集群的znode状态变化，会触发event事件，注册的监听器会处理这些事件
            - Path Cache，Node Cache，Tree Cache
            
            
- sync命令 强制同步
    - 由于请求在半数以上的zk server上生效就表示此请求生效，那么就会有一些zk server上的数据是旧的。sync命令就是强制同步所有的更新操作。
    
- Zookeeper一致性级别

- Zookeeper损失的是不同客户端的读写操作的一致性
    - 如何保证不同客户端的读写操作的一致性
        - sync
        - watcher通知机制
- 有序节点
    - 序号的最大值
    - zookeeper 临时有序节点最大序号节点失效后，后面新建的序号是否会继续严格递增
- Zookeeper vs Chubby
    - zookeeper 
        - Zookeeper：provide a simple and high performance kernel for building more complex coordination primitives at the client.
        - 而Zookeeper则倾向于构造一个“Kernel”，而利用这个“Kernel”客户端可以自己实现众多更复杂的分布式协调机制。自然的，Chubby倾向于提供更精准明确的操作来免除使用者的负担，Zookeeper则需要提供更通用，更原子的原材料，留更多的空白和自由给Client。也正是因此，为了更适配到更广的场景范围，Zookeeper对性能的提出了更高的要求
        - 写锁
            - curator LeaderLatch实现
        - 读锁
            - 读锁稍宽松，只要没有比自己编号更小的写节点就可以加读锁成功。
        - cache
            - Zookeeper根本没有实现Cache功能，用户如果需要必须自己实现，利用watcher机制，用户能方便的按自己需求实现一致或不一致的Cache语义
        - Zookeeper提供Sync操作来满足对更高的一致性要求的场景。
        - Zookeeper：写操作线性(Linearizable writes) + 客户端有序(FIFO client order)
        - 线性一致性
            - 即使发生网络分区或机器节点异常，整个集群依然能够像单点一样提供一致的服务，即依次原子地执行每一条操作


- zab vs paxos 
    - paxos
        - 对于Acceptors：
          - 当收到Prepare请求时，如果其编号n大于之前所收到的Prepare消息，则回复。
          - 当收到Accept请求时，仅当它没有回复过一个具有更大编号的Prepare消息，接受该Proposal并回复
          
        - Paxos算法解决的问题正是分布式一致性问题，即一个分布式系统中的各个进程如何就某个值（决议）达成一致。
        - Paxos算法是一种基于消息传递且具有高度容错特性的共识（consensus）算法。需要注意的是，Paxos常被误称为“一致性算法”。但是“一致性（consistency）”和“共识（consensus）”并不是同一个概念。Paxos是一个共识（consensus）算法
        - 提案选定过程:算法陈述 p34 类似于两阶段提交的算法执行过程
            - Proposer Acceptor
            - 选取主Proposer避免陷入死循环
                - 极端情况：有两个Proposer一次提出了一系列编号递增的议案，但是最终都无法被选定，进入死循环
                - 保持Paxos算法的活性
        - 提案的获取: Learner获取提案(的详情)
            - 一个主Learner
            - 主Learner集合
            
        - Paxos将系统中的角色分为提议者 (Proposer)，决策者 (Acceptor)，和最终决策学习者 (Learner); 在多副本状态机中，每个副本同时具有Proposer、Acceptor、Learner三种角色: 
            - Proposer: 提出提案 (Proposal)。Proposal信息包括提案编号 (Proposal ID) 和提议的值 (Value)。
            - Acceptor：参与决策，回应Proposers的提案。收到Proposal后可以接受提案，若Proposal获得多数Acceptors的接受，则称该Proposal被批准。
            - Learner：不参与决策，从Proposers/Acceptors学习最新达成一致的提案（Value）。
        - Paxos算法运行在允许宕机故障的异步系统中，不要求可靠的消息传递，可容忍消息丢失、延迟、乱序以及重复。它利用大多数 (Majority) 机制保证了2F+1的容错能力，即2F+1个节点的系统最多允许F个节点同时出现故障。
        - 活锁（Livelock）
            - 两个Proposers交替Prepare成功，而Accept失败
            - Acceptor响应Prepare请求的承诺之一，Acceptor不再应答Proposal ID小于等于当前请求的Prepare请求

- chubby 
    - chubby底层一致性实现以Paxos算法为基础
- 分布式一致性协议
    - 不同分布式一致性算法从不同方面不同程度地解决了分布式数据一致性问题
    - 三种典型的一致性算法
        - 两阶段提交协议
            - 解决了分布式事务的原子性问题
            - 存在同步阻塞、无限期等待和 "脑裂"等问题
        - 三阶段提交协议
            - 在两阶段提交协议基础上 添加了PreCommit过程，避免了无限期等待
        - Paxos算法
            - "过半"理念，少数服从多数的原则
            - 支持分布式节点角色之间的轮换
            - 既解决了 无限期等待问题， 也解决了 "脑裂"问题

- 分布式系统的一致性与共识性
    - 一致性是指分布式系统中的多个服务节点，给定一系列的操作，在约定协议的保障下，使它们对外界呈现的状态是一致的。换句话说，也就是保证集群中所有服务节点中的数据完全相同并且能够对某个提案（Proposal）达成一致
    - 分布式事务一致性和分布式数据一致性这两种说法搞混淆。实际上，两者是从两种不同的角度对一致性的描述
    - 事务（数据库事务的简称）是数据库管理系统中执行过程中的一个逻辑单位，由一个有限的数据库操作序列构成
    - 分布式数据一致性，指的是“数据在多份副本中存储时，各副本中的数据是一致的”。
    - 分布式事务一致性，指的是“操作序列在多个服务节点中执行的顺序是一致的”。
    - 保证了分布式事务的一致性，也就保证了数据的一致性。
    - 共识性描述了分布式系统中多个节点之间，彼此对某个状态达成一致结果的过程。 在实践中，要保障系统满足不同程度的一致性，核心过程往往需要通过共识算法来达成。
      

- Multi-Paxos算法
    - 针对每一个要确定的值，运行一次Paxos算法实例（Instance），形成决议。每一个Paxos实例使用唯一的Instance ID标识
    - 在所有Proposers中选举一个Leader，由Leader唯一地提交Proposal给Acceptors进行表决。这样没有Proposer竞争，解决了活锁问题。在系统中仅有一个Leader进行Value提交的情况下，Prepare阶段就可以跳过，从而将两阶段变为一阶段，提高效率
    
    - Multi-Paxos通过改变Prepare阶段的作用范围至后面Leader提交的所有实例，从而使得Leader的连续提交只需要执行一次Prepare阶段，后续只需要执行Accept阶段，将两阶段变为一阶段，提高了效率。为了区分连续提交的多个实例，每个实例使用一个Instance ID标识，Instance ID由Leader本地递增生成即可
    - Multi-Paxos允许有多个自认为是Leader的节点并发提交Proposal而不影响其安全性，这样的场景即退化为Basic Paxos。
     
    - Chubby和Boxwood均使用Multi-Paxos。ZooKeeper使用的Zab也是Multi-Paxos的变形。
    
    - 不Prepare直接Accept为啥是安全的
      - 在一个Leader提交proposal的前提下，不会有其他Proposer提交，那么就不会出现Acceptor promised的最大编号大于proposal中所带编号的情况，同样也不会出现Acceptor accept的编号大于proposal中所带编号的情况。因此这么做是安全的。
      
      
      
  - 分布式锁
  
其实，各个领域的线性一致性都是一样的。线性一致性最早在并行计算领域提出，现在在分布式领域、数据库领域都在用，含义是一样的。我们可以把线性一致性称作为强一致性，或者原子一致性。准确的来说，Zookeeper如果只有写请求时，是线性一致性的；如果从读和写的角度来说是顺序一致性的
严格说【Zookeeper如果只有写请求时，是线性一致性的；如果从读和写的角度来说是顺序一致性的】


假设有2n+1个server，在同步流程中，leader 向 follower 同步数据，当同步完成的 follower 数量大于 n+1时同步流程结束，系统可接受 client 的连接请求。如果client 连接的并非同步完成的follower，那么得到的并非最新数据，但可以保证单调性。
假设是 follower 接收的写请求，然后转发给 leader 处理；leader 完成两阶段提交的机制。向所有 server 发起提案，当提案获得超过半数（n+1）的 server 认同后，将对整个集群进行同步，超过半数（n+1）的 server 同步完成后，该写请求完成。如果 client 连接的并非同步完成 follower，那么得到的并非最新数据，但可以保证单调性。


用分布式系统的CAP原则来分析Zookeeper
（1）C（一致性）: Zookeeper保证了顺序一致性（满足最终一致性），在十几秒可以Sync到各个节点
（2）A（可用性）: Zookeeper保证了可用性，数据总是可用的，没有锁。并且有一大半的节点所拥有的数据是最新的、实时的。 如果想保证取得是数据一定是最新的，需要手工调用Sync()； 恢复模式下是不可用的
（3）P（分区容错性）: 有两点需要分析
节点多了会导致写数据延时变大，因为更多的节点需要同步
节点多了Leader选举耗时变长，从而会放大网络的问题， 可以通过引入 observer（不参与选举）节点缓解这个问题.



client连接到follower 分布式锁 获取锁节点信息 如果follower不在过半写成功的列表中 此处获取锁节点信息是否要调用sync同步leader