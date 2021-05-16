#### skiplist




##### Redis中的sorted set
- 是在skiplist, dict和ziplist基础上构建起来的
    - 当数据较少时，sorted set是由一个ziplist来实现的。
        - ziplist就是由很多数据项组成的一大块连续内存
        - 由于sorted set的每一项元素都由数据和score组成，因此，当使用zadd命令插入一个(数据, score)对的时候，底层在相应的ziplist上就插入两个数据项：数据在前，score在后。
        - ziplist的主要优点是节省内存
        - 查找操作只能按顺序查找（可以正序也可以倒序）。因此，sorted set的各个查询操作，就是在ziplist上从前向后（或从后向前）一步步查找，每一步前进两个数据项，跨域一个(数据, score)对。
    - 当数据多的时候，sorted set是由一个叫zset的数据结构来实现的，这个zset包含一个dict + 一个skiplist。dict用来查询数据到分数(score)的对应关系，而skiplist用来根据分数查询数据（可能是范围查找）。
        - 随着数据的插入，sorted set底层的这个ziplist就可能会转成zset的实现, 相关配置
            ```
            zset-max-ziplist-entries 128
            zset-max-ziplist-value 64
            ```
        - 在如下两个条件之一满足的时候，ziplist会转成zset
            - 当sorted set中的元素个数，即(数据, score)对的数目超过128的时候，也就是ziplist数据项超过256的时候。
            - 当sorted set中插入的任意一个数据的长度超过了64的时候。
        - zset数据结构定义
            ```
            typedef struct zset {
                dict *dict;
                zskiplist *zsl;
            } zset;
            
            ```
        - Redis为什么用skiplist而不用平衡树
            - 内存占用、对范围查找的支持和实现难易程度
            ```
            There are a few reasons:
            
            1) They are not very memory intensive. It’s up to you basically. Changing parameters about the probability of a node to have a given number of levels will make then less memory intensive than btrees.
            
            2) A sorted set is often target of many ZRANGE or ZREVRANGE operations, that is, traversing the skip list as a linked list. With this operation the cache locality of skip lists is at least as good as with other kind of balanced trees.
            
            3) They are simpler to implement, debug, and so forth. For instance thanks to the skip list simplicity I received a patch (already in Redis master) with augmented skip lists implementing ZRANK in O(log(N)). It required little changes to the code.
            

            - 内存占用更少 默认配置情况每个节点所包含的平均指针数目为1.33
            - 跳表查询的期望时间复杂度为O(logn) 且插入、删除操作更加简单
            - 实现简单
            - 体会下ConcurrentSkipListMap对ConcurrentHashMap的增强
