#### 本地事务
- 数据库提供的事务机制（如果数据库支持事务的话）保证对库中记录所进行的操作的可靠性，这里的可靠性有四种语义：ACID
- 单数据源事务及其 ACID 特性
    - MySQL 的 InnoDB 引擎通过 Undo Log + Redo Log + ARIES 算法来实现