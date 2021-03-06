    
#### MySQL 的自增主键不单调也不连续
- 不单调
    - MySQL 5.7 版本之前在内存中存储 AUTO_INCREMENT 计数器，实例重启后会根据表中的数据重新设置，在删除记录后重启就可能出现重复的主键，该问题在 8.0 版本使用重做日志解决，保证了主键的单调性
    - 存在 主键为10，11的记录，然后删除主键为11的记录，此时AUTO_INCREMENT=11，mysql实例重启后会根据表中的数据重新设置AUTO_INCREMENT=10,出现重复的主键(11)，当被删除的主键被外部系统引用时才会影响数据的一致性
    - MySQL 8.0 中，AUTO_INCREMENT 计数器的初始化行为发生了改变，每次计数器的变化都会写入到系统的重做日志（Redo log）并在每个检查点存储在引擎私有的系统表中，当 MySQL 服务被重启或者处于崩溃恢复时，它可以从持久化的检查点和重做日志中恢复出最新的 AUTO_INCREMENT 计数器，避免出现不单调的主键同时也解决删除可能出现系统问题
    - In MySQL 8.0, this behavior is changed. The current maximum auto-increment counter value is written to the redo log each time it changes and is saved to an engine-private system table on each checkpoint. These changes make the current maximum auto-increment counter value persistent across server restarts
- 不连续
    - 并发事务
        - 事务 1 向数据库中插入 id = 10 的记录，事务 2 向数据库中插入 id = 11 和 id = 12 的两条记录，
        - 如果在最后事务 1 由于插入的记录发生了唯一键冲突导致了回滚，而事务 2 没有发生错误而正常提交，在这时会发现当前表中的主键出现了不连续的现象，后续新插入的数据也不再会使用 10 作为记录的主键
        - InnoDB 存储引擎提供的 innodb_autoinc_lock_mode 配置控制的，该配置决定了获取 AUTO_INCREMENT 计时器时需要先得到的锁，该配置存在三种不同的模式
            - 连续模式 innodb_autoinc_lock_mode = 1 默认
                - INSERT ... SELECT、REPLACE ... SELECT 以及 LOAD DATA 等批量的插入操作( Bulk inserts )需要获取表级别的 AUTO_INCREMENT 锁，该锁会在当前语句执行后释放
                - 简单的插入语句（预先知道插入多少条记录的语句）只需要获取获取 AUTO_INCREMENT 计数器的互斥锁并在获取主键后直接释放，不需要等待当前语句执行完成
                - 目的是保证 AUTO_INCREMENT 的获取不会导致线程竞争，而不是保证 MySQL 中主键的连续
                - MySQL 插入数据获取 AUTO_INCREMENT 时不会使用事务锁，而是会使用互斥锁，并发的插入事务可能出现部分字段冲突导致插入失败，想要保证主键的连续需要串行地执行插入语句
    - 解决
        - 并发 串行执行所有包含插入操作的事务，也就是使用数据库的最高隔离级别 —— 可串行化（Serialiable）
        - 无并发 完全串行的插入
    - 牺牲主键的连续性来支持数据的并发插入，最终提高了 MySQL 服务的吞吐量
                
#### MySQL 默认的存储引擎 InnoDB 使用 B+ 树来存储数据
- MySQL 跟 B+ 树没有直接的关系，真正与 B+ 树有关系的是 MySQL 的默认存储引擎 InnoDB，MySQL 中存储引擎的主要作用是负责数据的存储和提取
- 无论是表中的数据（主键索引）还是辅助索引最终都会使用 B+ 树来存储数据，其中前者在表中会以 <id, row> 的方式存储，而后者会以 <index, id> 的方式进行存储
    - 在主键索引中，id 是主键，我们能够通过 id 找到该行的全部列；
    - 在辅助索引中，索引中的几个列构成了键，我们能够通过索引中的列找到 id，如果有需要的话，可以再通过 id 找到当前数据行的全部内容
    - 对于 InnoDB 来说，所有的数据都是以键值对的方式存储的，键索引和辅助索引在存储数据时会将 id 和 index 作为键，将所有列和 id 作为键对应的值
- MySQL 作为 OLTP 的数据库不仅需要具备事务的处理能力，而且要保证数据的持久化并且能够有一定的实时数据查询能力
- 数据加载
    - 计算机在读写文件时会以页为单位将数据加载到内存中。页的大小可能会根据操作系统的不同而发生变化，不过在大多数的操作系统中，页的大小都是 4KB；获取操作系统上的页大小 getconf PAGE_SIZE
    - 在数据库中查询数据时，CPU 会发现当前数据位于磁盘而不是内存中，这时就会触发 I/O 操作将数据加载到内存中进行访问，数据的加载都是以页的维度进行加载的，然而将数据从磁盘读取到内存中所需要的成本是非常大的，普通磁盘（非 SSD）加载数据需要经过队列、寻道、旋转以及传输的这些过程，大概要花费 10ms 左右的时间
    - 可以使用 10ms 这个数量级对随机 I/O 占用的时间进行估算，随机 I/O 对于 MySQL 的查询性能影响会非常大，而顺序读取磁盘中的数据时速度可以达到 40MB/s，这两者的性能差距有几个数量级
- 范围查询和排序的性能
- B 树可以在非叶结点中存储数据， 由于所有的节点都可能包含目标数据，我们总是要从根节点向下遍历子树查找满足条件的数据行，这个特点带来了大量的随机 I/O，也是 B 树最大的性能问题
- B+ 树中就不存在这个问题了，因为所有的数据行都存储在叶节点中，而这些叶节点可以通过指针依次按顺序连接
- B+ 树遍历数据时可以直接在多个子节点之间进行跳转，这样能够节省大量的磁盘 I/O 时间，也不需要在不同层级的节点之间对数据进行拼接和排序；通过一个 B+ 树最左侧的叶子节点，我们可以像链表一样遍历整个树中的全部数据，我们也可以引入双向链表保证倒序遍历时的性能
- B 树能够在非叶节点中存储数据，但是这也导致在查询连续数据时可能会带来更多的随机 I/O，而 B+ 树的所有叶节点可以通过指针相互连接，能够减少顺序遍历时产生的额外随机 I/O
- B+ 树这种数据结构会虽然会增加每次查询深度从而增加整体的耗时，然而高度为 3 的 B+ 树就能够存储千万级别的数据，实践中 B+ 树的高度最多也就 4 或者 5，所以这并不是影响性能的根本问题


### doublewrite
### checkpoint



#### 各种查询模式
- 流式查询


#### - 查询(如等值查询、范围查询)时锁定的边界问题，即如何阻止插入(td) P281
### - 死锁检测采用深度优先算法(td)

- 如何避免死锁
- 如何解决死锁         

- 可以为一张表同时建 B+ 树和哈希构成的存储结构，这样不同类型的查询就可以选择相对更快的数据结构，但是会导致更新和删除时需要操作多份数据

#### 计算一张表存多少数据
- 在计算机中磁盘存储数据最小单元是扇区，一个扇区的大小是512字节，而文件系统（例如XFS/EXT4）他的最小单元是块，一个块的大小一般是4k，而对于InnoDB存储引擎也有自己的最小储存单元——页（Page），一个页的大小默认是16K
- InnoDB存储引擎的最小存储单元是页，页可以用于存放数据也可以用于存放键值+指针，在B+树中叶子节点存放数据，非叶子节点存放键值+指针
- 索引组织表通过非叶子节点的二分查找法以及指针确定数据在哪个页中，进而在去数据页中查找到需要的数据
- InnoDB存储引擎一个页大小等于B+树一个节点大小
- 假设主键ID为bigint类型，长度为8字节，而指针大小在InnoDB源码中设置为6字节，这样一共14字节，我们一个页中能存放多少这样的单元，其实就代表有多少指针，即16384/14=1170
- 假设一行记录的数据大小为1k, 单个叶子节点（页）中的记录数=16K/1K=16
- 那么可以算出一棵高度为2的B+树，能存放1170*16=18720条这样的数据记录
- 高度为3的B+树可以存放：1170*1170*16=21902400条这样的记录
- 在InnoDB中B+树高度一般为1-3层，它就能满足千万级的数据存储。在查找数据时一次页的查找代表一次IO，所以通过主键索引查询通常只需要1-3次IO操作即可查找到数据。
   
   
#### WAL
- 预写式日志 Write-ahead logging，缩写 WAL
- WAL是关系数据库系统中用于提供原子性和持久性的一系列技术, 在使用 WAL 的系统中，所有的修改在提交之前都要先写入 log 文件中, log 文件中通常包括 redo 和 undo 信息
- WAL 机制的原理：修改并不直接写入到数据库文件中，而是写入到另外一个称为 WAL 的文件中；如果事务失败，WAL 中的记录会被忽略，撤销修改；如果事务成功，它将在随后的某个时间被写回到数据库文件中，提交修改。
- 磁盘的读写性能提升
    - 随机读写改顺序读写
    - 缓冲单条读写改批量读写
    - 单线程读写改并发读写
    - WAL
        - 一方面 WAL 中记录事务的更新内容，通过 WAL 将随机的脏页写入变成顺序的日志刷盘
        - 一方面，WAL 通过 buffer 的方式改单条磁盘刷入为缓冲批量刷盘
        - 再者从 WAL 数据到最终数据的同步过程中可以采用并发同步的方式
- checkpoint
    - 同步 WAL 文件和数据库文件的行为被称为 checkpoint（检查点），一般在 WAL 文件积累到一定页数修改的时候；当然，有些系统也可以手动执行 checkpoint。执行 checkpoint 之后，WAL 文件可以被清空，这样可以保证 WAL 文件不会因为太大而性能下降
    - 有些数据库系统读取请求也可以使用 WAL，通过读取 WAL 最新日志就可以获取到数据的最新状态
- 常见的数据库一般都会用到 WAL 机制，只是不同的系统说法和实现可能有所差异。mysql、sqlite、postgresql、etcd、hbase、zookeeper、elasticsearch 等等都有自己的实现
- mysql
    - mysql 通过 redo、undo 日志实现 WAL    

### redo log
- 重做日志中记录的是对页的物理操作，如偏移量800，写'aaa'记录；
- 可以提供事务的持久性
- 当前事务提交时，先写重做日志 redo log，再修改页(内存缓存池中数据页)。这样在掉电的时候，未刷盘的缓存池中数据页可以通过重做日志恢复
- redo log也会缓存(重做日志缓存)且会刷盘(重做日志文件)
- 重做日志块，重做日志以重做日志块(512字节)进行存储，大小和磁盘扇区大小一样，因此重做日志的写入可以保证原子性，不需要doublewrite技术 ？？
- checkpoint
    - 缓存池中数据页 脏页刷新到磁盘
- 需要checkpoint的原因
    - redo log 大小固定，采用循环写的方式，重做日志写满不可用就要刷新缓存池脏页(数据页)
    - innodb内存缓存池大小有限，缓冲池不够用的时候，就要刷新缓存池脏页(数据页)到磁盘
- redo log在数据库存储引擎层 当前库(主库)
- binlog在数据库server层 可以用于备份同步(从库)
- innodb_flush_log_at_trx_commit
    - 0 每秒刷盘
    - 1 每次事务提交刷盘
    - 2 每次事务提交写到OS缓存，然后每秒刷盘
### undo log
- undo log是把所有没有COMMIT的事务回滚到事务开始前的状态，系统崩溃时，可能有些事务还没有COMMIT，在系统恢复时，这些没有COMMIT的事务就需要借助undo log来进行回滚。
使用undo log时事务执行顺序
1. 记录START T 
2. 记录需要修改的记录的旧值（要求持久化）
3. 根据事务的需要更新数据库（要求持久化）
4. 记录COMMIT T 


- 可以提供事务的原子性
- 作用
    - 事务的回滚
        - undo log是逻辑日志，逻辑地恢复到原来的样子
        - 反向操作
    - MVCC
        - MVCC的实现是通过undo来完成
        - 通过undo读取之前的行版本信息 以此实现非锁定读
- 对数据库进行修改时，会产生redo，也会产生undo
- undo记录中存储的是老版本数据，当一个旧的事务需要读取数据时，为了能读取到老版本的数据，需要顺着undo链找到满足其可见性的记录
- undo log的产生会伴随着redo log的产生，因为undo log也需要持久性的保护

### binlog
- sync_binlog
    - 1 每次binlog日志文件写入后与磁盘同步
    - N 每N次binlog日志文件写入后与磁盘同步
### redo log 和 binlog的两阶段提交 
- prepare
    - redolog xid redolog的prepare标识
- commit
    - binlog xid redolog的commit标识
- 重启恢复
    - redolog有commit标识，提交事务
    - redolog只有prepare标识
        - binlog 有该xid事务 提交
        - binlog 无该xid事务 回滚 
```
prepare：redolog写入log buffer，并fsync持久化到磁盘，在redolog事务中记录2PC的XID，在redolog事务打上prepare标识
commit：binlog写入log buffer，并fsync持久化到磁盘，在binlog事务中记录2PC的XID，同时在redolog事务打上commit标识
其中，prepare和commit阶段所提到的“事务”，都是指内部XA事务，即2P


redolog中的事务如果经历了二阶段提交中的prepare阶段，则会打上prepare标识，如果经历commit阶段，则会打上commit标识（此时redolog和binlog均已落盘）。

Step1. 按顺序扫描redolog，如果redolog中的事务既有prepare标识，又有commit标识，就直接提交（复制redolog disk中的数据页到磁盘数据页）

Step2 .如果redolog事务只有prepare标识，没有commit标识，则说明当前事务在commit阶段crash了，binlog中当前事务是否完整未可知，此时拿着redolog中当前事务的XID（redolog和binlog中事务落盘的标识），去查看binlog中是否存在此XID

​ a. 如果binlog中有当前事务的XID，则提交事务（复制redolog disk中的数据页到磁盘数据页）

​ b. 如果binlog中没有当前事务的XID，则回滚事务（使用undolog来删除redolog中的对应事务
```
#### Mysql Join算法原理

- Simple Nested-Loop Join（简单的嵌套循环连接）
    - 简单来说嵌套循环连接算法就是一个双层for 循环 ，通过循环外层表的行数据，逐个与内层表的所有行数据进行比较来获取结     
    - 如果每个表有1万条数据，那么对数据比较的次数=1万 * 1万 =1亿次，很显然这种查询效率会非常慢
- Index Nested-Loop Join（索引嵌套循环连接）
    - 优化的思路是减少内层表数据的匹配次数
        - 循环匹配次数角度
    - 通过外层表匹配条件直接与内层表索引进行匹配，避免和内层表的每条记录去进行比较， 这样极大的减少了对内层表的匹配次数，从原来的匹配次数=外层表行数 * 内层表行数,变成了 外层表的行数 * 内层表索引的高度
    - 使用Index Nested-Loop Join 算法的前提是匹配的字段必须建立了索引
        - select * from tb1 left join tb2 on tb1.id=tb2.user_id 当tb2表的 user_id 有索引的时候
- Block Nested-Loop Join（缓存块嵌套循环连接）
    - 优化思路是减少内层表的**扫表**次数
        - IO 次数角度
        - Simple Nested-Loop Join 左表的每一条记录都会对右表进行一次扫表，扫表的过程其实也就是从内存读取数据的过程
        - 通过一次性缓存外层表的多条数据，通批量匹配的方式来减少内层表的扫表IO次数
        - 无法使用Index Nested-Loop Join的时候，数据库是默认使用的是Block Nested-Loop Join算法的
            - select * from tb1 left join tb2 on tb1.id=tb2.user_id 当tb2表的 user_id 无索引的时候
    - optimizer_switch的设置block_nested_loop为on 
        - 默认为开启
    - join_buffer_size参数可设置join buffer的大小
        - 缓存外层表的多条数据
- JOIN 查询的优化思路
    - 用小结果集驱动大结果集(其本质就是减少外层循环次数)
    
    - 为匹配的条件增加索引(减少内层表的循环匹配次数)
    
    - 增大join buffer size的大小（一次缓存的数据越多，那么内层包的扫表次数就越少）
    - 减少不必要的字段查询（字段越少，join buffer 所缓存的数据就越多）
- 驱动表的定义：当进行多表连接查询时，1.指定了联接条件时，满足查询条件的记录行数少的表为驱动表，2.未指定联接条件时，行数少的表为驱动表
    - 访问驱动表的次数更少了
- MySQL 表关联的算法是 Nest Loop Join，是通过驱动表的结果集作为循环基础数据，然后一条一条地通过该结果集中的数据作为过滤条件到下一个表中查询数据，然后合并结果
    
    
- 数据库连接池 参数
    - druid
    - 连接池(private volatile DruidConnectionHolder[] connections;)中数据库连接使用完 再获取连接会等待

- mysql 最大连接数
    ```
    mysql>
    mysql> show variables like '%max_connections%';
    +------------------------+-------+
    | Variable_name          | Value |
    +------------------------+-------+
    | max_connections        | 151   |
    | mysqlx_max_connections | 100   |
    +------------------------+-------+
    2 rows in set (0.00 sec)
    
    mysql>
    mysql>
    ```
    - “Can not connect to MySQL server. Too many connections”-mysql 1040错误，访问MySQL且还未释放的连接数目已经达到MySQL的上限
- 机器最大tcp连接数(服务端接受连接)
    - Linux每维护一条TCP连接都要花费资源。处理连接请求，保活，数据的收发时需要消耗一些CPU，维持TCP连接主要消耗内存
    - TCP在静止的状态下，就不怎么消耗CPU了，主要消耗内存。而Linux上内存是有限的；
        - 一条TCP连接如果不发送数据的话，消耗内存是3.3K左右。
    - 如果有数据发送，需要为每条TCP分配发送缓存区，大小受你的参数net.ipv4.tcp_wmem配置影响，默认情况下最小是4K。
    - 最大并发数取决你的内存大小
    
    
### 数据库连接池原理

- 不管on上的条件是否为真都会返回left或right表中的记录，full则具有left和right的特性的并集。而inner jion没这个特殊性，则条件放在on中和where中，返回的结果集是相同的

可以总结数据库为了解决 partial write问题，一般有4种手段：
事后恢复：innodb doublewirite 机制，事先存一份page的副本，当partial write发生需要恢复时，先通过page的副本来还原该page，再进行重做；
事后恢复：物理redo log 恢复机制，物理redo log里面存有完整的数据page，当partial write发生需要恢复时，先通过redo log page的副本来还原该page，再进行重做可以保证幂等性；
事先避免：底层存储来实现原子写入避免partial write；
事先避免：数据库的page size 设置为块设备扇区大小512字节保证原子写避免partial write，如：innodb redo log 。

RocksDB & InfluxDB

存储引擎采用LSM或者TSM（类LSM）的结构，数据page采用append only方式写入，而不是像innodb或PG一样采用in-place update的方式写入page，所以即使出现了partial write，由于原page没有变更，可以通过原page重做wal log恢复来保证page的完整性



- InnoDB read-ahead
    - InnoDB 提供了两种预读的方式，一种是 Linear read ahead，由参数innodb_read_ahead_threshold控制，当你连续读取一个 extent 的 threshold 个 page 的时候，会触发下一个 extent 64个page的预读。另外一种是Random read-ahead，由参数innodb_random_read_ahead控制，当你连续读取设定的数量的page后，会触发读取这个extent的剩余page。

### NULL & 索引
- 可为null字段
    - 当某一列有为null值的数据时，该列的索引依然生效
    - 使用is null确实是走了索引，没有问题。
    - 现象比较特殊，在这里is not null条件并没有走索引，但是修改成select a from j_copy的话，就可以走索引了。一个比较靠谱的说法：select a from j_copy直接读取索引上的数据后即可返回。而select * from j_copy如果走索引的话，则需要通过索引获取数据位置再去读取整行内容，在数据比较少的情况下，可能会更慢，所以mysql的优化器选择了直接全表扫描。
- 字段not null的情况下，is null和is not null都不会走索引