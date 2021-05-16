
###  Redis在使用 RDB 进行快照时通过fork子进程的方式进行实现
- 通过 fork 创建的子进程能够获得和父进程完全相同的内存空间，父进程对内存的修改对于子进程是不可见的，两者不会相互影响
- 通过 fork 创建子进程时不会立刻触发大量内存的拷贝，内存在被修改时会以页为单位进行拷贝，这也就避免了大量拷贝内存而带来的性能问题
- 上述两个原因中，一个为子进程访问父进程提供了支撑，另一个为减少额外开销做了支持，这两者缺一不可，共同成为了 Redis 使用子进程实现快照持久化的原因


#### redis单线程

# redis
- Redis单线程为什么速度还那么快，就是因为用了多路复用IO和缓存操作的原因
- Redis单进程单线程的特性，天然可以解决分布式集群的并发问题
- 单进程单线程模型的原因
    - 绝大部分请求是纯粹的内存操作
    - 采用单线程,避免了不必要的上下文切换和竞争条件
    - 内部实现采用非阻塞IO和epoll，基于epoll自己实现的简单的事件框架。epoll中的读、写、关闭、连接都转化成了事件，然后利用epoll的多路复用特性，绝不在io上浪费一点时间
#### Lua
- 原子性
    - Lua脚本会以原子性方式进行，单线程的方式执行脚本，在执行脚本时不会再执行其他脚本或命令。并且，Redis只要开始执行Lua脚本，就会一直执行完该脚本再进行其他操作，所以Lua脚本中不能进行耗时操作
- Redis集群键值映射问题
    - Redis的KeySlot算法中，如果key包含{}，就会使用第一个{}内部的字符串作为hash key，这样就可以保证拥有同样{}内部字符串的key就会拥有相同slot。Redis要求单个Lua脚本操作的key必须在同一个节点上，但是Cluster会将数据自动分布到不同的节点，使用这种方法就解决了上述的问题
- 优点
    - 还可以减少与Redis的交互，减少网络请求的次数
- 缺点
    - Lua脚本中不能进行耗时操作
- 使用场景
    - 有很多，比如说分布式锁，限流，秒杀等，
    - 总结起来，下面两种情况下可以使用Lua脚本:
        - 使用 Lua 脚本实现原子性操作的CAS，避免不同客户端先读Redis数据，经过计算后再写数据造成的并发问题
        - 前后多次请求的结果有依赖时，使用 Lua 脚本将多个请求整合为一个请求
            - 如果前后多次请求的结果没有依赖时可使用pipeline
- 注意事项
    - 不要自定义的全局变量；要保证安全性，在Lua脚本中不要定义自己的全局变量，以免污染 Redis内嵌的Lua环境。因为Lua脚本中你会使用一些预制的全局变量，比如说 redis.call()
    - 注意 Lua 脚本的时间复杂度；Redis 的单线程同样会阻塞在 Lua 脚本的执行中
    - 原子操作的破坏；使用 Lua 脚本实现原子操作时，要注意如果 Lua 脚本报错，之前的命令无法回滚，这和Redis所谓的事务机制是相同的。 待确认*
    - 使用pipeline代替； 一次发出多个 Redis 请求，但请求前后无依赖时，使用 pipeline，比 Lua 脚本方便
    - Lua脚本操作的key必须在同一个Redis节点上；Redis要求单个Lua脚本操作的key必须在同一个Redis节点上。解决方案可以看下文对Gateway原理的解析
- 性能测试
    - redis benchmark
    ```

    ```
## redis集群
- 节点
    - cluster-enabled yes 开启服务器的集群模式成为一个节点
    
- tip
    - Redis 集群是一个可以在多个 Redis 节点之间进行数据共享的设施（installation）。
    - Redis 集群不支持那些需要同时处理多个键的 Redis 命令， 因为执行这些命令需要在多个 Redis 节点之间移动数据， 并且在高负载的情况下， 这些命令将降低 Redis 集群的性能， 并导致不可预测的行为。
    - Redis 集群提供了以下两个好处
        - 将数据自动切分（split）到多个节点的能力。
        - 当集群中的一部分节点失效或者无法进行通讯时， 仍然可以继续处理命令请求的能力。


#### sorted set
- Redis中的sorted set，是在skiplist, dict和ziplist基础上构建起来的:
    - 当数据较少时，sorted set是由一个ziplist来实现的
        - ziplist就是由很多数据项组成的一大块连续内存,
        - 当使用zadd命令插入一个(数据, score)对的时候，底层在相应的ziplist上就插入两个数据项：数据在前，score在后
        - ziplist的主要优点是节省内存，但它上面的查找操作只能按顺序查找（可以正序也可以倒序）
    - 当数据多的时候，sorted set是由一个叫zset的数据结构来实现的，这个zset包含一个dict + 一个skiplist。dict用来查询数据到分数(score)的对应关系，而skiplist用来根据分数查询数据（可能是范围查找）
        - 随着数据的插入，sorted set底层的这个ziplist就可能会转成zset的实现
        - Redis配置
            ```aidl
            # Similarly to hashes and lists, sorted sets are also specially encoded in
            # order to save a lot of space. This encoding is only used when the length and
            # elements of a sorted set are below the following limits:
            zset-max-ziplist-entries 128
            zset-max-ziplist-value 64
            ```
        - 这个配置的意思是说，在如下两个条件之一满足的时候，ziplist会转成zset（具体的触发条件参见t_zset.c中的zaddGenericCommand相关代码）
            - 当sorted set中的元素个数，即(数据, score)对的数目超过128的时候，也就是ziplist数据项超过256的时候。
            - 当sorted set中插入的任意一个数据的长度超过了64的时候。
- ziplist
    - ziplist是一个经过特殊编码的双向链表，它的设计目标就是为了提高存储效率。ziplist可以用于存储字符串或整数，其中整数是按真正的二进制表示进行编码的，而不是编码成字符串序列。它能以O(1)的时间复杂度在表的两端提供push和pop操作
    - 一个普通的双向链表，链表中每一项都占用独立的一块内存，各项之间用地址指针（或引用）连接起来。这种方式会带来大量的内存碎片，而且地址指针也会占用额外的内存
    - 而ziplist却是将表中每一项存放在前后连续的地址空间内，一个ziplist整体占用一大块内存。它是一个表（list），但其实不是一个链表（linked list）
    - 另外，ziplist为了在细节上节省内存，对于值的存储采用了变长的编码方式，大概意思是说，对于大的整数，就多用一些字节来存储，而对于小的整数，就少用一些字节来存储
    
    - 缺点
        - ziplist本来就设计为各个数据项挨在一起组成连续的内存空间，这种结构并不擅长做修改操作。一旦数据发生改动，就会引发内存realloc，可能导致内存拷贝
        - ziplist变得很大的时候，它有如下几个缺点
            - 每次插入或修改引发的realloc操作会有更大的概率造成内存拷贝，从而降低性能
            - 查找指定的数据项就会性能变得很低，因为ziplist上的查找需要进行遍历
#### 持久化


- 持久化
	- AOF 持久化记录服务器执行的所有写操作命令，并在服务器启动时，通过重新执行这些命令来还原数据集。AOF 文件中的命令全部以 Redis 协议的格式来保存，新命令会被追加到文件的末尾。 Redis 还可以在后台对 AOF 文件进行重写（rewrite），使得 AOF 文件的体积不会超出保存数据集状态所需的实际大小。AOF 文件是一个只进行追加操作的日志文件。执行 BGREWRITEAOF 命令， Redis 将生成一个新的 AOF 文件， 这个文件包含重建当前数据集所需的最少命令。
	- RDB 持久化可以在指定的时间间隔内生成数据集的时间点快照（point-in-time snapshot）。可以通过调用 SAVE 或者 BGSAVE ， 手动让 Redis 进行数据集保存操作。

	-  BGSAVE 执行的过程中， 不可以执行 BGREWRITEAOF 。 反过来说， 在 BGREWRITEAOF 执行的过程中， 也不可以执行 BGSAVE 。
	- 同时使用 AOF 持久化和 RDB 持久化，当 Redis 重启时， 它会优先使用 AOF 文件来还原数据集
- AOF
	- 耐久性更好，AOF 的默认策略为每秒钟 fsync 一次，最多丢失一秒钟的数据
	- 写入操作以 Redis 协议的格式保存， 因此 AOF 文件的内容非常容易读懂、分析
	- 根据所使用的 fsync 策略，AOF 的速度可能会慢于 RDB
	- 对于相同的数据集来说，AOF 文件的体积通常要大于 RDB 文件的体积。

- RDB
	- 适合备份数据
	- 数据可以压缩 更紧凑
	- RDB 在恢复大数据集时的速度比 AOF 的恢复速度要快
	- 持久性更差，可能丢失几分钟的数据
	- 
#### 性能
```
zhenglubiaodeMacBook-Pro:src zlb$ pwd
/Users/zlb/application/redis-3.0.6/src
zhenglubiaodeMacBook-Pro:src zlb$ redis-cli --latency  -h 172.16.50.192 -c
min: 7, max: 16, avg: 10.20 (101 samples)
min: 7, max: 16, avg: 10.32 (126 samples)
min: 7, max: 20, avg: 10.48 (152 samples)^C
zhenglubiaodeMacBook-Pro:src zlb$
```
```
zhenglubiaodeMacBook-Pro:bin zlb$ pwd
/Users/zlb/redis-stat/bin
zhenglubiaodeMacBook-Pro:bin zlb$ redis-stat 172.16.50.192:6379 172.16.50.192:6380 172.16.50.192:6381,172.16.50.193:6379 172.16.50.207:6379 172.16.50.207:6380 5
┌────────────────────────┬────────────────────┬────────────────────┬───────────────────────────────────────>
│                        │ 172.16.50.192:6379 │ 172.16.50.192:6380 │ 172.16.50.192:6381,172.16.50.193:6379 >
├────────────────────────┼────────────────────┼────────────────────┼───────────────────────────────────────>
│          redis_version │             4.0.10 │             4.0.10 │                                4.0.10 >
│             redis_mode │            cluster │            cluster │                               cluster >
│             process_id │              22749 │              31339 │                                 31347 >
│      uptime_in_seconds │           26713881 │           25408246 │                              25408238 >
│         uptime_in_days │                309 │                294 │                                   294 >
│                   role │             master │             master │                                master >
│       connected_slaves │                  1 │                  1 │                                     1 >
│            aof_enabled │                  1 │                  1 │                                     0 >
│ rdb_bgsave_in_progress │                  0 │                  1 │                                     0 >
│     rdb_last_save_time │         1566375271 │         1566375226 │                            1566375252 >
└────────────────────────┴────────────────────┴────────────────────┴───────────────────────────────────────>

┌────────┬──┬──┬─────┬───┬──────┬──────┬─────┬─────┬─────┬─────┬──────┬─────┬─────┬─────┐
     time us sy    cl bcl    mem    rss  keys cmd/s exp/s evt/s hit%/s hit/s mis/s aofcs
├────────┼──┼──┼─────┼───┼──────┼──────┼─────┼─────┼─────┼─────┼──────┼─────┼─────┼─────┤
 16:14:51  -  - 1.26k   0 1.56GB 2.17GB 5.85M     -     -     -      -     -     - 519MB
 16:14:56 16 18 1.26k   0 1.56GB 2.17GB 5.85M 8.10k  10.0     0   86.7 1.73k   265 521MB
 16:15:01 20 24 1.27k   0 1.56GB 2.17GB 5.85M 9.02k  9.19     0   85.7 1.78k   297 524MB
└────────┴──┴──┴─────┴───┴──────┴──────┴─────┴─────┴─────┴─────┴──────┴─────┴─────┴─────┘
```
#### pipeline VS lua
- pipeline 通过减少客户端与redis服务端的通信次数来实现降低往返延时时间，而且Pipeline 实现的原理是队列，而队列的原理是时先进先出，这样就保证数据的顺序性；一次性发送所有要处理的命令集合，redis必须在处理完所有命令前先缓存已处理命令的处理结果，所有命令处理完一次性返回；
- lua脚本可以高效读写数据，并且保持原子性，作为一条命令处理，在redis集群下一个redis lua脚本中所有要处理的redis-key需要在一个哈希槽中
- pipeline中也可以是lua脚本命令的集合

- lettuce redis集群下如果一个lua脚本中所有redis-key都在一个哈希槽中则可以执行成功，否则抛出错误
- 集群下jedis客户端不支持pipeline
```
java.lang.UnsupportedOperationException: Pipeline is currently not supported for JedisClusterConnection.
```
- 集群下lettuce客户端支持pipeline
```
nested exception is io.lettuce.core.RedisCommandExecutionException: CROSSSLOT Keys in request don't hash to the same slot
```
- jedis redis集群下不能执行EvalSha，抛出如下错误
- JedisClusterScriptingCommands
```
EvalSha is not supported in cluster environment.
```

## redis command

#### 连接
```
$ redis-cli -h host -p port -a password

$ redis-cli -h host -c
```
#### key
- keys集群下不同节点返回不同
```
172.16.50.185:6380>
172.16.50.185:6380> keys zlb*
(empty list or set)
172.16.50.185:6380>
172.16.50.185:6380>
172.16.50.185:6380> get zlb1
-> Redirected to slot [4078] located at 172.16.50.183:6379
"1"
172.16.50.183:6379> keys zlb*
1) "zlb1"
172.16.50.183:6379>
172.16.50.183:6379> get zlb2
-> Redirected to slot [16269] located at 172.16.50.183:6380
"2"
172.16.50.183:6380> keys zlb*
1) "zlb3"
2) "zlb2"
172.16.50.183:6380>
172.16.50.183:6380> get zlb3
"3"
172.16.50.183:6380>
172.16.50.183:6380> keys zlb*
1) "zlb3"
2) "zlb2"
172.16.50.183:6380>
172.16.50.183:6380> get zlb1
-> Redirected to slot [4078] located at 172.16.50.183:6379
"1"
172.16.50.183:6379>
172.16.50.183:6379> keys zlb*
1) "zlb1"
172.16.50.183:6379>
```
```
zhenglubiaodeMacBook-Pro:src zlb$ ./redis-cli -h 127.0.0.1 -p 6379 -a lbzheng keys zlb_* | xargs  ./redis-cli -h 127.0.0.1 -p 6379 -a lbzheng del
```
- 支持的glob样式模式：
    h?llo比赛hello，hallo和hxllo
    h*llo比赛hllo和heeeello
    h[ae]llo比赛hello和hallo,，但不hillo
    h[^e]llo比赛hallo，hbllo，...但不hello
    h[a-b]llo比赛hallo和hbllo
    \如果要逐字匹配，请使用以转义特殊字符
- scan ( SCAN 、 SSCAN 、 HSCAN 和 ZSCAN 四个命令的工作方式都非常相似，)
```
redis 127.0.0.1:6379> scan 0
1) "17"
2)  1) "key:12"
    2) "key:8"
    3) "key:4"
    4) "key:14"
    5) "key:16"
    6) "key:17"
    7) "key:15"
    8) "key:10"
    9) "key:3"
    10) "key:7"
    11) "key:1"

redis 127.0.0.1:6379> scan 17
1) "0"
2) 1) "key:5"
   2) "key:18"
   3) "key:0"
   4) "key:2"
   5) "key:19"
   6) "key:13"
   7) "key:6"
   8) "key:9"
   9) "key:11"
```
- 在上面这个例子中， 第一次迭代使用 0 作为游标， 表示开始一次新的迭代。

第二次迭代使用的是第一次迭代时返回的游标， 也即是命令回复第一个元素的值 —— 17 。

从上面的示例可以看到， SCAN 命令的回复是一个包含两个元素的数组， 第一个数组元素是用于进行下一次迭代的新游标， 而第二个数组元素则是一个数组， 这个数组中包含了所有被迭代的元素。

在第二次调用 SCAN 命令时， 命令返回了游标 0 ， 这表示迭代已经结束， 整个数据集（collection）已经被完整遍历过了。

以 0 作为游标开始一次新的迭代， 一直调用 SCAN 命令， 直到命令返回游标 0 ， 我们称这个过程为一次完整遍历（full iteration）。
```
72.16.50.183:6379>
172.16.50.183:6379> scan 0 match zset_2_* count 100000
1) "0"
2) (empty list or set)
172.16.50.183:6379>
172.16.50.183:6379> scan 0 count 5
1) "416"
2) 1) "market:spotKline:now:ETH:3:1556521200000"
   2) "spring:session:sessions:expires:fd6ea0ae-f9da-45eb-8b7f-ba8af33c5689"
   3) "margin:kline:connect:from:2013:2"
   4) "margin:kline:connect:from:2044:1"
   5) "FOTA_WEBSOCKET_ALL_ASSETCATEGORY"
172.16.50.183:6379>
172.16.50.183:6379>
```
```
172.16.50.185:6380> expire zlb1 500
-> Redirected to slot [4078] located at 172.16.50.183:6379
(integer) 1
172.16.50.183:6379>
172.16.50.183:6379> pexpire zlb1 60000
(integer) 1
172.16.50.183:6379>
172.16.50.183:6379> pttl zlb1
(integer) 48618
172.16.50.183:6379>
172.16.50.183:6379> ttl zlb1
(integer) 44
172.16.50.183:6379> persist zlb
-> Redirected to slot [7530] located at 172.16.50.185:6380
(integer) 0
172.16.50.185:6380> get zlb1
-> Redirected to slot [4078] located at 172.16.50.183:6379
"1"
172.16.50.183:6379> persist zlb1
(integer) 1
172.16.50.183:6379> ttl zlb1
(integer) -1
172.16.50.183:6379> type zlb1
string
172.16.50.183:6379>
```


#### string

```
172.16.50.183:6379> set zlb1 123456
OK
172.16.50.183:6379>
172.16.50.183:6379> getrange zlb1  0 2
"123"
172.16.50.183:6379> getset zlb1 4321
"123456"
172.16.50.183:6379>
172.16.50.183:6379> mget zlb1 zlb2 zlb3
(error) CROSSSLOT Keys in request don't hash to the same slot
172.16.50.183:6379>
172.16.50.183:6379>
172.16.50.183:6379>
172.16.50.183:6379>
172.16.50.183:6379> get zlb2
-> Redirected to slot [16269] located at 172.16.50.183:6380
"2"
172.16.50.183:6380> mget zlb2 zlb3
(error) CROSSSLOT Keys in request don't hash to the same slot
172.16.50.183:6380>
172.16.50.183:6380>
172.16.50.183:6380> set {zlb}zlb1 5
-> Redirected to slot [7530] located at 172.16.50.185:6380
OK
172.16.50.185:6380>
172.16.50.185:6380> set {zlb}zlb2 6
OK
172.16.50.185:6380> set {zlb}zlb3 7
OK
172.16.50.185:6380> mget {zlb}zlb1 {zlb}zlb2 {zlb}zlb3
1) "5"
2) "6"
3) "7"
172.16.50.185:6380>
172.16.50.185:6380>
172.16.50.185:6380>
172.16.50.185:6380> set testbit a
OK
172.16.50.185:6380> get testbit
"a"
172.16.50.185:6380> setbit testbit 6 1
(integer) 0
172.16.50.185:6380>
172.16.50.185:6380> setbit testbit 7 0
(integer) 1
172.16.50.185:6380> get testbit
"b"
172.16.50.185:6380>
172.16.50.185:6380>
```
```
172.16.50.183:6380>
172.16.50.183:6380> setex zlb 60 value
-> Redirected to slot [7530] located at 172.16.50.185:6380
OK
172.16.50.185:6380> ttl zlb
(integer) 55
172.16.50.185:6380> pttl zlb
(integer) 51015
172.16.50.185:6380>
172.16.50.185:6380> setnx zlb value
(integer) 0
172.16.50.185:6380> setnx zlb value
(integer) 0
172.16.50.185:6380> setnx zlb value
(integer) 0
172.16.50.185:6380> ttl zlb
(integer) 16
172.16.50.185:6380>
172.16.50.185:6380> setnx zlb value
(integer) 1
172.16.50.185:6380>
172.16.50.185:6380> strlen zlb
(integer) 5
172.16.50.185:6380> setrange zlb 1 bbb
(integer) 5
172.16.50.185:6380> get zlb
"vbbbe"
172.16.50.185:6380> setrange 1 c
(error) ERR wrong number of arguments for 'setrange' command
172.16.50.185:6380> setrange zlb 1 c
(integer) 5
172.16.50.185:6380> get zlb
"vcbbe"
172.16.50.185:6380> setrange 1 dddddddd
(error) ERR wrong number of arguments for 'setrange' command
172.16.50.185:6380> setrange zlb 1 dddddddd
(integer) 9
172.16.50.185:6380> get zlb
"vdddddddd"
172.16.50.185:6380>
172.16.50.185:6380> mset zlb1 1 zlb2 2 zbl3 3
(error) CROSSSLOT Keys in request don't hash to the same slot
172.16.50.185:6380>
172.16.50.185:6380> mset {aa}zlb1 1 {aa}zlb2 2 {cc}zbl3 3
(error) CROSSSLOT Keys in request don't hash to the same slot
172.16.50.185:6380> mset {aa}zlb1 1 {aa}zlb2 2 {aa}zbl3 3
-> Redirected to slot [1180] located at 172.16.50.183:6379
OK
172.16.50.183:6379>
172.16.50.183:6379> mget {aa}zlb1 {aa}zlb2 {aa}zbl3
1) "1"
2) "2"
3) "3"
172.16.50.183:6379>
172.16.50.183:6379>
172.16.50.183:6379> set zlb 0
-> Redirected to slot [7530] located at 172.16.50.185:6380
OK
172.16.50.185:6380> ince zlb
(error) ERR unknown command 'ince'
172.16.50.185:6380> incr zlb
(integer) 1
172.16.50.185:6380> incrby zlb 1
(integer) 2
172.16.50.185:6380> incrby zlb 1.1
(error) ERR value is not an integer or out of range
172.16.50.185:6380> incrbyfloat zlb 1.1
"3.1"
172.16.50.185:6380>
172.16.50.185:6380> decr zlb
(error) ERR value is not an integer or out of range
172.16.50.185:6380> decrby zlb 1
(error) ERR value is not an integer or out of range
172.16.50.185:6380>
172.16.50.185:6380> incrbyfloat zlb 0.9
"4"
172.16.50.185:6380>
172.16.50.185:6380> decr zlb
(integer) 3
172.16.50.185:6380> decrby zlb 2
(integer) 1
172.16.50.185:6380>
172.16.50.185:6380> set zlb "2"
OK
172.16.50.185:6380> incr zlb
(integer) 3
172.16.50.185:6380> get zlb
"3"
172.16.50.185:6380> set zlb 2
OK
172.16.50.185:6380> incr
(error) ERR wrong number of arguments for 'incr' command
172.16.50.185:6380> incr zlb
(integer) 3
172.16.50.185:6380> set zlb a
OK
172.16.50.185:6380> incr zlb
(error) ERR value is not an integer or out of range
172.16.50.185:6380> set zlb '1'
OK
172.16.50.185:6380> incr zlb
(integer) 2
172.16.50.185:6380>
172.16.50.185:6380> append zlb 00000
(integer) 6
172.16.50.185:6380> get zlb
"200000"
172.16.50.185:6380>
```


- LPUSH 向头部插入
- LPOP 从头部移除
- RPUSH 向尾部插入
- RPOP 从尾部移除
- LINDEX 索引0 为头部第一个元素
- 

```
172.16.50.183:6380>
172.16.50.183:6380> RPUSH list 1
(integer) 1
172.16.50.183:6380> RPUSH list 2
(integer) 2
172.16.50.183:6380> RPUSH list 3
(integer) 3
172.16.50.183:6380> RPUSH list 4
(integer) 4
172.16.50.183:6380> RPUSH list 5
(integer) 5
172.16.50.183:6380> RPUSH list 6
(integer) 6
172.16.50.183:6380>
172.16.50.183:6380> LPOP list
"1"
172.16.50.183:6380> LPOP list
"2"
172.16.50.183:6380> LPOP list
"3"
172.16.50.183:6380> LPOP list
"4"
172.16.50.183:6380> LPOP list
"5"
172.16.50.183:6380> LPOP list
"6"
172.16.50.183:6380> LPOP list
(nil)
172.16.50.183:6380>
```

```
172.16.50.184:6379>
172.16.50.184:6379> LPUSH "list" 1
-> Redirected to slot [12291] located at 172.16.50.183:6380
(integer) 1
172.16.50.183:6380> LPUSH "list" 2
(integer) 2
172.16.50.183:6380> LPUSH "list" 3
(integer) 3
172.16.50.183:6380>
172.16.50.183:6380> LPOP "list"
"3"
172.16.50.183:6380> LPOP "list"
"2"
172.16.50.183:6380> LPOP "list"
"1"
172.16.50.183:6380> LPOP "list"
(nil)
172.16.50.183:6380>
172.16.50.183:6380>
172.16.50.183:6380>
172.16.50.183:6380>
172.16.50.183:6380> RPUSH "list" 1
(integer) 1
172.16.50.183:6380> RPUSH "list" 2
(integer) 2
172.16.50.183:6380> RPUSH "list" 3
(integer) 3
172.16.50.183:6380>
172.16.50.183:6380> RPOP
(error) ERR wrong number of arguments for 'rpop' command
172.16.50.183:6380> RPOP "list"
"3"
172.16.50.183:6380> RPOP "list"
"2"
172.16.50.183:6380> RPOP "list"
"1"
172.16.50.183:6380>
172.16.50.183:6380> RPUSH "list" 1
(integer) 1
172.16.50.183:6380> RPUSH "list" 2
(integer) 2
172.16.50.183:6380> RPUSH "list" 3
(integer) 3
172.16.50.183:6380> LINDEX "list" 0
"1"
172.16.50.183:6380>
172.16.50.183:6380> LPUSH "list" 0
(integer) 4
172.16.50.183:6380> LINDEX "list" 0
"0"
172.16.50.183:6380> LPUSH "list" -1
(integer) 5
172.16.50.183:6380>
172.16.50.183:6380> LINDEX "list" 0
"-1"
172.16.50.183:6380>
```


### redis-lua

```
package com.fota.fotamarket.cache;

import com.alibaba.fastjson.JSON;
import com.fota.fotamarket.common.constant.RedisConstant;
import com.fota.market.domain.KLineCache;
import com.fota.market.domain.MarketSpotIndexKlineDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author taoyuanming
 * Created on 2018/7/7
 * Description RedisTemplate
 */
@Component
public class RedisCache {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisTemplate<String, KLineCache> redisTemplateKLine;

    @Autowired
    public RedisTemplate<String, MarketSpotIndexKlineDTO> redisTemplateSpotKLine;

    /**
     * 添加redis缓存
     *
     * @param key
     * @param value
     */
    public void saveValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param time
     * @param timeUnit
     */
    public void expireValue(String key, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().getOperations().expire(key, time, timeUnit);
    }

    /**
     * 设置具体日期过期
     *
     * @param key
     * @param date
     */
    public void expireOnDate(String key, Date date) {
        redisTemplate.opsForValue().getOperations().expireAt(key, date);
    }

    /**
     * 添加redis缓存并设置过期时间
     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     */
    public void saveValueOnExpire(String key, String value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 添加redis缓存并设置过期时间
     *
     * @param key
     * @param obj
     * @param time
     * @param timeUnit
     */
    public void saveObjectOnExpire(String key, Object obj, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), time, timeUnit);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void deleteValue(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * Set key to hold the string value if key is absent.
     *
     * @param key
     * @param value
     * @return 不存该key则保存该K-V并返回true，如果已存在该key则无法保存该K-V也不会修改该Key原来的value并返回false
     */
    public Boolean saveValueIfAbsent(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 通过key获取指定Object
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getObject(String key, Class<T> clazz) {
        return JSON.parseObject((String) redisTemplate.opsForValue().get(key), clazz);
    }

    /**
     * 通过key获取指定泛型的集合
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getArray(String key, Class<T> clazz) {
        return JSON.parseArray((String) redisTemplate.opsForValue().get(key), clazz);
    }

    /**
     * 通过key获取value
     *
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 保存交易数据已被处理的key
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean saveMatchedOrderhandledKey(String key, String value) {
        Boolean aBoolean = saveValueIfAbsent(key, value);
        if (aBoolean) {
            expireValue(key, RedisConstant.MQ_MATCHED_ORDER_VALID_HANDING_HOURS, TimeUnit.HOURS);
        }
        return aBoolean;
    }

    /**
     * 同步定时任务
     *
     * @param key      定时任务唯一标示作为redis的key 单独枚举类RedisKeyEnum
     * @param time     取定时任务执行的间隔时间的一半作为key的失效时间
     * @param timeUnit
     * @return 返回true则执行定时任务，返回false则不执行
     */
    public Boolean syncScheduledTask(String key, long time, TimeUnit timeUnit) {
        return setExpireIfAbsent(key, "", time, timeUnit);
    }

    /**
     * setIfAbsent，如果不存在该key即返回true则同时设置该key的过期时间
     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @return
     */
    public Boolean setExpireIfAbsent(String key, String value, long time, TimeUnit timeUnit) {
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, value);
        if (aBoolean) {
            redisTemplate.opsForValue().getOperations().expire(key, time, timeUnit);
        }
        return aBoolean;
    }


    public Boolean zSetAdd(String key, MarketSpotIndexKlineDTO kLineCache, double score) {
        Boolean aBoolean = redisTemplateSpotKLine.opsForZSet().add(key, kLineCache, score);
        redisTemplateSpotKLine.expire(key, 1, TimeUnit.HOURS);
        return aBoolean;
    }

    /**
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     *
     * @param key
     * @param kLineCache
     * @param score
     * @return
     */
    public Boolean zAdd(String key, KLineCache kLineCache, double score) {
        return redisTemplateKLine.opsForZSet().add(key, kLineCache, score);
    }

    /**
     * 通过索引区间返回有序集合指定区间内的成员
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<KLineCache> zRange(String key, long start, long end) {
        //start == end 则返回该索引的集合成员
        Set<KLineCache> set = redisTemplateKLine.opsForZSet().range(key, start, end);
        if (set == null) {
            set = new HashSet<>();
        }
        return set;
    }

    /**
     * 通过分数返回有序集合指定区间内的成员
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<KLineCache> zRangeByScore(String key, double start, double end) {
        //start == end 则返回该分数的集合成员
        Set<KLineCache> set = redisTemplateKLine.opsForZSet().rangeByScore(key, start, end);
        if (set == null) {
            set = new HashSet<>();
        }
        return set;
    }

    /**
     * 移除有序集合中给定的排名区间的所有成员
     *
     * @param key
     * @param start
     * @param end
     */
    public void zRemRangeByRank(String key, long start, long end) {
        redisTemplateKLine.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 移除有序集合中给定的分数区间的所有成员
     *
     * @param key
     * @param start
     * @param end
     */
    public void zRemRangeByScore(String key, double start, double end) {
        redisTemplateKLine.opsForZSet().removeRangeByScore(key, start, end);
    }

    /**
     * 清理历史K线记录
     *
     * @param key
     */
    public void zRemRangeByRankForKline(String key) {
        long reserve = RedisConstant.RESERVED_QUANTITY_AFTER_CLEAN_REDIS_HISTORY_KLINE_TASK;
        Long size = redisTemplateKLine.opsForZSet().zCard(key);
        long start = 0;
        long end = 0;
        if (size != null && size > reserve) {
            end = size - 1 - reserve;
        }
        //start == end == 0, 会删除索引为0的集合元素，此处不删除
        if (end != 0) {
            zRemRangeByRank(key, start, end);
            log.info("CleanRedisHistoryKLine: Key: {}, start: {}, end: {}", key, start, end);
        }
    }

    /**
     * 清理历史K线记录
     *
     * @param key
     */
    public void cleanByRankForKline(String key, long reserve) {
        Long size = redisTemplateKLine.opsForZSet().zCard(key);
        long start = 0;
        long end = 0;
        if (size != null && size > reserve) {
            end = size - 1 - reserve;
        }
        //start == end == 0, 会删除索引为0的集合元素，此处不删除
        if (end != 0) {
            zRemRangeByRank(key, start, end);
            log.info("CleanRedisHistoryKLine: Key: {}, start: {}, end: {}", key, start, end);
        }
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public String checkAndSetKline(String key, String dealPrice, String dealVolume, String dealTime, String dealAmount) {
        StringBuilder sb = new StringBuilder();
        sb.append(" local existFlag = redis.call('EXISTS',KEYS[1])");
        sb.append(" if existFlag == 0 then return '0' end");
        sb.append(" local value = redis.call('get',KEYS[1])");
        sb.append(" local kline = cjson.decode(cjson.decode(value))");
        sb.append(" local high = 'high'");
        sb.append(" local low = 'low'");
        sb.append(" local close = 'close'");
        sb.append(" local lastDealTime = 'lastDealTime'");
        sb.append(" local volume = 'volume'");
        sb.append(" local amount = 'amount'");
        sb.append(" local open = 'open'");
        sb.append(" local dealPrice = tonumber(ARGV[1])");
        sb.append(" local dealVolume = tonumber(ARGV[2])");
        sb.append(" local dealTime = tonumber(ARGV[3])");
        sb.append(" local dealAmount = tonumber(ARGV[4])");
        sb.append(" if kline[lastDealTime] == nil ")
                .append(" then kline[open] = dealPrice kline[high] = dealPrice kline[low] = dealPrice kline[close] = dealPrice kline[lastDealTime] = dealTime")
                .append(" else")
                .append(" if dealPrice > kline[high] then kline[high] = dealPrice end")
                .append(" if dealPrice < kline[low] then kline[low] = dealPrice end")
                .append(" if dealTime > kline[lastDealTime] then kline[close] = dealPrice kline[lastDealTime] = dealTime end")
                .append(" end");
        sb.append(" kline[volume] = kline[volume] + dealVolume");
        sb.append(" if kline[amount] == nil")
                .append(" then kline[amount] = dealAmount")
                .append(" else")
                .append(" kline[amount] = kline[amount] + dealAmount")
                .append(" end");
        sb.append(" local value2 = cjson.encode(cjson.encode(kline))");
        sb.append(" redis.call('set', KEYS[1], value2)");
        sb.append(" return '1'");
        RedisScript<String> luaScript = new DefaultRedisScript<>(sb.toString(), String.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        return redisTemplate.execute(luaScript, stringRedisSerializer, stringRedisSerializer, Collections.singletonList(key), dealPrice, dealVolume, dealTime, dealAmount);
    }
}


```

```


    /**
     * 更新redis K线
     * @param kLineRedisKey
     * @param dto
     * @return 不存在该key返回false 存在返回true
     */
    public boolean updateRedisKline(String kLineRedisKey, MatchedOrderKlineDTO dto) {
        String ret = redisCache.checkAndSetKline(kLineRedisKey, dto.getFilledPrice().toPlainString(), dto.getFilledAmount().toPlainString(), dto.getFilledDate().toString(), dto.getAmount().toPlainString());
        return "1".equals(ret);
    }
    
```

```
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RedisCacheTest {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MarketKlineManager marketKLineManager;

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    @Test
    public void saveValue() {

        Long klineId = 2019L;
        Integer resolution = 4;
        Long time = 1561075200000L;

        String kLineRedisKey2 = RedisUtils.getKLineRedisKey(String.valueOf(200001L), resolution, time);
        KLineCache kLineCache = JSON.parseObject((String) redisTemplate.opsForValue().get(kLineRedisKey2), KLineCache.class);

        String json = JSON.toJSONString(kLineCache);
        System.out.println(json);
        // {"amount":0,"close":4999.7,"high":4999.7,"klineId":200001,"klineName":"BTC","low":4999.7,"open":4999.7,"resolution":4,"time":1561075200000,"volume":0}

        kLineRedisKey2 = RedisUtils.getKLineRedisKey(String.valueOf(klineId), resolution, time);
        redisTemplate.opsForValue().set(kLineRedisKey2, json);


        MatchedOrderKlineDTO dto = new MatchedOrderKlineDTO();
        dto.setKlineId(klineId);
        dto.setResolution(resolution);
        dto.setTime(time);
        dto.setFilledPrice(new BigDecimal(300000));
        dto.setFilledAmount(new BigDecimal(300001));
        long time2 = System.currentTimeMillis();
        System.out.println(time2);
        dto.setFilledDate(time2);
        dto.setAmount(new BigDecimal(300002));

        boolean kLineRedisExist = marketKLineManager.updateRedisKline(kLineRedisKey2, dto);
        System.out.println(kLineRedisExist);
        kLineCache = JSON.parseObject((String) redisTemplate.opsForValue().get(kLineRedisKey2), KLineCache.class);
        System.out.println(JSON.toJSONString(kLineCache));
        // {"amount":300002,"close":300000,"high":300000,"klineId":200001,"klineName":"BTC","lastDealTime":1561105566653,"low":300000,"open":300000,"resolution":4,"time":1561075200000,"volume":300001}
    }
}
```

- redis spring 启动
```
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.boot.autoconfigure.data.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionConfiguration.ConnectionInfo;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass({RedisClient.class})
class LettuceConnectionConfiguration extends RedisConnectionConfiguration {
    private final RedisProperties properties;
    private final List<LettuceClientConfigurationBuilderCustomizer> builderCustomizers;

    LettuceConnectionConfiguration(RedisProperties properties, ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider, ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider, ObjectProvider<List<LettuceClientConfigurationBuilderCustomizer>> builderCustomizers) {
        super(properties, sentinelConfigurationProvider, clusterConfigurationProvider);
        this.properties = properties;
        this.builderCustomizers = (List)builderCustomizers.getIfAvailable(Collections::emptyList);
    }

    @Bean(
        destroyMethod = "shutdown"
    )
    @ConditionalOnMissingBean({ClientResources.class})
    public DefaultClientResources lettuceClientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    @ConditionalOnMissingBean({RedisConnectionFactory.class})
    public LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources) throws UnknownHostException {
        LettuceClientConfiguration clientConfig = this.getLettuceClientConfiguration(clientResources, this.properties.getLettuce().getPool());
        return this.createLettuceConnectionFactory(clientConfig);
    }

    private LettuceConnectionFactory createLettuceConnectionFactory(LettuceClientConfiguration clientConfiguration) {
        if (this.getSentinelConfig() != null) {
            return new LettuceConnectionFactory(this.getSentinelConfig(), clientConfiguration);
        } else {
            return this.getClusterConfiguration() != null ? new LettuceConnectionFactory(this.getClusterConfiguration(), clientConfiguration) : new LettuceConnectionFactory(this.getStandaloneConfig(), clientConfiguration);
        }
    }

    private LettuceClientConfiguration getLettuceClientConfiguration(ClientResources clientResources, Pool pool) {
        LettuceClientConfigurationBuilder builder = this.createBuilder(pool);
        this.applyProperties(builder);
        if (StringUtils.hasText(this.properties.getUrl())) {
            this.customizeConfigurationFromUrl(builder);
        }

        builder.clientResources(clientResources);
        this.customize(builder);
        return builder.build();
    }

    private LettuceClientConfigurationBuilder createBuilder(Pool pool) {
        return pool == null ? LettuceClientConfiguration.builder() : (new LettuceConnectionConfiguration.PoolBuilderFactory()).createBuilder(pool);
    }

    private LettuceClientConfigurationBuilder applyProperties(LettuceClientConfigurationBuilder builder) {
        if (this.properties.isSsl()) {
            builder.useSsl();
        }

        if (this.properties.getTimeout() != null) {
            builder.commandTimeout(this.properties.getTimeout());
        }

        if (this.properties.getLettuce() != null) {
            Lettuce lettuce = this.properties.getLettuce();
            if (lettuce.getShutdownTimeout() != null && !lettuce.getShutdownTimeout().isZero()) {
                builder.shutdownTimeout(this.properties.getLettuce().getShutdownTimeout());
            }
        }

        return builder;
    }

    private void customizeConfigurationFromUrl(LettuceClientConfigurationBuilder builder) {
        ConnectionInfo connectionInfo = this.parseUrl(this.properties.getUrl());
        if (connectionInfo.isUseSsl()) {
            builder.useSsl();
        }

    }

    private void customize(LettuceClientConfigurationBuilder builder) {
        Iterator var2 = this.builderCustomizers.iterator();

        while(var2.hasNext()) {
            LettuceClientConfigurationBuilderCustomizer customizer = (LettuceClientConfigurationBuilderCustomizer)var2.next();
            customizer.customize(builder);
        }

    }

    private static class PoolBuilderFactory {
        private PoolBuilderFactory() {
        }

        public LettuceClientConfigurationBuilder createBuilder(Pool properties) {
            return LettucePoolingClientConfiguration.builder().poolConfig(this.getPoolConfig(properties));
        }

        private GenericObjectPoolConfig getPoolConfig(Pool properties) {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(properties.getMaxActive());
            config.setMaxIdle(properties.getMaxIdle());
            config.setMinIdle(properties.getMinIdle());
            if (properties.getMaxWait() != null) {
                config.setMaxWaitMillis(properties.getMaxWait().toMillis());
            }

            return config;
        }
    }
}
```


- redis-stat
```



Last login: Fri Aug 16 16:28:12 on ttys003
zhenglubiaodeMacBook-Pro:~ zlb$ ruby -v
ruby 2.3.7p456 (2018-03-28 revision 63024) [universal.x86_64-darwin17]
zhenglubiaodeMacBook-Pro:~ zlb$ rubygems -v
-bash: rubygems: command not found
zhenglubiaodeMacBook-Pro:~ zlb$ gem -v
2.5.2.3
zhenglubiaodeMacBook-Pro:~ zlb$ git clone https://github.com/junegunn/redis-stat.git
Cloning into 'redis-stat'...

remote: Enumerating objects: 909, done.
remote: Total 909 (delta 0), reused 0 (delta 0), pack-reused 909
Receiving objects: 100% (909/909), 1.99 MiB | 17.00 KiB/s, done.
Resolving deltas: 100% (400/400), done.
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ gem install redis-stat
Fetching: ansi256-0.2.5.gem (100%)
ERROR:  While executing gem ... (Gem::FilePermissionError)
    You don't have write permissions for the /Library/Ruby/Gems/2.3.0 directory.
zhenglubiaodeMacBook-Pro:~ zlb$ sudo gem install redis-stat
Password:
Fetching: ansi256-0.2.5.gem (100%)
Successfully installed ansi256-0.2.5
Fetching: redis-3.0.7.gem (100%)
Successfully installed redis-3.0.7
Fetching: unicode-display_width-1.6.0.gem (100%)
Successfully installed unicode-display_width-1.6.0
Fetching: tabularize-0.2.10.gem (100%)
Successfully installed tabularize-0.2.10
Fetching: insensitive_hash-0.3.3.gem (100%)
Successfully installed insensitive_hash-0.3.3
Fetching: parallelize-0.4.1.gem (100%)
Successfully installed parallelize-0.4.1
Fetching: si-0.1.4.gem (100%)
Successfully installed si-0.1.4
Fetching: tilt-1.4.1.gem (100%)
Successfully installed tilt-1.4.1
Fetching: rack-1.6.11.gem (100%)
Successfully installed rack-1.6.11
Fetching: rack-protection-1.5.5.gem (100%)
Successfully installed rack-protection-1.5.5
Fetching: sinatra-1.3.6.gem (100%)
Successfully installed sinatra-1.3.6
Fetching: option_initializer-1.5.1.gem (100%)
Successfully installed option_initializer-1.5.1
Fetching: lps-0.2.1.gem (100%)
Successfully installed lps-0.2.1
Fetching: multi_json-1.13.1.gem (100%)
Successfully installed multi_json-1.13.1
Fetching: elasticsearch-api-1.0.18.gem (100%)
Successfully installed elasticsearch-api-1.0.18
Fetching: multipart-post-2.1.1.gem (100%)
Successfully installed multipart-post-2.1.1
Fetching: faraday-0.15.4.gem (100%)
Successfully installed faraday-0.15.4
Fetching: elasticsearch-transport-1.0.18.gem (100%)
Successfully installed elasticsearch-transport-1.0.18
Fetching: elasticsearch-1.0.18.gem (100%)
Successfully installed elasticsearch-1.0.18
Fetching: daemons-1.1.9.gem (100%)
Successfully installed daemons-1.1.9
Fetching: eventmachine-1.2.7.gem (100%)
Building native extensions.  This could take a while...
Successfully installed eventmachine-1.2.7
Fetching: thin-1.5.1.gem (100%)
Building native extensions.  This could take a while...
Successfully installed thin-1.5.1
Fetching: redis-stat-0.4.14.gem (100%)
Successfully installed redis-stat-0.4.14
Parsing documentation for ansi256-0.2.5
Installing ri documentation for ansi256-0.2.5
Parsing documentation for redis-3.0.7
Installing ri documentation for redis-3.0.7
Parsing documentation for unicode-display_width-1.6.0
Installing ri documentation for unicode-display_width-1.6.0
Parsing documentation for tabularize-0.2.10
Installing ri documentation for tabularize-0.2.10
Parsing documentation for insensitive_hash-0.3.3
Installing ri documentation for insensitive_hash-0.3.3
Parsing documentation for parallelize-0.4.1
Installing ri documentation for parallelize-0.4.1
Parsing documentation for si-0.1.4
Installing ri documentation for si-0.1.4
Parsing documentation for tilt-1.4.1
Installing ri documentation for tilt-1.4.1
Parsing documentation for rack-1.6.11
Installing ri documentation for rack-1.6.11
Parsing documentation for rack-protection-1.5.5
Installing ri documentation for rack-protection-1.5.5
Parsing documentation for sinatra-1.3.6
Installing ri documentation for sinatra-1.3.6
Parsing documentation for option_initializer-1.5.1
Installing ri documentation for option_initializer-1.5.1
Parsing documentation for lps-0.2.1
Installing ri documentation for lps-0.2.1
Parsing documentation for multi_json-1.13.1
Installing ri documentation for multi_json-1.13.1
Parsing documentation for elasticsearch-api-1.0.18
Installing ri documentation for elasticsearch-api-1.0.18
Parsing documentation for multipart-post-2.1.1
Installing ri documentation for multipart-post-2.1.1
Parsing documentation for faraday-0.15.4
Installing ri documentation for faraday-0.15.4
Parsing documentation for elasticsearch-transport-1.0.18
Installing ri documentation for elasticsearch-transport-1.0.18
Parsing documentation for elasticsearch-1.0.18
Installing ri documentation for elasticsearch-1.0.18
Parsing documentation for daemons-1.1.9
Installing ri documentation for daemons-1.1.9
Parsing documentation for eventmachine-1.2.7
Installing ri documentation for eventmachine-1.2.7
Parsing documentation for thin-1.5.1
Installing ri documentation for thin-1.5.1
Parsing documentation for redis-stat-0.4.14
Installing ri documentation for redis-stat-0.4.14
Done installing documentation for ansi256, redis, unicode-display_width, tabularize, insensitive_hash, parallelize, si, tilt, rack, rack-protection, sinatra, option_initializer, lps, multi_json, elasticsearch-api, multipart-post, faraday, elasticsearch-transport, elasticsearch, daemons, eventmachine, thin, redis-stat after 18 seconds
23 gems installed
zhenglubiaodeMacBook-Pro:~ zlb$ ./redis-stat --help
-bash: ./redis-stat: is a directory
zhenglubiaodeMacBook-Pro:~ zlb$ cd ./
.499481.padl                  .npm/                         eclipse-workspace/
.CFUserTextEncoding           .oracle_jre_usage/            go/
.DS_Store                     .p2/                          heap
.ShadowsocksX-NG/             .rediscli_history             java_error_in_idea_15026.log
.Trash/                       .rocketmq_offsets/            java_error_in_idea_20482.log
.android/                     .ssh/                         java_error_in_idea_21227.log
.arthas/                      .subversion/                  java_error_in_idea_24931.log
.bash_history                 .vim/                         java_error_in_idea_27673.log
.bash_profile                 .viminfo                      java_error_in_idea_28868.log
.bash_sessions/               .yarn/                        java_error_in_idea_42231.log
.config/                      .yarnrc                       java_error_in_idea_47267.log
.dubbo/                       Applications/                 java_error_in_idea_52290.log
.eclipse/                     Calibre 书库/                 java_error_in_idea_52539.log
.elastic-job-console/         Desktop/                      java_error_in_idea_54624.log
.gem/                         Documents/                    java_error_in_idea_5910.log
.gitconfig                    Downloads/                    java_error_in_idea_7009.log
.gradle/                      IdeaProjects/                 java_error_in_idea_90530.log
.m2/                          Library/                      java_error_in_idea_92495.log
.mycli-history                Movies/                       java_error_in_idea_9641.log
.mycli.log                    Music/                        jmeter.log
.myclirc                      Pictures/                     logs/
.mysql_history                Public/                       redis-stat/
.node-gyp/                    application/                  zookeeper.out
--More--
zhenglubiaodeMacBook-Pro:~ zlb$ cd ./redis-stat/
zhenglubiaodeMacBook-Pro:redis-stat zlb$ pwd
/Users/zlb/redis-stat
zhenglubiaodeMacBook-Pro:redis-stat zlb$
zhenglubiaodeMacBook-Pro:redis-stat zlb$ ls -l
total 40
-rw-r--r--  1 zlb  staff    95  8 16 17:14 Gemfile
-rw-r--r--  1 zlb  staff  1069  8 16 17:14 LICENSE
-rw-r--r--  1 zlb  staff  3661  8 16 17:14 README.md
-rw-r--r--  1 zlb  staff   202  8 16 17:14 Rakefile
drwxr-xr-x  3 zlb  staff    96  8 16 17:14 bin
drwxr-xr-x  4 zlb  staff   128  8 16 17:14 lib
-rw-r--r--  1 zlb  staff  1756  8 16 17:14 redis-stat.gemspec
drwxr-xr-x  6 zlb  staff   192  8 16 17:14 screenshots
drwxr-xr-x  4 zlb  staff   128  8 16 17:14 test
zhenglubiaodeMacBook-Pro:redis-stat zlb$ cd bin/
zhenglubiaodeMacBook-Pro:bin zlb$ ls -l
total 8
-rwxr-xr-x  1 zlb  staff  301  8 16 17:14 redis-stat
zhenglubiaodeMacBook-Pro:bin zlb$ ./redis-stat
[2019/08/16 17:20:06@127.0.0.1:6379] Error connecting to Redis on 127.0.0.1:6379 (ECONNREFUSED)
zhenglubiaodeMacBook-Pro:bin zlb$ ./redis-stat  --help
usage: redis-stat [HOST[:PORT] ...] [INTERVAL [COUNT]]

    -a, --auth=PASSWORD              Password
    -v, --verbose                    Show more info
        --style=STYLE                Output style: unicode|ascii
        --no-color                   Suppress ANSI color codes
        --csv[=CSV_FILE]             Print or save the result in CSV
        --es=ELASTICSEARCH_URL       Send results to ElasticSearch: [http://]HOST[:PORT][/INDEX]

        --server[=PORT]              Launch redis-stat web server (default port: 63790)
        --daemon                     Daemonize redis-stat. Must be used with --server option.

        --version                    Show version
        --help                       Show this message
zhenglubiaodeMacBook-Pro:bin zlb$
zhenglubiaodeMacBook-Pro:bin zlb$
zhenglubiaodeMacBook-Pro:bin zlb$ ./redis-stat 127.0.0.1:6379 5
┌────────────────────────┬────────────────┐
│                        │ 127.0.0.1:6379 │
├────────────────────────┼────────────────┤
│          redis_version │          3.0.6 │
│             redis_mode │     standalone │
│             process_id │          99094 │
│      uptime_in_seconds │             18 │
│         uptime_in_days │              0 │
│                   role │         master │
│       connected_slaves │              0 │
│            aof_enabled │              0 │
│ rdb_bgsave_in_progress │              0 │
│     rdb_last_save_time │     1565947261 │
└────────────────────────┴────────────────┘

┌────────┬──┬──┬──┬───┬──────┬──────┬────┬─────┬─────┬─────┬──────┬─────┬─────┬─────┐
     time us sy cl bcl    mem    rss keys cmd/s exp/s evt/s hit%/s hit/s mis/s aofcs
├────────┼──┼──┼──┼───┼──────┼──────┼────┼─────┼─────┼─────┼──────┼─────┼─────┼─────┤
 17:21:19  -  -  1   0 1019kB 2.07MB   38     -     -     -      -     -     -    0B
 17:21:24  0  0  1   0 1019kB 2.11MB   38  0.20     0     0      -     0     0    0B
 17:21:29  0  0  1   0 1019kB 1.19MB   38  0.20     0     0      -     0     0    0B
 17:21:34  0  0  1   0 1019kB 1.19MB   38  0.20     0     0      -     0     0    0B
 17:21:39  0  0  1   0 1019kB 1.20MB   38  0.20     0     0      -     0     0    0B
 17:21:44  0  0  1   0 1019kB 1.20MB   38  0.20     0     0      -     0     0    0B
 17:21:49  0  0  1   0 1019kB 1.21MB   38  0.20     0     0      -     0     0    0B
 17:21:54  0  0  1   0 1019kB 1.21MB   38  0.20     0     0      -     0     0    0B
 17:21:59  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:04  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:09  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:14  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:19  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:24  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:29  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:34  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:39  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:44  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:49  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:22:54  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
├────────┼──┼──┼──┼───┼──────┼──────┼────┼─────┼─────┼─────┼──────┼─────┼─────┼─────┤
     time us sy cl bcl    mem    rss keys cmd/s exp/s evt/s hit%/s hit/s mis/s aofcs
├────────┼──┼──┼──┼───┼──────┼──────┼────┼─────┼─────┼─────┼──────┼─────┼─────┼─────┤
 17:22:59  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:23:04  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:23:09  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
 17:23:14  0  0  1   0 1019kB 1.23MB   38  0.20     0     0      -     0     0    0B
└────────┴──┴──┴──┴───┴──────┴──────┴────┴─────┴─────┴─────┴──────┴─────┴─────┴─────┘

 17:23:19  0  0  1   0 1019kB 1.24MB   38  0.20     0     0      -     0     0    0B
└────────┴──┴──┴──┴───┴──────┴──────┴────┴─────┴─────┴─────┴──────┴─────┴─────┴─────┘^C
Interrupted.
Elapsed: 120.19 sec.
zhenglubiaodeMacBook-Pro:bin zlb$
zhenglubiaodeMacBook-Pro:bin zlb$
zhenglubiaodeMacBook-Pro:bin zlb$ ./redis-stat 172.16.50.192:6379,172.16.50.192:6380,172.16.50.192:6381,172.16.50.193:6379,172.16.50.207:6379,172.16.50.207:6380
zhenglubiaodeMacBook-Pro:bin zlb$ 172.16.50.192:6379 172.16.50.192:6380 172.16.50.192:6381 172.16.50.193:6379 172.16.50.207:6379 172.16.50.207:6380 8
-bash: 172.16.50.192:6379: command not found
zhenglubiaodeMacBook-Pro:bin zlb$ ./redis-stat 172.16.50.192:6379 172.16.50.192:6380 172.16.50.192:6381 172.16.50.193:6379 172.16.50.207:6379 172.16.50.207:6380 8
┌────────────────────────┬────────────────────┬────────────────────┬────────────────────┬────────────────────┬────────────────────>
│                        │ 172.16.50.192:6379 │ 172.16.50.192:6380 │ 172.16.50.192:6381 │ 172.16.50.193:6379 │ 172.16.50.207:6379 >
├────────────────────────┼────────────────────┼────────────────────┼────────────────────┼────────────────────┼────────────────────>
│          redis_version │             4.0.10 │             4.0.10 │             4.0.10 │             4.0.10 │             4.0.10 >
│             redis_mode │            cluster │            cluster │            cluster │            cluster │            cluster >
│             process_id │              22749 │              31339 │              31347 │              11860 │              16547 >
│      uptime_in_seconds │           26286088 │           24980453 │           24980445 │           27811158 │           17129633 >
│         uptime_in_days │                304 │                289 │                289 │                321 │                198 >
│                   role │             master │             master │             master │              slave │              slave >
│       connected_slaves │                  1 │                  1 │                  1 │                  0 │                  0 >
│            aof_enabled │                  1 │                  1 │                  0 │                  1 │                  0 >
│ rdb_bgsave_in_progress │                  0 │                  0 │                  0 │                  1 │                  0 >
│     rdb_last_save_time │         1565947447 │         1565947467 │         1565947468 │         1565947433 │         1565947480 >
└────────────────────────┴────────────────────┴────────────────────┴────────────────────┴────────────────────┴────────────────────>

┌────────┬──┬──┬───┬───┬──────┬──────┬─────┬─────┬─────┬─────┬──────┬─────┬─────┬─────┐
     time us sy  cl bcl    mem    rss  keys cmd/s exp/s evt/s hit%/s hit/s mis/s aofcs
├────────┼──┼──┼───┼───┼──────┼──────┼─────┼─────┼─────┼─────┼──────┼─────┼─────┼─────┤
 17:24:58  -  - 877   0 1.93GB 2.84GB 7.71M     -     -     -      -     -     - 778MB
 17:25:06 20 22 882   0 1.93GB 2.84GB 7.71M 6.81k  0.12     0   48.6   300   317 785MB
 17:25:14 19 21 883   0 1.93GB 2.84GB 7.71M 6.74k  0.12     0   47.8   299   326 792MB
└────────┴──┴──┴───┴───┴──────┴──────┴─────┴─────┴─────┴─────┴──────┴─────┴─────┴─────┘
 17:25:22 19 23 882   0 1.93GB 2.84GB 7.71M 7.60k     0     0     48   298   323 799MB
 17:25:30 21 23 885   0 1.93GB 2.84GB 7.71M 7.35k     0     0   49.2   292   301 805MB
└────────┴──┴──┴───┴───┴──────┴──────┴─────┴─────┴─────┴─────┴──────┴─────┴─────┴─────┘^C
Interrupted.
Elapsed: 35.25 sec.
zhenglubiaodeMacBook-Pro:bin zlb$
```

- 部分指标说明：  cl 连接客户端数量，bcl 阻塞客户端数量(如BLPOP)，exp/s 每秒过期key数量，evt/s 每秒淘汰key数量，aofcs AOF日志当前大小,我们看到，此款软件功能上与redis-cli –stat 类似，监控项目也更加全面，但是不如系统自动的工具来得快。



- 集群 持久化 分布式锁 单机或单机群  租约时间自动续租 (多个集群)联锁 红锁
- Redis实现可重入的分布式锁
```
通常我们为了实现 Redis 的高可用，一般都会搭建 Redis 的集群模式，比如给 Redis 节点挂载一个或多个 slave 从节点，然后采用哨兵模式进行主从切换。但由于 Redis 的主从模式是异步的，所以可能会在数据同步过程中，master 主节点宕机，slave 从节点来不及数据同步就被选举为 master 主节点，从而导致数据丢失，大致过程如下：

用户在 Redis 的 master 主节点上获取了锁；
master 主节点宕机了，存储锁的 key 还没有来得及同步到 slave 从节点上；
slave 从节点升级为 master 主节点；
用户从新的 master 主节点获取到了对应同一个资源的锁，同把锁获取两次。
ok，然后为了解决这个问题，Redis 作者提出了 RedLock 算法，步骤如下（五步）：

在下面的示例中，我们假设有 5 个完全独立的 Redis Master 节点，他们分别运行在 5 台服务器中，可以保证他们不会同时宕机。

获取当前 Unix 时间，以毫秒为单位。
依次尝试从 N 个实例，使用相同的 key 和随机值获取锁。在步骤 2，当向 Redis 设置锁时，客户端应该设置一个网络连接和响应超时时间，这个超时时间应该小于锁的失效时间。例如你的锁自动失效时间为 10 秒，则超时时间应该在 5-50 毫秒之间。这样可以避免服务器端 Redis 已经挂掉的情况下，客户端还在死死地等待响应结果。如果服务器端没有在规定时间内响应，客户端应该尽快尝试另外一个 Redis 实例。
客户端使用当前时间减去开始获取锁时间（步骤 1 记录的时间）就得到获取锁使用的时间。当且仅当从大多数（这里是 3 个节点）的 Redis 节点都取到锁，并且使用的时间小于锁失效时间时，锁才算获取成功。
如果取到了锁，key 的真正有效时间等于有效时间减去获取锁所使用的时间（步骤 3 计算的结果）。
如果因为某些原因，获取锁失败（没有在至少 N/2+1 个Redis实例取到锁或者取锁时间已经超过了有效时间），客户端应该在所有的 Redis 实例上进行解锁（即便某些 Redis 实例根本就没有加锁成功）。
到这，基本看出来，只要是大多数的 Redis 节点可以正常工作，就可以保证 Redlock 的正常工作。这样就可以解决前面单点 Redis 的情况下我们讨论的节点挂掉，由于异步通信，导致锁失效的问题。

但是细想后， Redlock 还是存在如下问题：

假设一共有5个Redis节点：A, B, C, D, E。设想发生了如下的事件序列：

客户端1 成功锁住了 A, B, C，获取锁成功（但 D 和 E 没有锁住）。
节点 C 崩溃重启了，但客户端1在 C 上加的锁没有持久化下来，丢失了。
节点 C 重启后，客户端2 锁住了 C, D, E，获取锁成功。
这样，客户端1 和 客户端2 同时获得了锁（针对同一资源）。
哎，还是不能解决故障重启后带来的锁的安全性问题...

针对节点重后引发的锁失效问题，Redis 作者又提出了 延迟重启 的概念，大致就是说，一个节点崩溃后，不要立刻重启他，而是等到一定的时间后再重启，等待的时间应该大于锁的过期时间，采用这种方式，就可以保证这个节点在重启前所参与的锁都过期，听上去感觉 延迟重启 解决了这个问题...

但是，还是有个问题，节点重启后，在等待的时间内，这个节点对外是不工作的。那么如果大多数节点都挂了，进入了等待，就会导致系统的不可用，因为系统在过期时间内任何锁都无法加锁成功...

巴拉巴拉那么多，关于 Redis 分布式锁的缺点显然进入了一个无解的步骤，包括后来的 神仙打架事件（Redis 作者 antirez 和 分布式领域专家 Martin Kleppmann）...

总之，首先我们要明确使用分布式锁的目的是什么？

无外乎就是保证同一时间内只有一个客户端可以对共享资源进行操作，也就是共享资源的原子性操作。

总之，在 Redis 分布式锁的实现上还有很多问题等待解决，我们需要认识到这些问题并清楚如何正确实现一个 Redis 分布式锁，然后在工作中合理的选择和正确的使用分布式锁。

目前我们项目中也有在用分布式锁，也有用到 Redis 实现分布式锁的场景，然后有的小伙伴就可能问，啊，你们就不怕出现上边提到的那种问题吗~

其实实现分布式锁，从中间件上来选，也有 Zookeeper 可选，并且 Zookeeper 可靠性比 Redis 强太多，但是效率是低了点，如果并发量不是特别大，追求可靠性，那么肯定首选 Zookeeper。

如果是为了效率，就首选 Redis 实现。


```