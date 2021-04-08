
- 门面模式是软件工程中常用的一种软件设计模式，也被称为正面模式、外观模式。它为子系统中的一组接口提供一个统一的高层接口，使得子系统更容易使用


- 接口
    - Slf4j
        - 在编译期间，静态绑定本地的 Log 库，因此可以在 Osgi 中正常使用。它是通过查找类路径下 org.slf4j.impl.StaticLoggerBinder，然后在 StaticLoggerBinder 中进行绑定
    - Commons Logging
        - 动态查找机制，在程序运行时，使用自己的 ClassLoader 寻找和载入本地具体的实现。详细策略可以查看 commons-logging-*.jar 包中的 org.apache.commons.logging.impl.LogFactoryImpl.java 文件。由于 Osgi 不同的插件使用独立的 ClassLoader，Osgi 的这种机制保证了插件互相独立, 其机制限制了 Commons Logging 在 Osgi 中的正常使用

- 接口实现
    - Logback
    - Log4j2
    - Slf4j
    - Jul

- Commons Logging 和 Slf4j 是日志门面，Log4j 和 Logback 则是具体的日志实现方案。可以简单的理解为接口与接口的实现，调用者只需要关注接口而无需关注具体的实现，做到解耦
    - 比较常用的组合使用方式是 Slf4j 与 Logback 组合使用，Commons Logging 与 Log4j 组合使用
    - Log4j2 是 Slf4j 的升级
    - Jul (Java Util Logging) 是具体的日志实现方案
    
- Slf4j + Logback 的优势
    - 由于 Slf4j 在编译期间，静态绑定本地的 LOG 库使得通用性要比 Commons Logging 要好
    - Logback 拥有更好的性能
    - Logback 文档免费
    
- Slf4j
    - 对于不同的日志实现方案(例如 Logback，Log4j…)，封装出不同的桥接组件(例如 logback-classic-version.jar，slf4j-log4j12-version.jar)，这样使用过程中可以灵活的选取项目里的日志实现