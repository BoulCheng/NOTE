- SPI 全称为 Service Provider Interface，是一种服务发现机制。SPI 的本质是将接口实现类的全限定名配置在文件中，并由服务加载器读取配置文件，加载实现类。这样可以在运行时，动态为接口替换实现类。正因此特性，我们可以很容易的通过 SPI 机制为我们的程序提供拓展功能

- vs jdk-spi
    - jdk-spi 加载全部实现类  ； Dubbo SPI 是通过键值对的方式进行配置，这样我们可以按需加载指定的实现类
    - jdk-spi 没有IOC功能 ； Dubbo SPI 除了支持按需加载接口实现类，还增加了 IOC 和 AOP 等特性
    - 自适应拓展 有些拓展并不想在框架启动阶段被加载，而是希望在拓展方法被调用时，根据运行时参数进行加载，通过代理
        - 自适应拓展机制的实现逻辑比较复杂，首先 Dubbo 会为拓展接口生成具有代理功能的代码
        - 获取url中的指定参数再通过 SPI 加载具体的实现类
        