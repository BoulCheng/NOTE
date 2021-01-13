



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
    
    

- 异步注解@Async为什么会产生循环依赖问题；异步注解是不是一定会产生循环依赖问题；异步注解产生的循环依赖如何解决，解决的原理是什么；为什么spring要这么设计异步注解    
```
Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2020-12-15 10:23:56,206 [main] ERROR o.s.boot.SpringApplication [SpringApplication.java : 826] - Application run failed
org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'peekServiceImpl': Bean with name 'peekServiceImpl' has been injected into other beans [generalPeekManager] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching - consider using 'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:624)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:517)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:323)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:321)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:879)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:878)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:550)
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:141)
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:747)
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:397)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1226)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1215)
	at so.dian.datacenter.peekdata.DemApplication.main(DemApplication.java:26)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:48)
	at org.springframework.boot.loader.Launcher.launch(Launcher.java:87)
	at org.springframework.boot.loader.Launcher.launch(Launcher.java:51)
	at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:52)
```

```
java -Dspring.profiles.active=pre -Dfile.encoding=UTF8 -Dsun.jnu.encoding=UTF8 -XX:HeapDumpPath=/home/admin/logs/oomDump.hprof -XX:+HeapDumpOnOutOfMemoryError -XX:+HeapDumpBeforeFullGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -XX:+PrintTenuringDistribution -Xloggc:/home/admin/logs/gc_%p.log -Drocketmq.client.logRoot=/home/admin/logs/rocketmq.log -Drocketmq.client.logLevel=WARN -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Xms1331m -Xmx1331m -XX:+UseParallelOldGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -XX:GCTimeRatio=99 -XX:AdaptiveSizePolicyWeight=90 -XX:MaxMetaspaceSize=256m -XX:ParallelGCThreads=1 -Djava.util.concurrent.ForkJoinPool.common.parallelism=1 -XX:CICompilerCount=2 -XX:+ExitOnOutOfMemoryError -cp . -jar /deployments/peek-data.jar
```