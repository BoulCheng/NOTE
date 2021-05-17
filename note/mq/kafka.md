# kafka



#### consumer
- pull vs push
    - push
        - broker控制着数据传输速率, 当消费速率低于生产速率时，consumer 往往会不堪重负（本质上类似于拒绝服务攻击）
        - 必须选择立即发送请求或者积累更多的数据，然后在不知道下游的consumer能否立即处理它的情况下发送这些数据，如果系统调整为低延迟状态，这就会导致一次只发送一条消息，以至于传输的数据不再被缓冲，这种方式是极度浪费的
    - pull
        - consumer 速率落后于 producer 时，可以在适当的时间赶上来 
        - 可以大批量pull数据
        - consumer 的每个请求都在 log 中指定了对应的 offset，并接收从该位置开始的一大块数据，可以拉取所有可拉取的数据
        - 可以在需要的时候通过回退到该位置再次消费对应的数据
        - 如果 broker 中没有数据，consumer 可能会在一个紧密的循环中结束轮询，实际上 busy-waiting 直到数据到来。
        - 为了避免 busy-waiting，我们在 pull 请求中加入参数，使得 consumer 在一个“long pull”中阻塞等待，直到数据到来（还可以选择等待给定字节长度的数据来确保传输长度）
- 消费者的位置 offset
    - 其他mq系统
        - 持续追踪已经被消费的内容是消息系统的关键性能点之一
        - 大多数消息系统都在 broker 上保存被消费消息的元数据(broker 知道哪些消息被消费了，就可以在本地立即进行删除，一直保持较小的数据量)
            - 让 broker 和 consumer 就被消费的数据保持一致性的问题
            - 消息确认机制
    - Kafka的 topic 被分割成了一组完全有序的 partition，其中每一个 partition 在任意给定的时间内只能被每个订阅了这个 topic 的 consumer 组中的一个 consumer 消费
    - 这意味着 partition 中 每一个 consumer 的位置仅仅是一个数字，即下一条要消费的消息的offset。这使得被消费的消息的状态信息相当少，每个 partition 只需要一个数字
    - consumer 可以回退到之前的 offset 来再次消费之前的数据，这个操作违反了队列的基本原则，但事实证明对大多数 consumer 来说这是一个必不可少的特性
    
    
#### 消息交付语义
- Exactly once——这正是人们想要的, 每一条消息只被传递一次
    - producer
        - 如果 producer 没有收到表明消息已经被提交的响应, 那么 producer 可以将消息重传
        - 如果最初的请求事实上执行成功了，producer有等性的传递选项，该选项保证重传不会在 log 中产生重复条目
    - broker
        -  一旦消息被提交，只要有一个 broker 备份了该消息写入的 partition，并且保持“alive”状态，该消息就不会丢失
        -  committed message 和 alive partition 
    - consumer