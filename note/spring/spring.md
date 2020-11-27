



#### bean
- scope
    - Stateless无状态用单例Singleton模式，Stateful有状态就用原型Prototype模式。 被循环依赖的bean如果是Prototype作用域spring会抛出循环依赖的异常
- DI
    - xml
        - setter
        - constructor
            ```
                  <constructor-arg index="0" ref="springDao"></constructor-arg>  
                  <constructor-arg index="1" ref="user"></constructor-arg>  
            ```
    - 注解
        - @Autowired
            - Enabling @Autowired Annotations
            - Using @Autowired
                - 属性
                - setter方法
                - 构造函数
            - 默认按照 Bean 的类型进行装配
            - @Qualifier 会将默认的按 Bean 类型装配修改为按 Bean 的实例名称装配
        - @Resource(属性：name 和 type)
            - 默认按照 Bean 实例名称进行装配 
            - 如果都不指定name 和 type，则先按 Bean 实例名称装配，如果不能匹配，则再按照 Bean 类型进行装配；如果都无法匹配，则抛出 NoSuchBeanDefinitionException 异常
    - 自动装配
        - bean 标签有一个autowire属性
            - setter方法注入
            - 构造函数注入
    - 静态工厂方法注入 factory-method
    - 实例工厂方法注入 factory-bean factory-method