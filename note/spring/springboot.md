

#### springboot jar 如何加载到应用程序依赖jar包中的class
- classpath
    - classpath是JVM用到的一个环境变量，它用来指示JVM如何搜索class 去哪搜索对应的class文件
    - 是一组目录的集合
    - 启动JVM时设置classpath变量(java命令传入-classpath或-cp参数) 或 系统环境变量中设置classpath环境变量
- java  
    - 没有设置系统环境变量，也没有传入-cp参数，那么JVM默认的classpath为.，即当前目录
- java -jar 
    - .class文件，散落在各层目录中，将其打成一个jar文件
    - zip格式的压缩文件，jar包相当于目录。如果我们要执行一个jar包的class，就可以把jar包放到classpath中
        ```
          appledeiMac:target apple$ java -cp ./spring-practice-0.0.1-SNAPSHOT.jar.original com.zlb.spring.practice.Test
          test success
          appledeiMac:target apple$ java  com.zlb.spring.practice.Test
          错误: 找不到或无法加载主类 com.zlb.spring.practice.Test
          appledeiMac:target apple$ 
        ```
    - jar包还可以包含一个特殊的/META-INF/MANIFEST.MF文件，MANIFEST.MF是纯文本，可以指定Main-Class和其它信息
        - JVM会自动读取这个MANIFEST.MF文件，如果存在Main-Class，就不必在命令行指定启动的类名
            - java -jar XXX.jar
    - 可以在MANIFEST.MF文件里配置classpath
    - 默认情况下，JDK提供的ClassLoader只能识别Jar中的class文件以及加载classpath下的其他jar包中的class文件。对于在jar包中的jar包是无法加载的
        - jar里的资源分隔符是!/，在JDK提供的JarFile URL只支持一个’!/‘，而Spring boot扩展了这个协议，让它支持多个’!/‘，就可以表示jar in jar、jar in directory、fat jar的资源了

- springboot jar
    - springboot打包的生成jar包含了应用程序依赖的jar 即jar in jar
    - 插件 spring-boot-maven-plugin
        - 打包的jar包含了应用的依赖jar 和 Spring Boot相关classs
        - 替换 Main-Class: org.springframework.boot.loader.JarLauncher
            - 通过spring-boot-loader#JarLauncher类启动我们的Spring Boot的入口类(SpringPracticeApplication)，从而实现spring boot 项目的jar启动
        - Start-Class: com.zlb.spring.practice.SpringPracticeApplication
    - ClassLoader
        - Bootstrap ClassLoader（加载JDK的/lib目录下的类）
        - Extension ClassLoader（加载JDK的/lib/ext目录下的类）
        - Application ClassLoader（程序自己classpath下的类）
    - 采用双亲委派机制 无法加载应用程序所依赖的jar包
    - 自定义类加载机制
        - 自定义类加载器LaunchedURLClassLoader extends URLClassLoader 来加载jar in jar； 将fat jar包依赖的所有类和资源 包括应用程序的class文件和依赖的jar包中class文件都转化为urls传递给父类java.net.URLClassLoader
        - 使用该类加载器加载 SpringPracticeApplication 类，然后调用其main方法
        - 将该自定义类加载器设置为 线程上下文类加载器 
             - 在spring容器启动初始化容器时通过该线程上下文类加载器加载依赖jar包中的类和应用程序本身的类
    - @see org.springframework.util.ClassUtils#getDefaultClassLoader() 优先获取线程上下文类加载器 contextClassLoader
    
- starter
    - 让模块开发更加独立化，相互间依赖更加松散以及可以更加方便地集成
    - 一个标准的Starter， 独立业务模块， 这个模块并不是自己部署而是运行在依赖它的主模块的主函数中
        - pom
            ```
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-autoconfigure</artifactId>
                </dependency>
            
            ```
        - 添加自动配置项
            - HelloServiceAutoConfiguration
                - 在 ComponentScan 注解中加入这个模块的容器扫描路径
                - 它存在的目的仅仅是通过注解进行配置的声明
        - 声明这个配置文件的路径   
            - 在 Spring 的根路径下建立 META-INF/spring.factories 文件
                - org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.spring.study.HelloServiceAutoConfiguration
                  
    - 使用
        - 直接引用依赖
        
- 自动配置
    - MybatisAutoConfiguration
        - 

- Starter 自动化配置原理
    - springboot 的全局开关 @SpringBootApplication 
        - -> @org.springframework.boot.autoconfigure.EnableAutoConfiguration.EnableAutoConfiguration
            - -> org.springframework.boot.autoconfigure.AutoConfigurationImportSelector  (DeferredImportSelector extends org.springframework.context.annotation.ImportSelector)
                - AutoConfigurationImportSelector 与 Spring 的整合过程，在这个调用链中最核心的就是 Spring Boot使用 了 Spring 提供的 BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor 扩展点并实现了 ConfigurationClassPostProcessor 类(ConfigurationClassPostProcessor通过 ConfigurationClassParser 处理ImportSelector)，从而实现了 spring之上的一系列逻辑扩展
            - Spring启动的时候会扫描所有 JAR 中的 spring.factories定义的类
            - ConfigurationClassParser 会处理自动配置类 如处理 @ComponentScan、@Configuration等注解
- @Conditional Condition
    - 根据满足的某一个特定条件创建一个特定的bean。 
    - 比方说， 当某一个 JAR 包在一个类路径下 的时候，会自动配置一个或多个 bean;或者只有某个 bean 被创建后才会创建另外一个 bean。 
    - 总的来说，就是根据特定条件来控制 bean 的创建行为，这样我们可以利用这个特性进行一些自动的配置。
    - 属性自动化配置 @ConditionalOnProperty  OnPropertyCondition
        - 尝试使用 PropertyResolver 来验证对应的属性是 否存在， 如果不存在则验证不通过，因为 PropertyResolver 中包含了所有的配置属性信息 
        - 属性配置
            - Value注解直接将属性赋值给类的变量
            - 配置文件 
        - PropertyResolver初始化逻辑
            - Spring的属性处理逻辑
                - 初始化逻辑以实现 PropertySourcesPlaceholderConfigurer(BeanFactoryPostProcessor) 类的 postProcessBeanFactory 函数作为入口
    - ConditionalOnBean \ ConditionalOnMissingBean
        - Bean的存在与否作为条件
    - ConditionalOnClass\ConditionalOnMissingClass
        - Class的存在与否作为条件 
- 集成 Tomcat
    - ServletWebServerApplicationContext
        - 重写onRefresh、finishRefresh 启动tomcat
