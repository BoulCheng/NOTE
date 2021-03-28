- bloomFilter
    - 一个很长的二进制向量 （位数组）
    - 一系列随机函数 (哈希)
    - 空间效率和查询效率高
    - 有一定的误判率（哈希表是精确匹配）
        - 不存在的判定为存在
- 核心实现
    - 多对精确性不需要那么高的场景，不保证绝对的精确
        - 实际的重复的一定会判断为重复，实际不重复有一定的误判率判断为重复
        
        - 不存在则一定不存在：不存在则一定不存在，那么这个特性则可以很好的实现去重。
        - 存在不一定真的存在：存在则可能对应的数组位与其他key产生了碰撞。
    - 一个超大的位数组-以Bitmap为基础的排重算法
    - 几个哈希函数-string作多次hash产生多个hashcode，有任意一个hashcode不存在则一定不会重复，所有hashcode都存在才判定为重复，仍然存在误判即不重复判定为重复 

- 误判率计算
    - Given
        - n: how many items you expect to have in your filter (e.g. 216,553)
        - p: your acceptable false positive rate {0..1} (e.g. 0.01 → 1%)
            - 允许的误判率(不重复的误判为重复)
    - we want to calculate:
        - m: the number of bits needed in the bloom filter
        - k: the number of hash functions we should apply
    - The formulas:
        - m = -n*ln(p) / (ln(2)^2) the number of bits
        - k = m/n * ln(2) the number of hash functions
    - n相等的情况下，允许的误判率p越低，m越大，k越大    
    - 具体实现com.google.common.hash.BloomFilter

- 哈希函数
    - 任意大小的数据转换成特定大小的数据的函数
    - 实现哈希表和布隆过滤器的基础
    
- 常规(大数量量消耗内存过多)
    - 数组
    - 链表
    - 树、平衡二叉树、Trie
    - Map (红黑树)
    - 哈希表
        

- Guava     
    - com.google.common.hash.BloomFilter
        - 期望的插入数量和误判率计算出真正的numBits和numHashFunctions(哈希函数个数)
            - 计算m
                ```
                    static long optimalNumOfBits(long n, double p) {
                        if (p == 0.0D) {
                            p = 4.9E-324D;
                        }
                
                        return (long)((double)(-n) * Math.log(p) / (Math.log(2.0D) * Math.log(2.0D)));
                    }
                
                ```
            - 计算k
                ```
                    static int optimalNumOfHashFunctions(long n, long m) {
                        return Math.max(1, (int)Math.round((double)m / (double)n * Math.log(2.0D)));
                    }
                ```
        - AtomicLongArray 并发安全
        - 优化多次hash计算成为性能瓶颈
            - 没有引入多个Hash函数，而是计算出一个hash值后，其他的使用位移，乘除等方法快速计算出来，其实本身是均匀分布的随机值，也没必要使用多个hash函数
            ```
                    public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BloomFilterStrategies.LockFreeBitArray bits) {
                        long bitSize = bits.bitSize();
                        //计算出一个hash值
                        long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
                        // 取低32位
                        int hash1 = (int)hash64;
                        // 取高32位
                        int hash2 = (int)(hash64 >>> 32);
                        boolean bitsChanged = false;
                        // 根据hash次数分别计算出hash值,然后设置到bits数组中
                        for(int i = 1; i <= numHashFunctions; ++i) {
                            int combinedHash = hash1 + i * hash2;
                            if (combinedHash < 0) {
                                combinedHash = ~combinedHash;
                            }
                            // 填充bits位 //combinedHash % bitSize取模避免combinedHash超过bitSize
                            bitsChanged |= bits.set((long)combinedHash % bitSize);
                        }
            
                        return bitsChanged;
                    }
            ```
        - 枚举实现策略模式
            - com.google.common.hash.BloomFilterStrategies
- Redis
    - RedisBloom