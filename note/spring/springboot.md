

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