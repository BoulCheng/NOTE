
####[InnoDB On-Disk Structures](https://dev.mysql.com/doc/refman/8.0/en/innodb-index-types.html)
- Indexes
    - Clustered and Secondary Indexes
        - Every InnoDB table has a special index called the clustered index where the data for the rows is stored. Typically, the clustered index is synonymous with the primary key
            - To get the best performance from queries, inserts, and other database operations, you must understand how InnoDB uses the clustered index to optimize the most common lookup and DML operations for each table.
        - All indexes other than the clustered index are known as secondary indexes
            - In InnoDB, each record in a secondary index contains the primary key columns for the row, as well as the columns specified for the secondary index. InnoDB uses this primary key value to search for the row in the clustered index.
            - If the primary key is long, the secondary indexes use more space, so it is advantageous to have a short primary key.


#### [InnoDB Locking and Transaction Model](https://dev.mysql.com/doc/refman/8.0/en/innodb-locking-transaction-model.html)

- InnoDB Locking
    - Insert Intention Locks
        - An insert intention lock is a type of gap lock set by INSERT operations prior to row insertion.
        - This lock signals the intent to insert in such a way that multiple transactions inserting into the same index gap need not wait for each other if they are not inserting at the same position within the gap.
            - Suppose that there are index records with values of 4 and 7. Separate transactions that attempt to insert values of 5 and 6, respectively, each lock the gap between 4 and 7 with insert intention locks prior to obtaining the exclusive lock on the inserted row, but do not block each other because the rows are nonconflicting. 
- InnoDB Transaction Model
    - Consistent Nonlocking Reads
        - A [consistent read](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_consistent_read) means that InnoDB uses multi-versioning to present to a query a snapshot of the database at a point in time. The query sees the changes made by transactions that committed before that point of time, and no changes made by later or uncommitted transactions. The exception to this rule is that the query sees the changes made by earlier statements within the same transaction.
        - Consistent read is the default mode in which InnoDB processes SELECT statements in READ COMMITTED and REPEATABLE READ isolation levels
            - If the transaction isolation level is REPEATABLE READ (the default level), all consistent reads within the same transaction read the snapshot established by the first such read in that transaction. You can get a fresher snapshot for your queries by committing the current transaction and after that issuing new queries.
            - With READ COMMITTED isolation level, each consistent read within a transaction sets and reads its own fresh snapshot.
        - a timepoint of transaction     
            - Suppose that you are running in the default REPEATABLE READ isolation level. When you issue a consistent read (that is, an ordinary SELECT statement), InnoDB gives your transaction a timepoint according to which your query sees the database. If another transaction deletes a row and commits after your timepoint was assigned, you do not see the row as having been deleted. Inserts and updates are treated similarly.
            - You can advance your timepoint by committing your transaction and then doing another SELECT or START TRANSACTION WITH CONSISTENT SNAPSHOT.
            - BEGIN\START TRANSACTION\START TRANSACTION WITH CONSISTENT SNAPSHOT\autocommit\
                - [https://dev.mysql.com/doc/refman/8.0/en/commit.html]
            - autocommit
                - By default, MySQL runs with autocommit mode enabled. This means that, when not otherwise inside a transaction, each statement is atomic, as if it were surrounded by START TRANSACTION and COMMIT. You cannot use ROLLBACK to undo the effect; however, if an error occurs during statement execution, the statement is rolled back.
                - To disable autocommit mode implicitly for a single series of statements, use the START TRANSACTION statement:
                - With START TRANSACTION, autocommit remains disabled until you end the transaction with COMMIT or ROLLBACK. The autocommit mode then reverts to its previous state.
        - This is called multi-versioned concurrency control.    
    - Locking Reads
        - locking reads (SELECT with FOR UPDATE or FOR SHARE),

- Locks Set by Different SQL Statements in InnoDB
    - A locking read, an UPDATE, or a DELETE generally set record locks on every index record that is scanned in the processing of the SQL statement. It does not matter whether there are WHERE conditions in the statement that would exclude the row.
        - The locks are normally next-key locks that also block inserts into the “gap” immediately before the record.
        - todo 
            - 如何定义scanned ？ every index record that is scanned in the processing of the SQL statement
    - If a secondary index is used in a search and index record locks to be set are exclusive, InnoDB also retrieves the corresponding clustered index records and sets locks on them.
        - 二级索引的排他锁会导致该行对应的聚簇索引被锁
        - todo
            - 共享锁呢？
    - If you have no indexes suitable for your statement and MySQL must scan the entire table to process the statement, every row of the table becomes locked, which in turn blocks all inserts by other users to the table
        - It is important to create good indexes so that your queries do not unnecessarily scan many rows.
        - 应该是因为没有索引树 锁是基于索引树的 所以这里只能锁整张表
    - InnoDB sets specific types of locks as follows.
        - SELECT ... FROM is a consistent read, reading a snapshot of the database and setting no locks unless the transaction isolation level is set to SERIALIZABLE
        - SELECT ... FOR UPDATE and SELECT ... FOR SHARE statements that use a unique index acquire locks for scanned rows, and release the locks for rows that do not qualify for inclusion in the result set (for example, if they do not meet the criteria given in the WHERE clause)
            - However, in some cases, rows might not be unlocked immediately because the relationship between a result row and its original source is lost during query execution. For example, in a UNION,
        - For locking reads (SELECT with FOR UPDATE or FOR SHARE), UPDATE, and DELETE statements, the locks that are taken depend on whether the statement uses a unique index with a unique search condition, or a range-type search condition.
            - For a unique index with a unique search condition, InnoDB locks only the index record found, not the gap before it.
                - 二级索引的排他锁会导致该行对应的聚簇索引被锁  
            - For other search conditions, and for non-unique indexes, InnoDB locks the index range scanned, using gap locks or next-key locks to block insertions by other sessions into the gaps covered by the range.
        - UPDATE ... WHERE ... sets an exclusive next-key lock on every record the search encounters. However, only an index record lock is required for statements that lock rows using a unique index to search for a unique row.
            - When UPDATE modifies a clustered index record, implicit locks are taken on affected secondary index records. The UPDATE operation also takes shared locks on affected secondary index records when performing duplicate check scans prior to inserting new secondary index records, and when inserting new secondary index records.
        - INSERT ... ON DUPLICATE KEY UPDATE differs from a simple INSERT in that an exclusive lock rather than a shared lock is placed on the row to be updated when a duplicate-key error occurs. An exclusive index-record lock is taken for a duplicate primary key value. An exclusive next-key lock is taken for a duplicate unique key value
        - INSERT 
            - If a duplicate-key error occurs, a shared lock on the duplicate index record is set. This use of a shared lock can result in deadlock should there be multiple sessions trying to insert the same row if another session already has an exclusive lock. This can occur if another session deletes the row.
                - a duplicate-key error todo ？       
                - request a shared lock for the row
        - LOCK TABLES sets table locks, but it is the higher MySQL layer above the InnoDB layer that sets these locks. InnoDB is aware of table locks if innodb_table_locks = 1 (the default) and autocommit = 0, and the MySQL layer above InnoDB knows about row-level locks.
        - LOCK TABLES acquires two locks on each table if innodb_table_locks=1 (the default). In addition to a table lock on the MySQL layer, it also acquires an InnoDB table lock.  
        - All InnoDB locks held by a transaction are released when the transaction is committed or aborted. Thus, it does not make much sense to invoke LOCK TABLES on InnoDB tables in autocommit=1 mode because the acquired InnoDB table locks would be released immediately.
        - 当前读：特殊的读操作，插入/更新/删除操作，属于当前读，需要加锁。
            SELECT ... LOCK IN SHARE MODE 语句为当前读，加 S 锁；
            SELECT ... FOR UPDATE 语句为当前读，加 X 锁；
            常见的 DML 语句（如 INSERT、DELETE、UPDATE）为当前读，加 X 锁；
            都属于当前读，读取记录的最新版本。并且，读取之后，还需要保证其他并发事务不能修改当前记录，对读取记录加锁。(暂定)


##### 锁 [https://dev.mysql.com/doc/refman/5.7/en/innodb-locking.html]
- 多粒度锁定
    - 允许事务在行级上的锁和表级上的锁同时存在
    - 意向锁
        - 为了支持多粒度锁定
        - 不与行级锁冲突(不兼容)的表级锁
            - [https://dev.mysql.com/doc/refman/5.7/en/innodb-locking.html#innodb-intention-locks]
        - 揭示一个事务接下来对表中一(多)行将要请求的(行)锁类型(shared or exclusive)
            - Intention locks are table-level locks that indicate which type of lock (shared or exclusive) a transaction requires later for a row in a table
            - 在为数据行加共享 / 排他锁之前，会先获取该数据行所在在数据表的对应意向锁
        - 如果另一个任务试图在该表级别上应用共享或排它锁，则受到由第一个任务控制的表级别意向锁的阻塞。第二个任务在锁定该表前不必检查各个页或行锁，而只需检查表上的意向锁 
            - 如事务在试图对表加共享锁的时候，必须保证当前没有其他事务持有该表的排他锁且当前没有其他事务持有该表中任意一行的排他锁，为了检测是否满足后者，事务必须在确保该表不存在任何排他锁的前提下，去检测表中的每一行是否存在排他锁。但是有了意向锁可以直接检查意向锁
        - Intention locks do not block anything except full table requests (for example, LOCK TABLES ... WRITE). The main purpose of intention locks is to show that someone is locking a row, or going to lock a row in the table
- 表锁
    - 哪些情况会产生
        -  LOCK TABLES ... WRITE takes an exclusive lock (an X lock) on the specified table
```
	    X	        IX	        S	        IS
X	Conflict	Conflict	Conflict	Conflict
IX	Conflict	Compatible	Conflict	Compatible
S	Conflict	Conflict	Compatible	Compatible
IS	Conflict	Compatible	Compatible	Compatible

```
- 行锁
    - InnoDB performs row-level locking in such a way that when it searches or scans a table index, it sets shared or exclusive locks on the index records it encounters. Thus, the row-level locks are actually index-record locks    
- 锁算法
    - Record Lock
        - 读提交隔离级别下
        - 锁住索引记录
    - Gap Lock
        - are used in some transaction isolation levels and not others. 
            - 可重复读隔离级别下
            - disabled
                -  change the transaction isolation level to READ COMMITTED
        - Gap locking is not needed for statements that lock rows using a unique index to search for a unique row. (This does not include the case that the search condition includes only some columns of a multiple-column unique index; in that case, gap locking does occur.) 
        - only purpose is to prevent other transactions from inserting to the gap
            - 比如P268，间隙锁锁定 (3, 6)，那么在索引的B+树节点键值3到6之间就不能再插入新的节点
        - There is no difference between shared and exclusive gap locks. They do not conflict with each other, and they perform the same function
        - 插入意向锁
            - 插入意向锁是在插入一条记录行前，由 INSERT 操作产生的一种间隙锁。该锁用以表示插入意向，当多个事务在同一区间（gap）插入位置不同的多条数据时，事务之间不需要互相等待
            - 插入意向锁之间互不排斥，所以即使多个事务在同一区间插入多条记录，只要记录本身（主键、唯一索引）不冲突，那么事务之间就不会出现冲突等待
            - 插入意向锁中含有意向锁三个字，但是它并不属于意向锁而属于间隙锁，意向锁是表锁而插入意向锁是行锁
            - 解决并发插入，如果只有间隙锁并发插入性能差
            - INSERT sets an exclusive lock on the inserted row. This lock is an index-record lock, not a next-key lock (that is, there is no gap lock) and does not prevent other sessions from inserting into the gap before the inserted row.
    - Next-Key Lock
        - A next-key lock is a combination of a record lock on the index record and a gap lock on the gap before the index record.
        - a next-key lock is an index-record lock plus a gap lock on the gap preceding the index record.
            - A next-key lock on an index record also affects the “gap” before that index record 
            -  If one session has a shared or exclusive (Next-key td)lock on record R in an index, another session cannot insert a new index record in the gap immediately before R in the index order
            - Suppose that an index contains the values 10, 11, 13, and 20. The possible next-key locks for this index cover the following intervals, where a round bracket denotes exclusion of the interval endpoint and a square bracket denotes inclusion of the endpoint:
            ```
            (negative infinity, 10]
            (10, 11]
            (11, 13]
            (13, 20]
            (20, positive infinity)
            ```
            - For the last interval,The supremum is not a real index record, so, in effect, this next-key lock locks only the gap following the largest index value
        - 可重复读隔离级别下，查询都采用这种锁定算法
            - InnoDB operates in REPEATABLE READ transaction isolation level. In this case, InnoDB uses next-key locks for searches and index scans, which prevents phantom rows
        - 解决幻读
        
    - 插入意向锁
    - 读未提交
        - MySQL 事务隔离其实是依靠锁来实现的，加锁自然会带来性能的损失。而读未提交隔离级别是不加锁的，所以它的性能是最好的，没有加锁、解锁带来的性能开销
        - 可以读到其他事务未提交的数据，但没有办法保证你读到的数据最终一定是提交后的数据，如果中间发生回滚，那就会出现脏数据问题，读未提交没办法解决脏数据问题。也无法解决可重复读和幻读
    - 串行 Serializable
      - 这个级别很简单，读加共享锁，写加排他锁，读写互斥。使用的悲观锁的理论，实现简单，数据更加安全，但是并发能力非常差。如果你的业务并发的特别少或者没有并发，同时又要求数据及时可靠的话，可以使用这种模式。
      - select在Serializable这个级别，还是会加锁的
      - SELECT ... FROM is a consistent read, reading a snapshot of the database and setting no locks unless the transaction isolation level is set to SERIALIZABLE

#### 加锁 
- update
    - 二级索引的叶子节点中保存了主键索引的位置，在给二级索引加锁的时候，主键索引也会一并加锁；有可能其他事务会根据主键对 students 表进行更新，如果主键索引没有加锁，那么显然会存在并发问题；真正的行数据都被组织到主键索引的叶子节点
    - 在 InnoDb 存储引擎里，每个数据页中都会有两个虚拟的行记录，用来限定记录的边界，分别是：Infimum Record 和 Supremum Record，Infimum 是比该页中任何记录都要小的值，而 Supremum 比该页中最大的记录值还要大，这两条记录在创建页的时候就有了，并且不会删除
    - 在没有索引的时候，只能走聚簇索引，对表中的记录进行全表扫描。在 RC 隔离级别下会给所有记录加行锁，在 RR 隔离级别下，不仅会给所有记录加行锁，所有聚簇索引和聚簇索引之间还会加上 GAP 锁
        - 这是由于 MySQL 的实现决定的。如果一个条件无法通过索引快速过滤，那么存储引擎层面就会将所有记录加锁后返回，然后由 MySQL Server 层进行过滤，因此也就把所有的记录都锁上了
            - 不过在实际的实现中，MySQL 有一些改进，如果是 RC 隔离级别，在 MySQL Server 过滤条件发现不满足后，会调用 unlock_row 方法，把不满足条件的记录锁释放掉（违背了 2PL 的约束）。这样做可以保证最后只会持有满足条件记录上的锁，但是每条记录的加锁操作还是不能省略的
            - 如果是 RR 隔离级别，一般情况下 MySQL 是不能这样优化的，除非设置了 innodb_locks_unsafe_for_binlog 参数，这时也会提前释放锁，并且不加 GAP 锁，这就是所谓的 semi-consistent read
                - 启用innodb_locks_unsafe_for_binlog在没有索引的时候走聚簇索引时，还有以下作用：
                    - 对于update或者delete语句，InnoDB只会持有匹配条件的记录的锁。在MySQL Server过滤where条件，发现不满足后，会把不满足条件的记录释放锁。这可以大幅降低死锁发生的概率。
                    - 简单来说，semi-consistent read是read committed与consistent read两者的结合。一个update语句，如果读到一行已经加锁的记录，此时InnoDB返回记录最近提交的版本，由MySQL上层判断此版本是否满足update的where条件。若满足(需要更新)，则MySQL会重新发起一次读操作，此时会读取行的最新版本(并加锁)。
        - 半一致读发生条件
        RC隔离级别
        RR隔离级别，且innodb_locks_unsafe_for_binlog=true
            - innodb_locks_unsafe_for_binlog
                innodb_locks_unsafe_for_binlog默认为off。
                如果设置为1，会禁用gap锁，但对于外键冲突检测（foreign-key constraint checking）或者重复键检测（duplicate-key checking）还是会用到gap锁。
                启用innodb_locks_unsafe_for_binlog产生的影响等同于将隔离级别设置为RC，不同之处是：
                1）innodb_locks_unsafe_for_binlog是全局参数，影响所有session；但隔离级别可以是全局也可以是会话级别。
                2）innodb_locks_unsafe_for_binlog只能在数据库启动的时候设置；但隔离级别可以随时更改。
                基于上述原因，RC相比于innodb_locks_unsafe_for_binlog会更好更灵活
    - 范围查询
        - 所以对于范围查询，如果 WHERE 条件是 id <= N，那么 N 后一条记录也会被加上 Next-key 锁；如果条件是 id < N，那么 N 这条记录会被加上 Next-key 锁。另外，如果 WHERE 条件是 id >= N，只会给 N 加上记录锁，以及给比 N 大的记录加锁，不会给 N 前一条记录加锁；如果条件是 id > N，也不会锁前一条记录，连 N 这条记录都不会锁
            - 查找过程中访问到的对象才会加锁，范围查找时，不管是不是唯一索引都会扫描到 第一个不满足条件的值。 这点才是关键 (待定 如何确定扫描到 如何定义scanned)
        - 当数据表中数据非常少时， 语句会走全表扫描，这样表中所有记录都会被锁住 (不确定)
        - 在范围查询时，加锁是一条记录一条记录挨个加锁的，所以虽然只有一条 SQL 语句，如果两条 SQL 语句的加锁顺序不一样，也会导致死锁
- 复杂查询
    - Index Filter，就是复合索引中除 Index Key 之外的其他可用于过滤的条件
        - 在 MySQL 5.6 之后，Index Filter 与 Table Filter 分离，Index Filter 下降到 InnoDB 的索引层面进行过滤，减少了回表与返回 MySQL Server 层的记录交互开销，提高了SQL的执行效率，这就是传说中的 ICP（Index Condition Pushdown），使用 Index Filter 过滤不满足条件的记录，无需加锁
- delete
    - DELETE 和 UPDATE 的加锁也几乎是一样的
    - 标记为删除的记录，对于这种类型记录，它的加锁和其他记录的加锁机制不一样
    - 执行 DELETE 语句其实并没有直接删除记录，而是在记录上打上一个删除标记，然后通过后台的一个叫做 purge 的线程来清理
- insert
    - 隐式锁的特点是只有在可能发生冲突时才加锁，减少了锁的数量。另外，隐式锁是针对被修改的 B+Tree 记录，因此都是 Record 类型的锁，不可能是 Gap 或 Next-Key 类型
    - 为了防止幻读，如果记录之间加有 GAP 锁，此时不能 INSERT；
        - 加插入意向锁
    - 如果 INSERT 的记录和已有记录造成唯一键冲突，此时不能 INSERT
        - 加 S 锁进行当前读
    - INSERT 加锁流程
        - 首先对插入的间隙加插入意向锁（Insert Intension Locks）(INSERT 语句要插入记录所在区间加插入意向锁)
            - 如果该间隙已被加上了 GAP 锁或 Next-Key 锁，则加锁失败进入等待；
            - 如果没有，则加锁成功，表示可以插入；
        - 然后判断插入记录是否有唯一键，如果有，则进行唯一性约束检查
            - 如果不存在相同键值，则完成插入
            - 如果存在相同键值，则判断该键值是否加锁
                - 如果没有锁， 判断该记录是否被标记为删除
                    - 如果标记为删除，说明事务已经提交，还没来得及 purge，这时加 S 锁等待；
                    - 如果没有标记删除，则报 1062 duplicate key 错误；
                - 如果有锁，说明该记录正在处理（新增、删除或更新），且事务还未提交，加 S 锁等待；
        - 插入记录并对记录加 X 记录锁            
#### 死锁
- 死锁日志
    - 以通过 show engine innodb status 命令来获取死锁信息，但是它有个限制，只能拿到最近一次的死锁日志
    - InnoDb 的监控
    - 系统参数 innodb_print_all_deadlocks 专门用于记录死锁日志，当发生死锁时，死锁日志会记录到 MySQL 的错误日志文件中
    - 对死锁的诊断不能仅仅靠死锁日志，还应该结合应用程序的代码来进行分析，如果实在接触不到应用代码，还可以通过数据库的 binlog 来分析（只要你的死锁不是 100% 必现，那么 binlog 日志里肯定能找到一份完整的事务一和事务二的 SQL 语句）。通过应用代码或 binlog 理出每个事务的 SQL 执行顺序，这样分析死锁时就会容易很多
- 死锁的根本原因是有两个或多个事务之间加锁顺序的不一致导致的

- 死锁
    - 多个事务相互等待锁资源(等待其他事务释放锁以获得锁)
    - 采用等待图(wait-for graph)的方式来进行死锁检测
        - 事务为图中的节点，节点会指向其他节点
        - 图中存在回路则发生死锁
        - 死锁检测采用深度优先算法(td)  
    - 如何避免死锁
        - 对索引加锁顺序的不一致很可能会导致死锁，所以如果可以，尽量以相同的顺序来访问索引记录和表。以批量方式处理数据的时候，如果事先对数据排序，保证每个线程按固定的顺序来处理记录，也可以大大降低出现死锁的可能
        - 为表添加合理的索引，如果不走索引将会为表的每一行记录加锁，死锁的概率就会大大增大；
        - 避免大事务，尽量将大事务拆成多个小事务来处理；因为大事务占用资源多，耗时长，与其他事务冲突的概率也会变高；
    - 如何解决死锁        
- 锁升级
    - [https://segmentfault.com/a/1190000037510033]
    - InnoDB不存锁升级
    - InnoDB不是根据每个记录产生行锁，根据事务访问的每个页对锁进行管理，即根据页加锁
    - 一个事务锁住一个页中的一个记录还是多个记录，开销通常是一致的(td)        
#### practice
- pre
```
CREATE TABLE `z` (
  `a` int(11) NOT NULL,
  `b` int(11) DEFAULT NULL,
  PRIMARY KEY (`a`),
  KEY `idx_b` (`b`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- INSERT INTO z SELECT 1,1;
-- INSERT INTO z SELECT 3,1;
-- INSERT INTO z SELECT 5,3;
-- INSERT INTO z SELECT 7,6;
-- INSERT INTO z SELECT 10,8;

```

- deadlock
```
-- BEGIN; -- t1
-- select * from z where a < 7 LOCK IN SHARE MODE;
-- COMMIT;
-- 
-- BEGIN; -- t2
-- select * from z where a = 7 for UPDATE; -- 不会锁住
-- INSERT INTO z SELECT 4,2; -- deadlock
-- COMMIT;
```


- record lock 、next-key lock 、 gap lock
```
-- INSERT INTO z SELECT 1,1;
-- INSERT INTO z SELECT 3,1;
-- INSERT INTO z SELECT 5,3;
-- INSERT INTO z SELECT 7,6;
-- INSERT INTO z SELECT 10,8;


-- BEGIN;
-- select * from z where b=3 for UPDATE; --  (1,3] (3,6)  gap lock next-key lock 的范围？
-- -- 为什么需要  gap lock next-key lock 锁住这么大范围来防止幻读? 已解决
-- --  会同时锁住 a = 5;  -- 但 -- select * from z where a=3 for UPDATE;  并没有锁住 b=1; ？ 
-- commit;

-- BEGIN;
-- SELECT * from z where a = 5 LOCK IN SHARE MODE;

-- SELECT * from z where b = 1 LOCK IN SHARE MODE; -- 不会锁
-- INSERT INTO z SELECT 2,1; -- 不会锁
-- INSERT INTO z SELECT 4,1; -- 会锁

-- SELECT * from z where b = 6 LOCK IN SHARE MODE; -- 不会锁
-- INSERT INTO z SELECT 6,6; -- 会锁 
-- INSERT INTO z SELECT 8,6; -- 不会锁
-- commit;


-- ----------------------------------
-- BEGIN; -- A事务
--  select * from z where b > 3 for UPDATE; 
--  commit;
 
--  BEGIN; -- B事务
--  select * from z where b = 1 for UPDATE; -- 不会锁住
-- select * from z where b = 3 for UPDATE; -- 不会锁住
-- select * from z where b = 6 for UPDATE; -- 锁住了
-- commit;
-- -------------------------------
-- BEGIN; -- A事务
--  select * from z where b >= 3 for UPDATE;
-- commit
--  BEGIN; -- B事务
--  select * from z where b = 1 for UPDATE; -- 不会锁住
-- select * from z where b = 3 for UPDATE; -- 锁住
-- select * from z where b = 6 for UPDATE; -- 锁住
-- commit;

-- ----------------------------------
-- BEGIN; -- A事务
-- select * from z where b <= 3 for UPDATE; 
-- commit

--  BEGIN; -- B事务
--  select * from z where b = 1 for UPDATE; -- 锁住
-- select * from z where b = 3 for UPDATE; -- 锁住
-- select * from z where b = 6 for UPDATE; -- 锁住 -- 查找过程中访问到的对象才会加锁，范围查找时，不管是不是唯一索引都会扫描到 第一个不满足条件的值。 这点才是关键
-- select * from z where b = 8 for UPDATE; -- 不会锁住
-- commit;


-- ----------------------------------
-- BEGIN; -- A事务
-- select * from z where b < 3 for UPDATE; 
-- commit;
--  BEGIN; -- B事务
--  select * from z where b = 1 for UPDATE; -- 锁住
-- select * from z where b = 3 for UPDATE; -- 锁住 -- 查找过程中访问到的对象才会加锁，范围查找时，不管是不是唯一索引都会扫描到 第一个不满足条件的值。 这点才是关键
-- select * from z where b = 6 for UPDATE; -- 不会锁住
-- commit;

-- ----------------------------------
BEGIN; -- A事务
select * from z where b = 3 for UPDATE; 
commit


 BEGIN; -- B事务
update z set b=3 where b=6  -- 会锁住 why 
commit;

```

- 二级索引对聚簇索引的影响 二级索引的排他锁会导致该行对应的聚簇索引被锁
```
-- CREATE TABLE `Y` (
--   `a` int(11) NOT NULL,
--   `b` int(11) DEFAULT NULL,
--   PRIMARY KEY (`a`),
--   UNIQUE KEY `uk_b` (`b`) USING BTREE
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- INSERT INTO z SELECT 1,1;
-- INSERT INTO z SELECT 3,1;
-- INSERT INTO z SELECT 5,3;
-- INSERT INTO z SELECT 7,6;
-- INSERT INTO z SELECT 10,8;
-- ----------------
-- BEGIN; -- A事务
-- select * from Y where b = 3 LOCK IN SHARE MODE;
-- commit
-- 
--  BEGIN; -- B事务
-- -- select * from Y where a = 5 for UPDATE; -- 不会锁住
-- select * from Y where a = 5 LOCK IN SHARE MODE;; -- 不会锁住
-- commit;


-- ------------------
-- BEGIN; -- A事务
-- select * from Y where b = 3 for UPDATE; 
-- commit

--  BEGIN; -- B事务
-- select * from Y where a = 5 for UPDATE; -- 锁住
-- select * from Y where a = 5 LOCK IN SHARE MODE;; -- 锁住
-- commit;

```

```
-- t1
BEGIN;
select * from dw_term_type where id = 2 for update;
select * from dw_term where type = 2;
delete from dw_term_type where id = 1;
commit


-- t2
BEGIN;
select * from dw_term_type where id = 2 for update;
INSERT INTO `dw_term` (id, `term`,`full_name`,`desc`,`type`,gmt_create,creator,gmt_update) VALUES (2,'2','2','2',2,2,2,2);
commit;
```


#### todo
- 为什么需要插入意向锁
```
CREATE TABLE `z` (
  `a` int(11) NOT NULL,
  `b` int(11) DEFAULT NULL,
  PRIMARY KEY (`a`),
  KEY `idx_b` (`b`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- INSERT INTO z SELECT 1,1;
-- INSERT INTO z SELECT 3,1;
-- INSERT INTO z SELECT 5,3;
-- INSERT INTO z SELECT 7,6;
-- INSERT INTO z SELECT 10,8;



BEGIN; -- t1
select * from z where b = 3 for UPDATE; 
commit
-- 
BEGIN; -- t2
-- 更新为3就会锁住
update z set b=3 where b=1  -- 会锁住 why  -- 为什么需要插入意向锁 -- 插入意向锁的范围是(负无穷，正无穷) 确实是需要的 不然 select * from z where b = 3 for UPDATE; 这个锁将失去作用
update z set b=3 where b=6  -- 会锁住 why  -- 为什么需要插入意向锁 
update z set b=3 where b=8  -- 会锁住 why  -- 为什么需要插入意向锁 

-- update z set b=100 where b=1  -- 不会锁住
-- update z set b=100 where b=6  -- 不会锁住
-- update z set b=100 where b=8  -- 不会锁住
commit;


==========================
------------
TRANSACTIONS
------------
Trx id counter 808557
Purge done for trx's n:o < 808556 undo n:o < 0 state: running but idle
History list length 1
LIST OF TRANSACTIONS FOR EACH SESSION:
---TRANSACTION 281479785903104, not started
0 lock struct(s), heap size 1136, 0 row lock(s)
---TRANSACTION 281479785899712, not started
0 lock struct(s), heap size 1136, 0 row lock(s)
---TRANSACTION 808556, ACTIVE 8 sec updating or deleting
mysql tables in use 1, locked 1
LOCK WAIT 5 lock struct(s), heap size 1136, 4 row lock(s), undo log entries 1
MySQL thread id 16, OS thread handle 123145352810496, query id 1536 localhost 127.0.0.1 root updating
update z set b=3 where b=6
------- TRX HAS BEEN WAITING 8 SEC FOR THIS LOCK TO BE GRANTED:
RECORD LOCKS space id 57 page no 5 n bits 80 index idx_b of table `mytest`.`z` trx id 808556 lock_mode X locks gap before rec insert intention waiting
Record lock, heap no 5 PHYSICAL RECORD: n_fields 2; compact format; info bits 32
 0: len 4; hex 80000006; asc     ;;
 1: len 4; hex 80000007; asc     ;;

------------------
---TRANSACTION 808549, ACTIVE 449 sec
4 lock struct(s), heap size 1136, 3 row lock(s)
MySQL thread id 13, OS thread handle 123145351901184, query id 1505 localhost 127.0.0.1 root
--------
FILE I/O
--------
```
```

-- ----------------------------
-- Table structure for X
-- ----------------------------
DROP TABLE IF EXISTS `X`;
CREATE TABLE `X` (
  `a` int(11) NOT NULL,
  `b` int(11) DEFAULT NULL,
  `c` int(11) DEFAULT NULL,
  PRIMARY KEY (`a`),
  KEY `uk_b` (`b`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of X
-- ----------------------------
BEGIN;
INSERT INTO `X` VALUES (1, 1, 1);
INSERT INTO `X` VALUES (3, 1, 2);
INSERT INTO `X` VALUES (5, 3, 3);
INSERT INTO `X` VALUES (7, 6, 4);
INSERT INTO `X` VALUES (10, 8, 5);
COMMIT;



=========
BEGIN; -- t1
select * from X where b = 3 for UPDATE; 
commit


BEGIN; -- t2
update X set c=0 where b=6  -- 不会锁住  不需要插入意向锁
commit;
```

### explain
explain
type:
const: 针对主键或唯一索引的等值查询扫描, 最多只返回一行数据.
eq_ref: It is used when all parts of an index are used by the join and the index is a PRIMARY KEY or UNIQUE NOT NULL index
ref: 此类型通常出现在多表的 join 查询, 针对于非唯一或非主键索引, 或者是使用了 最左前缀 规则索引的查询. 
range: 表示使用索引范围查询, 通过索引字段范围获取表中部分数据记录. 这个类型通常出现在 =, <>, >, >=, <, <=, IS NULL, <=>, BETWEEN, IN() 操作中.
index: 表示全索引扫描(full index scan), 和 ALL 类型类似, 只不过 ALL 类型是全表扫描, 而 index 类型则仅仅扫描所有的索引, 而不扫描数据.
ALL: 表示全表扫描, 这个类型的查询是性能最差的查询之一.
ALL < index < range ~ index_merge < ref < eq_ref < const < system
key: 真正使用到的索引
key_len:优化器使用了索引的字节数,这个字段可以评估组合索引是否完全被使用, 或只有最左部分字段被使用到.
key_len 的计算规则如下:
字符串
char(n): n 字节长度
varchar(n): 如果是 utf8 编码, 则是 3 n + 2字节; 如果是 utf8mb4 编码, 则是 4 n + 2 字节.
数值类型:
TINYINT: 1字节
SMALLINT: 2字节
MEDIUMINT: 3字节
INT: 4字节
BIGINT: 8字节
时间类型
DATE: 3字节
TIMESTAMP: 4字节
DATETIME: 8字节
字段属性: NULL 属性 占用一个字节. 如果一个字段是 NOT NULL 的, 则没有此属性.




- 如何通过 show engine innodb status; 查看间隙锁的范围

- 16K /1K =16行 16K/(8 + 6)B =1000， 1000 * 1000 * 1000 =  