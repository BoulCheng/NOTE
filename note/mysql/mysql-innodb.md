
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
        - This is called multi-versioned concurrency control.    
    - Locking Reads
        - locking reads (SELECT with FOR UPDATE or FOR SHARE),

- Locks Set by Different SQL Statements in InnoDB
    - A locking read, an UPDATE, or a DELETE generally set record locks on every index record that is scanned in the processing of the SQL statement. It does not matter whether there are WHERE conditions in the statement that would exclude the row.
        - The locks are normally next-key locks that also block inserts into the “gap” immediately before the record.
    - If a secondary index is used in a search and index record locks to be set are exclusive, InnoDB also retrieves the corresponding clustered index records and sets locks on them.
        - 二级索引的排他锁会导致该行对应的聚簇索引被锁
    - If you have no indexes suitable for your statement and MySQL must scan the entire table to process the statement, every row of the table becomes locked, which in turn blocks all inserts by other users to the table
        - It is important to create good indexes so that your queries do not unnecessarily scan many rows.
    - InnoDB sets specific types of locks as follows.
        - SELECT ... FROM is a consistent read, reading a snapshot of the database and setting no locks unless the transaction isolation level is set to SERIALIZABLE
        - SELECT ... FOR UPDATE and SELECT ... FOR SHARE statements that use a unique index acquire locks for scanned rows, and release the locks for rows that do not qualify for inclusion in the result set (for example, if they do not meet the criteria given in the WHERE clause)
            - However, in some cases, rows might not be unlocked immediately because the relationship between a result row and its original source is lost during query execution. For example, in a UNION,
        - For locking reads (SELECT with FOR UPDATE or FOR SHARE), UPDATE, and DELETE statements, the locks that are taken depend on whether the statement uses a unique index with a unique search condition, or a range-type search condition.
            - For a unique index with a unique search condition, InnoDB locks only the index record found, not the gap before it.
                - 二级索引的排他锁会导致该行对应的聚簇索引被锁  
            - For other search conditions, and for non-unique indexes, InnoDB locks the index range scanned, using gap locks or next-key locks to block insertions by other sessions into the gaps covered by the range.
        - UPDATE ... WHERE ... sets an exclusive next-key lock on every record the search encounters. However, only an index record lock is required for statements that lock rows using a unique index to search for a unique row.
          
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