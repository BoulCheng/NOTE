#### ziplist
- The ziplist is a specially encoded dually linked list that is designed to be very memory efficient. It stores both strings and integer values, where integers are encoded as actual integers instead of a series of characters. It allows push and pop operations on either side of the list in O(1) time.
- ziplist是一个经过特殊编码的双向链表，它的设计目标就是为了提高存储效率。ziplist可以用于存储字符串或整数，其中整数是按真正的二进制表示进行编码的，而不是编码成字符序列。它能以O(1)的时间复杂度在表的两端提供push和pop操作

- ziplist却是将表中每一项存放在前后连续的地址空间内，一个ziplist整体占用一大块内存; ziplist就是由很多数据项组成的一大块连续内存
    - 避免大量的内存碎片；每一项都占用独立的一块内存，各项之间用地址指针（或引用）连接起来，会导致大量的内存碎片
    - 地址指针也会占用额外的内存
- 值的存储采用了变长的编码方式，大概意思是说，对于大的整数，就多用一些字节来存储，而对于小的整数，就少用一些字节来存储


- ziplist的内存结构
    - <zlbytes><zltail><zllen><entry>...<entry><zlend>
    - <entry>的构成
        - <prevrawlen><len><data>
          


- hash与ziplist
    - hash随着数据的增大，其底层数据结构的实现是会发生变化的，当然存储效率也就不同。在field比较少，各个value值也比较小的时候，hash采用ziplist来实现；而随着field增多和value值增大，hash可能会变成dict来实现。当hash底层变成dict来实现的时候，它的存储效率就没法跟那些序列化方式相比了
    - 当我们为某个key第一次执行 hset key field value 命令的时候，Redis会创建一个hash结构，这个新创建的hash底层就是一个ziplist
        ```
        robj *createHashObject(void) {
            unsigned char *zl = ziplistNew();
            robj *o = createObject(OBJ_HASH, zl);
            o->encoding = OBJ_ENCODING_ZIPLIST;
            return o;
        }
        ```
    - hash底层的这个ziplist转成dict的配置(redis.conf中的ADVANCED CONFIG部分)
        ```
        hash-max-ziplist-entries 512
        hash-max-ziplist-value 64
        
        ```
        - 这个配置的意思是说，在如下两个条件之一满足的时候，ziplist会转成dict：
            - 当hash中的数据项（即field-value对）的数目超过512的时候，也就是ziplist数据项超过1024的时候（请参考t_hash.c中的hashTypeSet函数）。
            - 当hash中插入的任意一个value的长度超过了64的时候（请参考t_hash.c中的hashTypeTryConversion函数）
        - Redis的hash之所以这样设计，是因为当ziplist变得很大的时候，它有如下几个缺点：
            - 每次插入或修改引发的realloc操作会有更大的概率造成内存拷贝，从而降低性能
            - 一旦发生内存拷贝，内存拷贝的成本也相应增加，因为要拷贝更大的一块数据。
            - 当ziplist数据项过多的时候，在它上面查找指定的数据项就会性能变得很低，因为ziplist上的查找需要进行遍历。
            - 总之，ziplist本来就设计为各个数据项挨在一起组成连续的内存空间，这种结构并不擅长做修改操作。一旦数据发生改动，就会引发内存realloc，可能导致内存拷贝。
             
