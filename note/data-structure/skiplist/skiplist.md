#### skiplist (10.4.2)
- A Probabilistic Alternative to Balanced Trees
- 本质上也是一种查找结构，用于解决算法中的查找问题（Searching），即根据给定的key，快速查到它所在的位置（或者对应的value）
- skiplist正是由多层链表的设计出来的
    - 是在有序链表的基础上发展起来的
    - 每相邻两个节点增加一个指针，让指针指向下下个节点,所有新增加的指针连成了一个新的链表，但它包含的节点个数只有原来的一半
    - 利用同样的方式，我们可以在上层新产生的链表上，继续为每相邻的两个节点增加一个指针，从而产生第三层链表
    - 当链表足够长的时候，这种多层链表的查找方式能让我们跳过很多下层节点，大大加快查找的速度
    - 实际上，按照上面生成链表的方式，上面每一层链表的节点个数，是下面一层的节点个数的一半，这样查找过程就非常类似于一个二分查找，使得查找的时间复杂度可以降低到O(log n)
    - 但是，这种方法在插入数据的时候有很大的问题。新插入一个节点之后，就会打乱上下相邻两层链表上节点个数严格的2:1的对应关系。如果要维持这种对应关系，就必须把新插入的节点后面的所有节点（也包括新插入的节点）重新进行调整，这会让时间复杂度重新蜕化成O(n)。删除数据也有同样的问题
    
- 顾名思义，跳表是一种类似于链表的数据结构。更加准确地说，跳表是对有序链表的改进。

- skiplist为了避免这一问题，它不要求上下相邻两层链表之间的节点个数有严格的对应关系，而是为每个节点随机出一个层数(level)。
    - 比如，一个节点随机出的层数是3，那么就把它链入到第1层到第3层这三层链表中
    - 每一个节点的层数（level）是随机出来的，而且新插入一个节点不会影响其它节点的层数。插入操作只需要修改插入节点前后的指针，而不需要对很多节点都进行调整
    - 降低了插入操作的复杂度。实际上，这是skiplist的一个很重要的特性，这让它在插入性能上明显优于平衡树的方案
- 平均时间复杂度为O(logn)
- 一般查找问题的解法分为两个大类：一个是基于各种平衡树，一个是基于哈希表。但skiplist却比较特殊，它没法归属到这两大类里面。
- skiplist与平衡树、哈希表的比较
    - 从内存占用上来说，skiplist比平衡树更灵活一些。一般来说，平衡树每个节点包含2个指针（分别指向左右子树），而skiplist每个节点包含的指针数目平均为1/(1-p)，具体取决于参数p的大小
    - 从算法实现难度上来比较，skiplist比平衡树要简单得多
    - 平衡树的插入和删除操作可能引发子树的调整，逻辑复杂，而skiplist的插入和删除只需要修改相邻节点的指针，操作简单又快速
    - skiplist和各种平衡树（如AVL、红黑树等）的元素是有序排列的，而哈希表不是有序的。因此，在哈希表上只能做单个key的查找，不适宜做范围查找。
    - 在做范围查找的时候，平衡树比skiplist操作要复杂 ？
    - 查找单个key，skiplist和平衡树的时间复杂度都为O(log n)，大体相当；而哈希表在保持较低的哈希值冲突概率的前提下，查找时间复杂度接近O(1)，性能更高一些。所以我们平常使用的各种Map或dictionary结构，大都是基于哈希表实现的。
    
- 插入操作
    - 执行插入操作时计算随机数的过程，是一个很关键的过程，它对skiplist的统计特性有着很重要的影响。这并不是一个普通的服从均匀分布的随机数
        - 它的计算过程如下：          
          1. 首先，每个节点肯定都有第1层指针（每个节点都在第1层链表里）。
          2. 如果一个节点有第i层(i>=1)指针（即节点已经在第1层到第i层链表中），那么它有第(i+1)层指针的概率为p。
          3. 节点最大的层数不允许超过一个最大值，记为MaxLevel。
        - 这个计算随机层数的伪码如下所示：
        ```
          randomLevel()
              level := 1
              // random()返回一个[0...1)的随机数
              while random() < p and level < MaxLevel do
                  level := level + 1
              return level
         
        ```
        - randomLevel()的伪码中包含两个参数，一个是p，一个是MaxLevel。在Redis的skiplist实现中，这两个参数的取值为：
        ```
              p = 1/4
              MaxLevel = 32

        ```
        ```
          我们先来计算一下每个节点所包含的平均指针数目（概率期望）。节点包含的指针数目，相当于这个算法在空间上的额外开销(overhead)，可以用来度量空间复杂度。
          
          根据前面randomLevel()的伪码，我们很容易看出，产生越高的节点层数，概率越低。定量的分析如下：
          
          节点层数至少为1。而大于1的节点层数，满足一个概率分布。
          节点层数恰好等于1的概率为1-p。
          节点层数大于等于2的概率为p，而节点层数恰好等于2的概率为p(1-p)。
          节点层数大于等于3的概率为p2，而节点层数恰好等于3的概率为p2(1-p)。
          节点层数大于等于4的概率为p3，而节点层数恰好等于4的概率为p3(1-p)。
          ……
            
          因此，一个节点的平均(期望)层数（也即包含的平均指针数目）为 1/(1-p)

        ```
        - 其他随机方法
        ```
          int randomLevel() {
            int lv = 1;
            // MAXL = 32, S = 0xFFFF, PS = S * P, P = 1 / 4
            while ((rand() & S) < PS) ++lv;
            return min(MAXL, lv);
          }

        ```
        - 模拟以  的概率往上加一层，最后和上限值取最小。
          
    - 上下相邻两层链表上节点个数严格的2:1的对应关系，查找的时间复杂度可以降低到O(log n)，但插入删除操作时间复杂度会蜕化成O(n)
        - skiplist为了避免这一问题，它不要求上下相邻两层链表之间的节点个数有严格的对应关系，而是为每个节点随机出一个层数(level)
        - 每一个节点的层数（level）是随机出来的，而且新插入一个节点不会影响其它节点的层数。因此，插入操作只需要修改插入节点前后的指针，而不需要对很多节点都进行调整。这就降低了插入操作的复杂度。实际上，这是skiplist的一个很重要的特性，这让它在插入性能上明显优于平衡树的方案。
    - 实际上在插入之前也要先经历一个类似的查找过程，在确定插入位置后，再完成插入操作。
- 查询    
    - skiplist，翻译成中文，可以翻译成“跳表”或“跳跃表”，指的就是除了最下面第1层链表之外，它会产生若干层稀疏的链表，这些链表里面的指针故意跳过了一些节点（而且越高层的链表跳过的节点越多）
    - 这就使得我们在查找数据的时候能够先在高层的链表中进行查找，然后逐层降低，最终降到第1层链表来精确地确定数据位置。在这个过程中，我们跳过了一些节点，从而也就加快了查找速度
    - 实际应用中的skiplist每个节点应该包含key和value两部分。前面的描述中我们没有具体区分key和value，但实际上列表中是按照key进行排序的，查找过程也是根据key在比较。
    - 节点插入时随机出一个层数，仅仅依靠这样一个简单的随机数操作而构建出来的多层链表结构，能保证它有一个良好的查找性能吗？
- skiplist的算法性能分析
    - 空间复杂度 
        - 每个节点所包含的平均指针数目（概率期望）。节点包含的指针数目，相当于这个算法在空间上的额外开销(overhead)，可以用来度量空间复杂度
            - 一个节点的平均(期望)层数（也即包含的平均指针数目）为 1/(1-p)
            - 当p=1/4时，每个节点所包含的平均指针数目为1.33。这也是Redis里的skiplist实现在空间上的开销。   当p=1/2时，每个节点所包含的平均指针数目为2； 
            - 期望空间复杂度为O(n)
            - 总层数的均值(跳表的期望层数)为log<sub>1/p</sub> <sup>n</sup>， 在最坏的情况下，每一层有序链表等于初始有序链表，即跳表的 最差空间复杂度 为 O(nlogn)
    - 时间复杂度
        - 平均查找长度约等于(log<sub>1/p</sub> <sup>n</sup> -1 )/p ， 平均时间复杂度为O(logn)。
        - 跳表查询的期望时间复杂度为O(logn)  。
        - 在最坏的情况下，每一层有序链表等于初始有序链表，查找过程相当于对最高层的有序链表进行查询，即跳表查询操作的最差时间复杂度为O(n)  。
        - 插入操作和删除操作就是进行一遍查询的过程，途中记录需要修改的节点，最后完成修改。易得每一层至多只需要修改一个节点(新增或删除节点都不会影响其他节点高度，只需要修改插入或删除节点前后的指针)
            - 又因为跳表期望层数为 log<sub>1/p</sub> <sup>n</sup>，所以插入和修改的期望时间复杂度等于O(logn) + O(log<sub>1/p</sub> <sup>n</sup>) 也是O(logn)
- Redis 
    - 当p=1/2时，每个节点所包含的平均指针数目为2；
    - 当p=1/4时，每个节点所包含的平均指针数目为1.33。这也是Redis里的skiplist实现在空间上的开销
    - 计算随机层数的伪码
        ```
        randomLevel()
            level := 1
            // random()返回一个[0...1)的随机数
            while random() < p and level < MaxLevel do
                level := level + 1
            return level
        ```
    - 在Redis的skiplist实现中，这两个参数的取值为：
        ```
        p = 1/4
        MaxLevel = 32

        ```
- Redis 设计与实现
    - 《Skip lists: a probabilistic alternative to balanced trees》
    - 是一种随机化的数据，以有序的方式在层次化的链表中保存元素
    - 效率和平衡树媲美 —— 查找、删除、添加等操作都可以在对数期望时间下完成， 并且比起平衡树来说， 跳跃表的实现要简单直观得多