


#### 

生成整个交易所的K线。接受交易数据的mq消息来生成K线，最新一个K线点数据以字符串单独放在redis，所有历史K线点以有序集合放在redis中，有序集合的score存放K线点的时间以对历史K线进行排序，历史的K线点同时会存在mysql中。
为了保证在短时间内没有交易的情况下K线正常生成，开启定时任务定时初始化K线。如：1分钟维度的K线，则定时任务的触发时间是每整分钟，判断redis不存在最新K线则会取上一分钟的数据生成当前分钟最新K线点。
每次接受到交易mq，如果redis存在最新K线点则判断并更新开高收低价格及成交量，如果不存在则继续判断该交易K线点是否存在于数据库中，如果不存在则redis新增最新K线点，如果存在则表明该交易数据接受延迟，同时更新redis和数据库中的历史K线点
此外还有一个定时任务定时清理redis历史K线，保证redis历史K线维持在一定数量，避免无限使用redis内存
高可用: K线生成应用会多机部署，因此定时任务使用了elastic-job，只会有一台机器真正触发定时任务，并且保证发生故障时转移机器。
分布式锁: 由于是多机部署，可能发生多台机器同时更新同一个K线点数据，为保证数据的一致性，基于redis实现了分布式锁，更新K线点时需要先获取锁，锁实现为一个注解，具体获取锁的逻辑实现在切面
高性能高并发: 
1.多次访问redis操作合并为一个redis lua脚本访问一次redis。
2.原本每次收到交易mq数据都会立即处理更新redis最新K线，改为333ms内的数据先更新到内存合并为一条数据，定时任务每333ms把内存中合并后的一条数据更新到K线点，
其中比较并更新内存中的数据由于并发安全问题会加锁，这里的synchronized同步块的监视器锁会根据业务用不同的监视器锁，以减少锁的粒度，不同标的的K线更新时持有不同的锁从而可同时执行同一synchronized同步块的代码






K线的最新一个K线点的数据以字符串数据类型单独存放Redis，部分历史K线点以有序集合(sorted set)数据类型存放Redis，且有序集合的分数(score)存放的是K线点对应的时间戳以使Redis中历史K线按时间进行排序方便查询，剩余其他所有历史K线存放在MySQL；
定时任务初始化生成最新的K线点数据，并同步上一K线点到Redis中历史K线点的有序集合(如1分钟维度的K线，当前时间14:01，定时任务触发生成14:01 K线点的初始化数据，并同步14:00 K线点的字符串数据类型K线数据到历史K线点的有序集合中)，
同时会有额外的定时任务同步历史K线点有序集合中的数据到MySQL，以保证Redis中存放历史K线点数据保持在一定的数据量；
所有定时任务都采用elasticjob框架的分布式定时任务以支持定时任务的高可用和Master-Slave模式只有一个任务节点工作，即当K线应用分布式部署时只有其中一个应用的定时任务会被触发，其他应用的定时任务只是作为备选在发生故障转移时使用；

当消费交易数据MQ消息一般情况下会更新Redis中以字符串数据类型存放的最新K线点数据，此时会发生K线数据的比较并执行更新，为避免读取、比较计算、更新操作的非原子性操作出现并发问题和减少多次操作Redis的网络开销，此处采用Redis Lua把读取、比较计算、更新操作放在一个Redis脚本中执行
在特殊情况下，(在1分钟K线维度下)比如在14:00:00上述定时任务触发初始化生成最新的K线点(14:00)数据正在执行且未完成，此时有一笔交易数据MQ消息需要更新14:00K线点的数据，那么当发现Redis没有最新的K线点会尝试去执行本该由定时任务做的生成最新的K线点(14:00)数据的工作，此时使用分布式锁保证定时任务的工作只被做执行

集群模式消费MQ消息，应用分布式部署时相同Consumer Group的每个Consumer实例分摊全量消息；同时K线系统单独使用一个Redis cluster集群以避免与其他业务的相互影响。


- 数据结构

- 分布式锁

- 如何确定一条消息被消费了 为什么MessageListenerConcurrently每次消费一条

- elasticjob failover

- gc模拟

#### 分布式数据一致性
- 分布式锁

#### 高并发
- redis lua 脚本

#### 缓存 
- 先处理redis数据 再处理数据库数据 
- 

#### 难点

- 历史K线缓存数据类型的选择 zset
- 更新K线分布式并发安全问题 分布式锁
- 业务复杂问题K线 连续性问题 
- 
- 高并发问题 
    - 高频访问的最新K线放在redis 当下一刻度的K线生成时转存上一K线
    - 数据库索引
    - redis lua

#### 总结
- 最新K线放在redis 数据类型 string（字符串）；历史K线也放在redis 数据类型为zset(sorted set：有序集合) 同时也存放mysql数据库，并且redis中的历史数仅据保存一定的量，会有定时任务定时清理超出的数据量；
- 通过redis lua 保证CAS更新redis最新K线 (比较于reids分布式，虽然可以不使用redis lua，但会增加三次redis访问，也增加释放锁失败的风险，以及等待时不断的访问redis消耗资源等等) 
- 下一刻度K线时间点到达时，定时任务会触发执行生成一条默认值的最新K线，并把上一刻度时间点的最新K线转存到数据库和redis中历史K线，
- 通过redis实现分布式锁 实现分布式同步 (即只有一个线程会执行最新K线转存到数据库和redis中历史K线)

- 获取整个交易所所有种类的最新K线通过Pipeline获取

#### 压测
- 1000 / (4次 * 1.5ms) = 200 tps    400
- 200 * 10台 = 2000 tps  4000
- 

