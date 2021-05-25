#### 缓存算法
- LRU（The Least Recently Used，最近最久未使用算法）
- LFU（Least Frequently Used ，最近最少使用算法）


### Cluster集群 主从数据同步问题



一致性（Consistency and WAIT）
WAIT 不能保证Redis强一致：尽管同步复制是复制状态机的一个部分，但是还需要其他条件。不过，在sentinel和Redis群集故障转移中，WAIT 能够增强数据的安全性。

如果写操作已经被传送给一个或多个slave节点，当master发生故障我们极大概率(不保证100%)提升一个受到写命令的slave节点为master:不管是Sentinel还是Redis Cluster 都会尝试选slave节点中最优(日志最新)的节点，提升为master。

尽管是选择最优节点，但是仍然会有丢失一个同步写操作可能行



### 缓存击穿
- 双key
    - 可用性和一致性的均衡
        - 一个key1设置过期时间且不放数据，另一个key2设置很长的或者不设置过期时间
            - redis lua 判断key1有没有数据，没有则新增，且设置一个相对过段的过期时间，并去设置key2数据，然后再更新key1过期时间为正常值