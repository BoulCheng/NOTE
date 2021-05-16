set是一个无序的、自动去重的集合数据类型，Set底层用两种数据结构存储，一个是hashtable，一个是inset。

其中hashtable的key为set中元素的值，而value为null

inset为可以理解为数组，使用inset数据结构需要满足下述两个条件：

元素个数不少于默认值512
  set-max-inset-entries 512
元素可以用整型表示
intset的底层结构

typedef struct intset {
    
    // 编码类型
    uint32_t encoding;

    // 集合包含的元素数量
    uint32_t length;

    // 保存元素的数组
    int8_t contents[];

} intset;
查询方式一般采用二分查找法，实际查询复杂度也就在log(n)