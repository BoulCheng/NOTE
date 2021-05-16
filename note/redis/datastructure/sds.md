#### Simple Dynamic String


- 二进制数据
    - 以字符串 ASCII
        - 支持setbit操作
    - 以long
- 动态扩展


- 特性
    - 可动态扩展内存。sds表示的字符串其内容可以修改，也可以追加。在很多语言中字符串会分为mutable和immutable两种，显然sds属于mutable类型的。
    - 二进制安全（Binary Safe）。sds能存储任意二进制数据，而不仅仅是可打印字符。
    - 与传统的C语言字符串类型兼容
    
    
- sds与string的关系
    - 确切地说，string在Redis中是用一个robj来表示的。
    - 用来表示string的robj可能编码成3种内部表示：OBJ_ENCODING_RAW, OBJ_ENCODING_EMBSTR, OBJ_ENCODING_INT。其中前两种编码使用的是sds来存储，最后一种OBJ_ENCODING_INT编码直接把string存成了long型。
    - 在对string进行incr, decr等操作的时候，如果它内部是OBJ_ENCODING_INT编码，那么可以直接进行加减操作；如果它内部是OBJ_ENCODING_RAW或OBJ_ENCODING_EMBSTR编码，那么Redis会先试图把sds存储的字符串转成long型，如果能转成功，再进行加减操作。
    - 对一个内部表示成long型的string执行append, setbit, getrange这些命令，针对的仍然是string的值（即十进制表示的字符串），而不是针对内部表示的long型进行操作。比如字符串”32”，如果按照字符数组来解释，它包含两个字符，它们的ASCII码分别是0x33和0x32。当我们执行命令setbit key 7 0的时候，相当于把字符0x33变成了0x32，这样字符串的值就变成了”22”。而如果将字符串”32”按照内部的64位long型来解释，那么它是0x0000000000000020，在这个基础上执行setbit位操作，结果就完全不对了。因此，在这些命令的实现中，会把long型先转成字符串再进行相应的操作
    - 值得一提的是，append和setbit命令的实现中，都会最终调用到db.c中的dbUnshareStringValue函数，将string对象的内部编码转成OBJ_ENCODING_RAW的（只有这种编码的robj对象，其内部的sds 才能在后面自由追加新的内容），并解除可能存在的对象共享状态
    
    
    
    
```
appledeiMac:src apple$ redis-cli
127.0.0.1:6379>
127.0.0.1:6379> setbit a 0 1
(integer) 0
127.0.0.1:6379> get a
"\x80"
127.0.0.1:6379> bitcount a
(integer) 1
127.0.0.1:6379>
127.0.0.1:6379> bitset a 7 1
(error) ERR unknown command `bitset`, with args beginning with: `a`, `7`, `1`,
127.0.0.1:6379> setbit a 7 1
(integer) 0
127.0.0.1:6379>
127.0.0.1:6379> get a
"\x81"
127.0.0.1:6379> bitcount a
(integer) 2
127.0.0.1:6379>
127.0.0.1:6379> setbit a 128 1
(integer) 0
127.0.0.1:6379> get a
"\x81\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x80"
127.0.0.1:6379> bitcount a
(integer) 3
127.0.0.1:6379>
```    