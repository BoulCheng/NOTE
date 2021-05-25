


#### todo
- application.yml bootstrap.yml
- spring.profiles

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
                   @Autowired
                    public void setService(Service service) {
                        this.service = service;
                    }
                - 构造函数
                    - Spring建议”总是在您的bean中使用构造函数建立依赖注入。总是使用断言强制依赖”。
                        - 因为Java类会先执行构造方法，然后再给注解了@Autowired 的属性注入值
                ```
              @Autowired
              public Constructor(Service service) {
                 this.service = service;
              }
              ```
            - 默认按照 Bean 的类型进行装配
            - @Qualifier 
                - @Autowired默认使用byType来装配属性，如果匹配到类型的多个实例，再通过byName来确定Bean
            - @Primary \ @Priority
                - 主和优先级
        - @Resource(属性：name 和 type)
            - 默认按照 Bean 实例名称进行装配 
            - 如果都不指定name 和 type，则先按 Bean 实例名称装配，如果不能匹配，则再按照 Bean 类型进行装配；如果都无法匹配，则抛出 NoSuchBeanDefinitionException 异常
    - 自动装配
        - bean 标签有一个autowire属性 byType、byName、constructor(与Bean的构造器入参具有相同类型的其他Bean自动装配到Bean构造器的对应入参中)
            - setter方法注入
            - 构造函数注入
        - autowireConstructor()
    - 静态工厂方法注入 factory-method
    - 实例工厂方法注入 factory-bean factory-method
    
    

    


#### 注解

- @AliasFor
    - AliasFor可以定义一个注解中的两个属性互为别名
    - 不仅是一个注解内不同属性可以声明别名，不同注解的属性也可以声明别名（注解可以作用于注解）
        ```
          @Component
          public @interface Service {
              @AliasFor(
                  annotation = Component.class
              )
              String value() default "";
          }
      
      
          @Service("serviceAlias")
          public class ServiceAlias {
          
              public static void main(String[] args) {
                  Component component = AnnotationUtils.getAnnotation(ServiceAlias.class, Component.class);
                  System.out.println(component);//@org.springframework.stereotype.Component(value=)
                
                Component component2 = AnnotatedElementUtils.getMergedAnnotation(ServiceAlias.class, Component.class);
                  System.out.println(component2);        //@org.springframework.stereotype.Component(value=serviceAlias)
    
            
              }
          }
      
        ```
        - @Service#value为@Component#value的别名，@Service#value的值可以映射到@Component#value。（这里将@Service，@Component看做一种特殊的继承关系，@Component是父注解，@Service是子注解，@Service#value覆盖@Component#value）
        - 虽然ServiceAlias上只有@Service，但通过AnnotationUtils.getAnnotation方法会解析得到@Component，而通过AnnotatedElementUtils.getMergedAnnotation方法还可以将@Service#value的值赋给@Component#value
         - Spring内部实现并不复杂，在java中，注解是使用动态代理类实现，Spring中同理
         - @Repeatable表示当配置了多个@ComponentScan时，@ComponentScan可以被@ComponentScans代替
              ```
              @ComponentScan("com.binecy.bean")
              @ComponentScan("com.binecy.service")
              public class ComponentScansService {
                  public static void main(String[] args) {
                      ComponentScans scans = ComponentScansService.class.getAnnotation(ComponentScans.class);
                      for (ComponentScan componentScan : scans.value()) {
                          System.out.println(componentScan.value()[0]);
                      }
                  }
              }
              
              ```
           

- @Import 
    - 没有把某个类注入到IOC容器中，但在运用的时候需要获取该类对应的bean，此时就需要用到@Import注解
    - 功能类似 @Bean  

- @EnableConfigurationProperties ConfigurationProperties
    - 两种方式都是将被  @ConfigurationProperties 修饰的类，加载到 Spring Env 中
        - @ConfigurationProperties + @Component
        - @ConfigurationProperties + EnableConfigurationProperties           
           
           
               
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

- 用---实现多文件内容的分割，application.yml中用spring.profiles.active: 实现指定的文件
- (todo)异步注解@Async为什么会产生循环依赖问题；异步注解是不是一定会产生循环依赖问题；异步注解产生的循环依赖如何解决，解决的原理是什么；为什么spring要这么设计异步注解

- springboot启动(todo)

```
java -Dspring.profiles.active=pre -Dfile.encoding=UTF8 -Dsun.jnu.encoding=UTF8 -XX:HeapDumpPath=/home/admin/logs/oomDump.hprof -XX:+HeapDumpOnOutOfMemoryError -XX:+HeapDumpBeforeFullGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -XX:+PrintTenuringDistribution -Xloggc:/home/admin/logs/gc_%p.log -Drocketmq.client.logRoot=/home/admin/logs/rocketmq.log -Drocketmq.client.logLevel=WARN -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Xms1331m -Xmx1331m -XX:+UseParallelOldGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -XX:GCTimeRatio=99 -XX:AdaptiveSizePolicyWeight=90 -XX:MaxMetaspaceSize=256m -XX:ParallelGCThreads=1 -Djava.util.concurrent.ForkJoinPool.common.parallelism=1 -XX:CICompilerCount=2 -XX:+ExitOnOutOfMemoryError -cp . -jar /deployments/peek-data.jar
```

- 模块组成
    - Core container
        - spring-core
            - 提供了框架的基本组成部分，包括控制反转（Inversion of Control，IOC）和依赖注入（Dependency Injection，DI）功能
        - spring-beans
            - 提供了BeanFactory
        - spring-context   
            - 建立在Core和Beans模块的基础之上，提供一个框架式的对象访问方式
    - AOP
        - spring-aop
            -  AOP 面向切面的编程实现
        - spring-aspects
            - 提供了与AspectJ的集成功能，AspectJ是一个功能强大且成熟的AOP框架
    - 数据访问与集成
        - spring-jdbc
        - spring-orm
            - JPA
        - spring-tx
    - Web
        - spring-websocket
        - spring-web
        - spring-webmvc
        - spring-webflux
    - Messaging
    - Test
- IOC
    - 指创建对象的控制权转移给Spring框架
    - DI
        - 将对象之间的依赖关系交由Spring框架，注入对象的依赖
    
- aop
    - 用于将那些与业务无关，但却对多个对象产生影响的公共行为和逻辑，抽取并封装为一个可重用的模块，这个模块被命名为“切面”（Aspect）
    - 代理模式
        - AspectJ是静态代理，也称为编译时增强；在编译阶段并将AspectJ(切面)织入到Java字节码中生成AOP代理类，运行的时候就是增强之后的AOP对象
        - Spring AOP使用的动态代理，在特定的切点做了增强处理，并回调原对象的方法
            - JDK动态代理
            - CGLIB动态代理
                - 可以在运行时动态的生成指定类的一个子类对象，并覆盖其中特定方法并添加增强代码，从而实现AOP。CGLIB是通过继承的方式做的动态代理，因此如果某个类被标记为final，那么它是无法使用CGLIB做动态代理的
        - 相对来说AspectJ的静态代理方式具有更好的性能，但是AspectJ需要特定的编译器进行处理，而Spring AOP则无需特定的编译器处理
        
    - IoC让相互协作的组件保持松散的耦合，而AOP编程把遍布于应用各层的功能分离出来形成可重用的功能组件
    - 概念
        - 切点（Pointcut）：切点用于定义 要对哪些连接点 Join point进行拦截。
            - 切点分为execution方式和annotation方式。execution方式可以用路径表达式指定对哪些方法拦截，比如指定拦截add*、search*。annotation方式可以指定被哪些注解修饰的代码进行拦截
        - 通知（Advice）：指要在连接点（Join Point）上执行的动作，即增强的逻辑
            - 通知有各种类型，包括Around、Before、After、After returning、After throwing
            - Around
                - 可以选择是否继续执行连接点或直接返回它们自己的返回值或抛出异常来结束执行
        - 引入（Introduction）：添加方法或字段到被代理的对象
            - 向现有的类添加新方法或属性
            - 在无需修改这些现有的类的情况下，让它们具有新的行为和状态
        - 参照切点定义找到相应的连接点
        - 将相应的通知织入到切点指定的连接点
        - 同一个Aspect，不同advice的执行顺序：
          
          （1）没有异常情况下的执行顺序：
          
          around before advice
          before advice
          target method 执行
          around after advice
          after advice
          afterReturning
          
          （2）有异常情况下的执行顺序：
          
          around before advice
          before advice
          target method 执行
          around after advice
          after advice
          afterThrowing
          java.lang.RuntimeException: 异常发生
        - 不同aspect，advice的执行顺序
        入操作（Around（接入点执行前）、Before），优先级越高，越先执行；
        一个切面的入操作执行完，才轮到下一切面，所有切面入操作执行完，才开始执行接入点；
        出操作（Around（接入点执行后）、After、AfterReturning、AfterThrowing），优先级越低，越先执行。
        一个切面的出操作执行完，才轮到下一切面，直到返回到调用点
    
- spring 容器启动
    - ApplicationContext
        - org.springframework.context.annotation.AnnotationConfigApplicationContext
        - org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext
     


- BeanFactory  VS ApplicationContext
    - BeanFactory 是Spring里面最底层的接口，是IoC的核心，定义了IoC的基本功能，包含了各种Bean的定义、加载、实例化，依赖注入和生命周期管理。ApplicationContext接口作为BeanFactory的子类，除了提供BeanFactory所具有的功能外，还提供了更完整的框架功能如资源文件访问、载入多个（有继承关系）上下文    
    - BeanFactroy采用的是延迟加载形式来注入Bean的，只有在使用到某个Bean时(调用getBean())，才对该Bean进行加载实例化
        - 直至第一次使用调用getBean方法才会发现错误
    - ApplicationContext，它是在容器启动时，一次性创建了所有的Bean 即在容器启动时触发调用getBean()
        - 在容器启动时，可以发现Spring中存在的配置错误，这样有利于检查所依赖属性是否注入    
        - ApplicationContext启动后预载入所有的单实例Bean
    - 相对于BeanFactory，ApplicationContext 唯一的不足是占用内存空间，当应用程序配置Bean较多时，程序启动较慢    
        
- Bean的生命周期
    - 循环引用
        - 循环依赖问题在Spring中主要有三种情况：
          （1）通过构造方法进行依赖注入时产生的循环依赖问题。
          （2）通过setter方法进行依赖注入且是在多例（原型）模式下产生的循环依赖问题。
          （3）通过setter方法进行依赖注入且是在单例模式下产生的循环依赖问题
        - 在Spring中，只有第（3）种方式的循环依赖问题被解决了，其他两种方式在遇到循环依赖问题时都会产生异常。这是因为
           1 第一种构造方法注入的情况下，在new对象的时候就会堵塞住了，其实也就是”先有鸡还是先有蛋“的历史难题。
           2 第二种setter方法（多例）的情况下，每一次getBean()时，都会产生一个新的Bean，如此反复下去就会有无穷无尽的Bean产生了，最终就会导致OOM问题的出现
        - 三级缓存到二级缓存的过程可能会触发生成代理对象或者直接把还未完全进行属性设置和初始化的bean实例引用从三级缓存放入二级缓存   
    	- 单例bean 三级缓存
    	    - 工厂对象缓存 用于解决(aop)代理对象循环引用问题, 解决循环引用时 A -> B -> A ， B在属性设置时引用A的代理对象，而非真实对象；三级缓存是为了解决(aop)代理下的循环引用
    	    - 在bean设置属性和初始化的过程中可能会被其他bean引用，此时被循环引用的过程会触发代理对象的创建，没有发生循环引用时代理对象在bean初始化完成后生成
    	    
- 三级缓存的根本原因
本质上采用二级缓存也完全可以解决单纯的代理问题 bean实例化后直接创建代理放入二级缓存即可，不管有没有循环依赖，都提前创建好代理对象，并将代理对象放入缓存，出现循环依赖时，其他对象直接就可以取到代理对象并注入。

采用三级缓存更多是设计上的考虑
        不提前创建好代理对象，在出现循环依赖被其他对象注入时，才实时生成代理对象。这样在没有循环依赖的情况下，Bean就可以按着Spring设计原则的步骤来创建
如果要使用二级缓存解决循环依赖，意味着Bean在构造完后就创建代理对象，这样违背了Spring设计原则。Spring结合AOP和Bean的生命周期，是在Bean创建完全之后通过AnnotationAwareAspectJAutoProxyCreator这个后置处理器来完成的，在这个后置处理的postProcessAfterInitialization方法中对初始化后的Bean完成AOP代理。如果出现了循环依赖，那没有办法，只有给Bean先创建代理，但是没有出现循环依赖的情况下，设计之初就是让Bean在生命周期的最后一步完成代理而不是在实例化后就立马完成代理

            
- bean的作用域            
    - prototype：每次getBean请求创建一个新实例对象

- 事务
    - 嵌套事务
        - mysql savepoint
- 设计模式
    - 适配器模式
        - Advice 
    - 装饰器模式
             
#### 循环引用
- Spring对Bean的实例化、初始化顺序，若没有特别干预的情况下，它和类名字母排序有关
- Async
    - 若按照正常Spring容器会先初始化A，启动就肯定是不会报错的，不会发生循环引用的错误
    - 只有用了@Async注解的类在类创建中被其他类循环引用了 才会发生循环引用的错误，源码可以看出这一点
```
@Service
public class A implements AInterface{
    @Autowired
    private BInterface b;
    @Override
    public void funA() {
    }
}

@Service
public class B implements BInterface {
    @Autowired
    private AInterface a;
    @Async // 写在B的方法上  这样B最终会被创建代理对象
    @Override
    public void funB() {
        a.funA();
    }
}
```
- @Async 循环引用解决
    - 标注有@Lazy注解完成注入的时候，最终注入只是一个此处临时生成的代理对象，只有在真正执行目标方法的时候才会去容器内拿到真是的bean实例来执行目标方法
    - 通过@Lazy注解能够解决很多情况下的循环依赖问题，它的基本思想是先'随便'给你创建一个代理对象先放着，等你真正执行方法的时候再实际去容器内找出目标实例执行
    - 到容器中拿的还是那个唯一的单例bean(scope为单例的情况下)
### 代理
- 自调用失效
    - AopContext.currentProxy()
        - 使用AopContext.currentProxy();方式解决同类方法调用的方案
    - 自己(@Autowired)注入自己
    
    

### tx
- 事务public方法 
-  dk代理由于接口原因只能代理public方法？ 
子类重写父类方法时，方法的访问权限不能小于原访问权限，在接口中，方法的默认权限就是public，所以子类重写后只能是public
1.接口必须要具体类实现才有意义，所以必须是public。
2.接口中的属性对所有实现类只有一份，所以是static。
3.要使实现类为了向上转型成功，所以必须是final的。
1、接口是一种约束和规范，是一种更加更高级的抽象类，抽象类的方法必须是公开的，因为要给人继承和使用啊，不用public，别人怎么看得到，所以在接口实现时，定义的方法修饰符必须是public；因此子类在实现接口重写方法时的修饰符必须是public。
2、另外再扩展一下，接口中没有变量（既然是约束和规范，怎么能够定义一个大家都可以改的东西呢），只能是常量，接口中定义常量默认的修饰符为public static final

### 注解实现
AnnotationMetadata注解的实现
在AnnotationMetadata语义上,基于Java反射StandardAnnotationMetadata和AnnotationMetadataReadingVisitor保持一致。基于Java反射API实现必然需要反射的Class被ClassLoader加载，当指定Java Package扫描Spring模式注解时,StandardAnnotationMetadata显然不适应。
因为应用不需要将指定Package下的Class全部加载。基于ASM实现的AnnotationMetadataReadingVisitor更适合这种场景，解释了为什么该类出现ClassPathScanningCandidateComponentProvider实现中