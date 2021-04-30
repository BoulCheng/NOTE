
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
 
- 死锁
    - 多个事务相互等待锁资源(等待其他事务释放锁以获得锁)
    - 采用等待图(wait-for graph)的方式来进行死锁检测
        - 事务为图中的节点，节点会指向其他节点
        - 图中存在回路则发生死锁
        - 死锁检测采用深度优先算法(td)  
    - 如何避免死锁
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

- 如何通过 show engine innodb status; 查看间隙锁的范围

- 16K /1K =16行 16K/(8 + 6)B =1000， 1000 * 1000 * 1000 =  