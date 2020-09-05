##### redisObject

- 为了在同一个dict内能够存储不同类型的value，这就需要一个通用的数据结构，这个通用的数据结构就是robj
```
typedef struct redisObject {
    unsigned type:4; /* 对象的数据类型。占4个bit。可能的取值有5种：OBJ_STRING, OBJ_LIST, OBJ_SET, OBJ_ZSET, OBJ_HASH，分别对应Redis对外暴露的5种数据结构(字符串(String), 哈希(Hash), 列表(list), 集合(sets) 和 有序集合(sorted sets)等类型)*/
    unsigned encoding:4; /* 对象的内部表示方式（也可以称为编码） 对于同一个type，还可能对应不同的encoding，这说明同样的一个数据类型，可能存在不同的内部表示方式。而不同的内部表示，在内存占用和查找性能上会有所不同*/
    unsigned lru:LRU_BITS; /* lru time (relative to server.lruclock) */
    int refcount; /* 引用计数。它允许robj对象在某些情况下被共享 */
    void *ptr; /* 数据指针。指向真正的数据。比如，一个代表string的robj，它的ptr可能指向一个sds结构；一个代表list的robj，它的ptr可能指向一个quicklist*/
} robj;

```

- robj的作用：
    - 为多种数据类型提供一种统一的表示方式
    - 允许同一类型(type)的数据采用不同的内部表示，从而在某些情况下尽量节省内存。
    - 支持对象共享和引用计数。当对象被共享的时候，只占用一份内存拷贝，进一步节省内存。
- robj的引用计数操作
    ```
  void incrRefCount(robj *o) {
      o->refcount++;
  }
  
  void decrRefCount(robj *o) {
      if (o->refcount <= 0) serverPanic("decrRefCount against refcount <= 0");
      if (o->refcount == 1) {
          switch(o->type) {
          case OBJ_STRING: freeStringObject(o); break;
          case OBJ_LIST: freeListObject(o); break;
          case OBJ_SET: freeSetObject(o); break;
          case OBJ_ZSET: freeZsetObject(o); break;
          case OBJ_HASH: freeHashObject(o); break;
          default: serverPanic("Unknown object type"); break;
          }
          zfree(o);
      } else {
          o->refcount--;
      }
  }

    ```
    - 将引用计数减1的操作decrRefCount。如果只剩下最后一个引用了（refcount已经是1了），那么在decrRefCount被调用后，整个robj将被释放。
    - Redis的del命令就依赖decrRefCount操作将value释放掉。
    
- robj所表示的就是Redis对外暴露的第一层面的数据结构：string, list, hash, set, sorted set，而每一种数据结构的底层实现所对应的是哪个（或哪些）第二层面的数据结构（dict, sds, ziplist, quicklist, skiplist, 等），则通过不同的encoding来区分。可以说，robj是联结两个层面的数据结构的桥梁
     