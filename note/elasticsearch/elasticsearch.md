

- 全文检索功能
- Elasticsearch 精通索引和查询文本
- 可以存储各种类型的数据，比如Geo地理类型的数据

- 基于 Apache Lucene 


- MySQL-全文索引
    - 通过关键字的匹配来进行查询过滤，那么就需要基于相似度的查询，而不是原来的精确数值比较。全文索引就是为这种场景设计的
    - 全文解析器ngram
        - ngram全文解析器能够对文本进行分词，每个单词是连续的n个字的序列
        - ngram_token_size
    - 全文索引
    - 检索模式
        - 自然语言检索
    - 检索
        - MATCH ( content) AGAINST ('topic' IN NATURAL LANGUAGE MODE)
    - [https://cloud.tencent.com/developer/article/1686101]