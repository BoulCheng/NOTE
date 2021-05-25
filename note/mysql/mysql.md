

- 一般对于业务记录类随时间会不断增加的数据，当数据量增加到一定量（一般认为整型值为主的表达到千万级，字符串为主的表达到五百万）的时候，性能将遇到瓶颈，同时调整表结构也会变得非常困难
    - mysql 数据量上限原因
- 数据库中间件
- 读写分离方案


- redolog binlog 的两阶段提交及其较普通两阶段提交的优化
- innodb_flush_log_at_trx_commit和sync_binlog
- group commit
- 分库分表 

- FULLTEXT ，用于全文索引 前缀索引
- count(*) 和 count(1)和count(列名)区
- 索引失效为什么不会使用主键索引？ i see

- varchar(255)
    - 首先我们要知道一个概念：InnoDB存储引擎的表索引的前缀长度最长是767字节(bytes)
    - 如果需要建索引，就不能超过 767 bytes；utf8编码时 255*3=765bytes ,恰恰是能建索引情况下的最大值。
    - utf8mb4编码时，默认字符长度则应该是 767除以4向下取整，也就是191。


- vs hbase
- 笛卡尔乘积
- join原理

- 游标

- 视图

- 索引失效
    - 字符串和数值
```

CREATE TABLE `explain_test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `score` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sex` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `explain_test_name` (`name`) USING BTREE,
  KEY `explain_test_score` (`score`) USING BTREE,
  KEY `explain_test_age` (`age`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
explain select * from explain_test where  CHAR_LENGTH(score)  != '3'  ;  -- const
<>， not in，!= 
-- 数值转化为字符串处理 
explain select age from explain_test where score = 1 ; 
explain select age from explain_test where age = '1' ; 
-- 
explain select * from test;

explain select * from explain_test where id = 1 ;  -- const
explain select * from explain_test where name = '1' ; -- const
explain select * from explain_test where score = '1' ; -- ref
explain select * from explain_test where id in (1,2,3) ; -- range
explain select * from explain_test where score between '1' and '3' ; -- range
explain select * from explain_test where score < '1' ; -- range
explain select score from explain_test   ; -- index
explain select sex from explain_test   ; -- ALL



explain select age from explain_test  ; 
explain select score from explain_test  ;


explain select t.* from (select id from explain_test) t;

explain select * from explain_test union select * from explain_test
```   


- 3.3 4 5 6 

#### tip
- explain
    - type
        - ref
            - ref is used if the join uses only a leftmost prefix of the key or if the key is not a PRIMARY KEY or UNIQUE index (in other words, if the join cannot select a single row based on the key value)
            - 连接不能根据键值选择单行
    - Extra
        - Using where
            - MySQL将通过Where条件筛选存储引擎返回的记录
            
- 查询
- 使用IN代替关联查询 可以让MySQL按照ID顺序进行查询 可能比随机的关联更高效
- mysql 的innodb引擎中，是允许在唯一索引的字段中出现多行null值的。根据NULL的定义，NULL表示的是未知，因此两个NULL比较的结果既不相等，也不不等，结果仍然是未知。根据这个定义，多个NULL值的存在应该不违反唯一约束，所以是合理的
- 

- MySQL 三种关联查询的方式: ON vs USING vs 传统风格 
    - 有一种特殊情况，当两个要关联表的字段名是一样的，我们可以使用  USING
- EXPLAIN EXTENDED、Show Warnings
    - explain 的extended 扩展能够在原本explain的基础上额外的提供一些查询优化的信息，这些信息可以通过MySQL的show warnings命令得到
    - 首先执行对想要分析的语句进行explain，并带上extended选项
    - 接下来再执行Show Warnings
        - 可以查看查询优化器优化后的sql
    ```
    mysql>
    mysql> explain extended select * from dem_peek INNER JOIN dem_peek_detail_record ON dem_peek.id = dem_peek_detail_record.peek_id;
    +----+-------------+------------------------+--------+---------------+---------+---------+------------------------------------+------+----------+-------------+
    | id | select_type | table                  | type   | possible_keys | key     | key_len | ref                                | rows | filtered | Extra       |
    +----+-------------+------------------------+--------+---------------+---------+---------+------------------------------------+------+----------+-------------+
    |  1 | SIMPLE      | dem_peek_detail_record | ALL    | NULL          | NULL    | NULL    | NULL                               |  120 |   100.00 | NULL        |
    |  1 | SIMPLE      | dem_peek               | eq_ref | PRIMARY       | PRIMARY | 8       | dem.dem_peek_detail_record.peek_id |    1 |   100.00 | Using where |
    +----+-------------+------------------------+--------+---------------+---------+---------+------------------------------------+------+----------+-------------+
    2 rows in set, 1 warning (0.03 sec)
    
    mysql> show warnings;
    +-------+------+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
    | Level | Code | Message                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
    +-------+------+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
    | Note  | 1003 | /* select#1 */ select `dem`.`dem_peek`.`id` AS `id`,`dem`.`dem_peek`.`model_id` AS `model_id`,`dem`.`dem_peek`.`name` AS `name`,`dem`.`dem_peek`.`field_list` AS `field_list`,`dem`.`dem_peek`.`peek_time` AS `peek_time`,`dem`.`dem_peek`.`deleted` AS `deleted`,`dem`.`dem_peek`.`gmt_create` AS `gmt_create`,`dem`.`dem_peek`.`creator` AS `creator`,`dem`.`dem_peek`.`gmt_update` AS `gmt_update`,`dem`.`dem_peek`.`modifier` AS `modifier`,`dem`.`dem_peek_detail_record`.`id` AS `id`,`dem`.`dem_peek_detail_record`.`peek_id` AS `peek_id`,`dem`.`dem_peek_detail_record`.`name` AS `name`,`dem`.`dem_peek_detail_record`.`peek_user_id` AS `peek_user_id`,`dem`.`dem_peek_detail_record`.`peek_sql` AS `peek_sql`,`dem`.`dem_peek_detail_record`.`peek_type` AS `peek_type`,`dem`.`dem_peek_detail_record`.`peek_status` AS `peek_status`,`dem`.`dem_peek_detail_record`.`email_send_time` AS `email_send_time`,`dem`.`dem_peek_detail_record`.`deleted` AS `deleted`,`dem`.`dem_peek_detail_record`.`gmt_create` AS `gmt_create`,`dem`.`dem_peek_detail_record`.`creator` AS `creator`,`dem`.`dem_peek_detail_record`.`gmt_update` AS `gmt_update`,`dem`.`dem_peek_detail_record`.`modifier` AS `modifier` from `dem`.`dem_peek` join `dem`.`dem_peek_detail_record` where (`dem`.`dem_peek`.`id` = `dem`.`dem_peek_detail_record`.`peek_id`) |
    +-------+------+--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
    1 row in set (0.04 sec)
    
    mysql>
    ```


### 查询性能优化
- 查询优化 索引优化 库表结构优化
- 查询的生命周期(子任务)
    - 客户端
    - 服务端
        - 解析
        - 生成执行计划
        - 执行
            - 包括大量为了检索数据到存储引擎的调用以及调用后的处理，排序、分组等
        - 返回结果给客户端
- 网络 内存 cpu 锁等待 I/O等待 上下文切换 系统调用
- 优化数据访问
    - 衡量查询开销
        - 响应时间
            - 服务时间 + 排队时间
            - 快速上限估计
                - 需要哪些索引和执行计划
                - 多少顺序和随机I/O
                - 一次I/O的消耗时间
        - 扫描的行数
            - 内存中的行比磁盘中的行访问快
            - 访问类型 explain type
                - type
                - 扫描表 扫描索引 范围访问和单值访问的概念
                - 没有合适的访问类型 就增加一个合适的索引 索引让MySQL以最高效、扫描行数最少的方式找到需要的记录 
        - 返回的行数
    - 检索大量超过需要的数据，过多的行或列
        - - limit 多表关联返回多表全部列 select * 
    - MySQL服务器层分析大量超过需要的数据行
    
- 重构查询方式
    - 大查询 小查询
    - 切分查询
        - 大delete切分成许多较小的delete
            - 锁 事务 MySQL复制延迟 
    - 分解关联查询 在应用层关联
        - 缓存效率更高 重复利用查询缓存
        - 单个查询减少锁竞争
        - 应用层关联 可以更容易对数据库进行拆分 更容易做到高性能和可扩展
        - 查询效率可能会提升
        - 减少冗余记录查询  避免重复访问一部分数据
        - 相当于在应用中实现了哈希关联 而不是使用MySQL的嵌套循环关联
- 查询执行的基础
    - 执行一个查询的过程(查询执行路径 p422 图6-1)
    - MySQL客户端/服务器通信协议
        - 半双工
        - limit 限制服务器响应给客户端的数据条数
        - 服务器(MySQL库函数)缓存查询结果集
    - 查询状态
    

- mysql查询
    - 普通查询    
        - 一次性把查询的所有结果集都保存在本地内存中，所以如果数据量太大，超过jvm内存，则会发生OOM
    - 流式查询
        - 流式查询与普通查询不同之处在于并不是一次性将所有数据加载到内存，在调用ResultSet#next()方法时，MySQL驱动只从网络数据流获取到1条数据，然后返回应用，这样就避免了内存溢出问题
        - 设置流式查询 statement.setFetchSize(Integer.MIN_VALUE);
        - 在流式查询下不能进行多次查询获取多个ResultSet进行操作
            - 会抛异常 java.sql.SQLException: Streaming result set com.mysql.cj.protocol.a.result.ResultsetRowsStreaming@21775abc is still active. No statements may be issued when any streaming result sets are open and in use on a given connection. Ensure that you have called .close() on any active streaming result sets before attempting more queries.
        - mysql本身并没有FetchSize方法, 它是通过使用CS阻塞方式的网络流控制实现服务端不会一下发送大量数据到客户端撑爆客户端内存， 这样带来的问题：如果使用了流式查询，一个MySQL数据库连接同一时间只能为一个ResultSet对象服务，并且如果该ResultSet对象没有关闭，势必会影响其他查询对数据库连接的使用！如果应用对数据库连接的消耗要求严苛，那么流式查询就不再适合。
        - sharding-sphere由于做的是流式查询，但无法在同一个数据库connection中拿到多个resultSet，再做结果集合并；需要每个结果集单独使用一个Connection获取resultSet
        - code 
            ```
            try (Connection conn = dataSource.getConnection(dsi)) {
                            try (Statement statement = conn.createStatement()) {
                                statement.setFetchSize(Integer.MIN_VALUE);
                                ResultSet rs = statement.executeQuery("");
                                rs.next();
                                Long l = rs.getLong(1);
            
            
            
                                ResultSet rs2;
                                Long l2;
                                Object o1;
                                try {
                                    rs2 = statement.executeQuery("");
                                    rs2.next();
                                    l2 = rs2.getLong(1);
                                    o1 = rs.getObject(2);// 抛异常 java.sql.SQLException: Operation not allowed after ResultSet closed
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    // 在流式查询下不能进行多次查询获取多个ResultSet进行操作 
                                    Statement statement2 = conn.createStatement();
                                    statement2.setFetchSize(Integer.MIN_VALUE);
                                    try {
                                        rs = statement2.executeQuery("");// 抛异常  java.sql.SQLException: Streaming result set com.mysql.cj.protocol.a.result.ResultsetRowsStreaming@21775abc is still active. No statements may be issued when any streaming result sets are open and in use on a given connection. Ensure that you have called .close() on any active streaming result sets before attempting more queries.
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
            
                            }
                        }
            ```
        - MySQL Connector/J 5.1 Developer Guide
            - There are some caveats with this approach. You must read all of the rows in the result set (or close it) before you can issue any other queries on the connection, or an exception will be thrown. 也就是说当通过流式查询获取一个ResultSet后，在你通过next迭代出所有元素之前或者调用close关闭它之前，你不能使用同一个数据库连接去发起另外一个查询，否者抛出异常（第一次调用的正常，第二次的抛出异常）
        - ResultSet
        
- 游标查询
    - 这种方式下MySQL服务器端一次只发送fetchSize条数据，MySQL驱动会获取完fetchSize条数据后返回给应用，应用处理完继续调用ResultSet#next()时，继续发送fetch命令，继续获取下一批次fetchSize条数据
    - 设置连接mysql-server参数 useCursorFetch=true，
    - 指定fetchSize
    - 应用指定每次查询获取的条数fetchSize，MySQL服务器每次只查询指定条数的数据，因此单次查询相比与前面两种方式占用MySQL时间较短。但由于MySQL方不知道客户端什么时候将数据消费完，MySQL需要建立一个临时空间来存放每次查询出的数据，大数据量时MySQL服务器IOPS、磁盘占用都会飙升，而且需要与服务器进行更多次的网络通讯，因此最终查询效率是不如流式查询的
    - 由于基于游标方式，服务器端需要更多额外处理，查询性能更低些，对于大数据量一般情况下推荐基于动态结果集的流式查询
    - 流式的结果集应该是保存在客户端内核缓冲区，但是当缓冲区满了，客户端通过CS阻塞方式的网络流控制服务端，使得服务端后面的结果处于阻塞状态；然后客户端消耗缓冲区的结果集，腾出空间后服务端继续向缓冲区注入结果集
    
