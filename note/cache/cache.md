

- 本地缓存
    - 后端本地缓存
        - 实现
            - Ehcache
            - Guava Cache
        - 刷新策略
            - pull 
                - 定时任务
                - http 长轮询机制
                    - Servlet 3.0 的异步机制响应数据
                    - RocketMQ 的消费者模型也同样被使用，接近准实时，并且可以减少服务端的压力
            - push 主动推送给客户端
                - zookeeper watch机制
                    - 会将数据全量写入 zookeeper，后续数据发生变更时，会增量更新 zookeeper 的节点，watch机制节点变更通知客户端更新本地缓存
                - websocket 机制
                    - 首次建立好 websocket 连接时，推送一次全量数据，后续数据发生变更，则将增量数据通过 websocket 主动推送给客户端更新本地缓存
                - RocketMQ Remoting 通讯框架
        
    - 前端页面级缓存 
- 分布式缓存
    - 实现
        - redis
            - redis使用的是单线程模型，保证了数据按顺序提交。redis单线程模型只能使用一个cpu，可以开启多个redis进程
            - mget 、hmget命令
            - pipleline
                - “打小包，短sleep的”策略 避免影响其他读写操作
            - lua脚本模式
        - memcached
            - Memcached可以使用多核，所以平均每一个核上Redis在存储小数据时比Memcached性能更高。而在100k以上的数据中，Memcached性能要高于Redis，虽然Redis最近也在存储大数据的性能上进行优化，但是比起Memcached，还是稍有逊色。
                - - memcache需要使用cas保证数据一致性。CAS（Check and Set）是一个确保并发一致性的机制，属于“乐观锁”范畴
            - memcache不支持数据持久存储
            - memcache可以使用一致性hash做分布式
            - memcache是一个内存缓存，key的长度小于250字符，单个item存储要小于1M，不适合虚拟机使用
        - Ehcache
            - 分布式解决方案
                - RMI组播
                - Jgroups
                    - 可通过TCP建立p2p点对点通信更新本地缓存
                - JMS
                    - 这种模式的核心就是一个消息队列(ActiveMQ、Kafka、RabbitMQ)
                    - 通过消息队列来更新本地缓存
    - 控制读取策略，最大程度减少GC的频率
        - 缓存数据的格式
        - 全量数据与增量数据分开 避免全量数据过滤成增量数据

- 多级缓存
    - 本地缓存 + 分布式缓存
        - 本地缓存速度极快，但是容量有限，而且无法共享内存
        - 分布式缓存容量可扩展，但在高并发场景下，如果所有数据都必须从远程缓存种获取，很容易导致带宽跑满，吞吐量下降
    - 高并发场景下, 能提升整个系统的吞吐量，减少分布式缓存的压力
    
    
- 使用场景
    - API网关
    
    
    
    
- 缓存问题
    - 缓存击穿是指一个Key非常热点，在不停的扛着大并发，大并发集中对这一个点进行访问，当这个Key在失效的瞬间，持续的大并发就穿破缓存，直接请求数据库，就像在一个完好无损的桶上凿开了一个洞