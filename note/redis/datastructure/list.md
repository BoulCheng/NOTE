

- redis 3.2版本前
    - ziplist -> Linkedlist 
    - Linkedlist
        - 链表的附加空间相对太高，prev 和 next 指针就要占去 16 个字节 (64bit 系统的指针是 8 个字节)，另外每个节点的内存都是单独分配，会加剧内存的碎片化，影响内存管理效率
    
- redis 3.2版本后
    - quicklist - A doubly linked list of ziplists
        - quicklist 实际上是 zipList 和 linkedList 的混合体，它将 linkedList 按段切分，每一段使用 zipList 来紧凑存储，多个 zipList 之间使用双向指针串接起来。
    - 可以想象得到，当一个空的quicklist加入一个值value时，会有以下操作（不一定以这个顺序）：
      
      使用Entry包裹value
      创建一个ziplist，把Entry加入到ziplist中
      创建一个Node，Node.zl指向ziplist
      创建quicklist，将Node加入quicklist中
      原来版本的list直接使用的一个ziplist，而现在版本的list可以看成使用多个ziplist，然后通过双向链表连接起来。如果知道ziplist的特性，那这样的优点就很明显了。
      
      由于ziplist的连锁更新。ziplist的插入删除操作在最坏情况下的复杂度为O(n^2)，虽然导致整条ziplist连锁更新的概率很低。将原来的ziplist划分成多条ziplist后，插入删除一个元素，即使情况再极端也只会引起一段ziplist的连锁更新
    - Linkedlist耗内存但是能应付数据较大（数量或者单个的长度）的情况，但是插入和删除成本低，ziplist在小规模数据情况下表现很好并且非常节省内存，数据规模大时会有问题，并且插入和删除成本高    