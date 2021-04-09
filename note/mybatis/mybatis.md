




### 组件
- 配置文件
    - Configuration
    - 拦截器 Interceptor
- Mapper
    - DAO 
    - 数据库操作接口
    - 数据库操作的映射文件，用于映射数据库的操作
- 映射SQL
    - xml映射文件或注解的方式
    - 用于建立数据库操作接口对应的SQL映射，SQL与对应的接口方法相关联
    - 调用接口会执行相应的SQL
    
### 原生运行机制
1. 根据配置(文件)生成 Configuration 对象
    - Mapper接口会生成对应的 MapperProxyFactory 对象
        - 在 MyBatis实现过程中并没有于动调用 configuration.addMapper方法，而是在映射文件读取过程中一旦解析到如<mapper namespace="Mapper.UserMapper”〉，便会自动进行类型映射的注册 
2. 根据 Configuration 对象构造 DefaultSqlSessionFactory implements SqlSessionFactory
3. 由 DefaultSqlSessionFactory 获得 DefaultSqlSession implements SqlSession
4. 由 DefaultSqlSession 和 Configuration 获得 Mapper接口 的代理对象(jdk动态代理)，调用该对象的方法会被代理为通过DefaultSqlSession执行对应的SQL，



    
- 每个DefaultSqlSession内都有独立的Executor，即每次创建DefaultSqlSession对象时都会新创建Executor对象
- 一级缓存与Executor对象关联，即与DefaultSqlSession对象关联
- 一级缓存作用域 
    - LocalCacheScope#SESSION
        - 同一个SqlSession内共享 
    - LocalCacheScope#STATEMENT
        - If localCacheScope=STATEMENT local session will be used just for statement execution, no data will be shared between two different calls to the same SqlSession. 
- 二级缓存以Mapper接口(xml namespace)为作用域，缓存粒度较大
    - 分布式应用可能会出现多个节点数据不一致
- 二级缓存可配置时间间隔刷新

### Spring整合
- springboot自动配置 
    - MybatisAutoConfiguration
- SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean
    - 包装了原生运行机制1、2
        1. 根据配置(文件)生成 Configuration 对象
            - Mapper接口会生成对应的MapperProxyFactory对象
        2. 根据Configuration 对象构造 DefaultSqlSessionFactory implements SqlSessionFactory
- MapperFactoryBean implements FactoryBean, InitializingBean
    - 使 Mapper接口 的代理对象 可以通过 BeanFactory#getBean 获取
    - 包装了原生运行机制3、4
        3. 由 DefaultSqlSessionFactory 获得 DefaultSqlSession implements SqlSession
        4. 由 DefaultSqlSession 和 Configuration 获得 Mapper接口 的代理对象(jdk动态代理)，调用该对象的方法会被代理为通过DefaultSqlSession执行对应的SQL，
    - 同时也用 SqlSessionTemplate 先代理了 DefaultSqlSession 的处理逻辑，最后再由 DefaultSqlSession 处理
        - 事务处理
- spring应用内通常情况下每次执行Mapper接口方法都会创建一个新的 DefaultSqlSession，但在开启事务下同一个事务会使用同一个DefaultSqlSession
    - (SqlSessionTemplate -> SqlSessionInterceptor -> SqlSessionUtils)

        
- 扫描特定的包，自动成批地创建 MapperFactoryBean        
    - 不需要对于每个接口都注册一个 MapperFactoryBean 类型的对应的 bean，但是，不在配置文件中注册并不代表这个 bean 不存在，而是在扫描的过程中通过编码的方式动态注册
    - ClassPathMapperScanner extends ClassPathBeanDefinitionScanner
        - 过滤 @Mapper
        - 生成 mapperFactoryBean
            - 自动装配 所有mapperFactoryBean引用同一个 SqlSessionTemplate 对象、同一个 SqlSessionFactory 对象
        - 触发方式
            - ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor
                - MybatisAutoConfiguration.MapperScannerRegistrarNotFoundConfiguration (@Configuration \ Import({MybatisAutoConfiguration.AutoConfiguredMapperScannerRegistrar.class}))
                    - AutoConfiguredMapperScannerRegistrar implements ImportBeanDefinitionRegistrar
                        - ImportBeanDefinitionRegistrar#registerBeanDefinitions
                        
            - MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor
                - spring初始化过程会保证BeanDefinitionRegistryPostProcessor接口方法被调用

- 游标查询
    - Mapper方法 ：返回一个Cursor类型的包装对象
    - 指定 fetchSize
    - 注意要使用@Transactional注解来维持数据库连接，否则当recordMapper查询结束后数据库连接就会断开，Cursor就取不到数据了
        - 或者原生方式使用Mybatis 
        