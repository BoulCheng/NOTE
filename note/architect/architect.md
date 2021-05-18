


- 高可用
    - 
    - 






- 高并发系统设计的目标有三个：高性能、高可用，以及高可扩展
    
- 性能指标
    - 平均响应时间 RT
    - TP90、TP99等分位值：将响应时间按照从小到大排序，TP90表示排在第90分位的响应时间， 分位值越大，对慢请求越敏感
    - 吞吐量
    - QPS tps 有读多写少的信息流场景、有读多写多的交易场景
- 可用性指标
- 可扩展性指标
    - 扩展性 = 性能提升比例 / 机器增加比例，理想的扩展能力是：资源增加几倍，性能提升几倍。通常来说，扩展能力要维持在70%以上。
    - 服务集群、数据库、缓存和消息队列等中间件、负载均衡    


- 单机
    - 计算和 IO两个角度
    - 通过增加内存、CPU核数、存储容量、或者将磁盘升级成SSD等堆硬件的方式来提升
    
    - 异步处理 
        - 将次要流程通过多线程、MQ、甚至延时任务进行异步处理
        - 削峰填谷
            - 通过MQ承接流量
    - 并发处理
        - 通过多线程将串行逻辑并行化
    - 减少IO次数，
        - 比如数据库和缓存的批量读写、RPC的批量接口支持、或者通过冗余数据的方式干掉RPC调用
    - 网络消耗
    - 池化技术
        - HTTP请求池、线程池（考虑CPU密集型还是IO密集型设置核心参数）、数据库和Redis连接池等
    - 锁选择
    - JVM优化，包括新生代和老年代的大小、GC算法的选择等，尽可能减少GC频率和耗时
  
- 数据库
    - 问题
        - 大量请求阻塞 SQL 操作变慢 存储出现问题
    - SQL优化
    - 索引优化
    - 主从 读写分离
    - 分不分表
        - 分库
            - 单点数据库压力
        - 分表
            - 解决单表数据量过大
            - 分表有几个维度，
                - 一是水平切分和垂直切分，
                - 二是单库内分表和多库内分表
                    - 在一个数据库中将一张表拆分为几个子表在一定程度上可以解决单表查询性能的问题，但是也会遇到一个问题：单数据库存储瓶颈
        - 关联查询、 排序 分页
        
        - 分布式事务
        - 分布式id
        - 多数据源
    - 列式存储
    - 时序数据库
        - InfluxDB
    - LSM
    - 对于90%以上场景都是写入的时序数据库，B tree很明显是不合适的。业界主流都是采用LSM tree替换B tree，比如Hbase, Cassandra等nosql中
    - 分布式数据库
        - TIDB
   
    
- 缓存
    - 多级缓存，包括静态数据使用CDN、本地缓存、分布式缓存等，
    - 缓存穿透
        - 查询不存在数据的现象我们称为缓存穿透
    - 缓存击穿
    - 缓存雪崩
    - 缓存预热
    
    - 数据预处理
    - 以及对缓存场景中的热点key、缓存穿透、缓存并发、数据一致性等问题的处理
    
- 高可用服务
    - 负载均衡 请求负载均衡到各个机器
    - 转移
        - 对等节点的故障转移
        - 非对等节点的故障转移，通过心跳检测并实施主备切换（比如redis的哨兵模式或者集群模式、MySQL的主从切换
    - 水平扩展
        - 无状态水平扩容，有状态做分片路由。业务集群通常能设计成无状态的，而数据库和缓存往往是有状态的，因此需要设计分区键做好存储分片，当然也可以通过主从同步、读写分离的方案提升读性能
    - 超时机制超时设置、
    - 重试策略和
    - 幂等设计
    - 隔离
        - 舱壁模式
        - 维护了一个小型的线程池（或者信号量
    - 降级处理
        - 断路器、熔断
    - 限流处理
        - Nginx接入层的限流
        - 服务端的限流
            - 在两个时间窗口临界的 20ms 内会集中有 200 次接口请求，如果不做限流，集中在这 20ms 内的 200 次请求就有可能压垮系统
            - 滑动时间窗口限流算法的时间窗口是持续滑动的，并且除了需要一个计数器来记录时间窗口内接口请求次数之外，还需要记录在时间窗口内每个接口请求到达的时间点，对内存的占用会比较多
            - 令牌桶
            - 漏桶
    - MQ场景的消息可靠性保证
    - 分层 
    - 监控报警：全方位的监控体系，包括最基础的CPU、内存、磁盘、网络的监控，以及Web服务器、JVM、数据库、各类中间件的监控和业务指标的监控
    - 灰度发布，能支持按机器维度进行小流量部署，观察系统日志和业务指标，等运行平稳后再推全量

- 高扩展   
    - 分层架构
    - 存储层的拆分：按照业务维度做垂直拆分、按照数据特征维度进一步做水平拆分（分库分表）。
    - 业务层的拆分
    
    
- 高并发确实是一个复杂且系统性的问题，由于篇幅有限，诸如分布式Trace、全链路压测、柔性事务 分布式锁 都是要考虑的技术点    

### 云原生




- 函数式编程


- DDD
    - 
- 登陆
    - 跨域
    - 单点登录
    - session
    - cookie
    
    - jwt
    - HTTPS vs http
    
    
    
    
- 用户登陆
    - Parts of a URL: host, port, path
        ```
        Unfortunately the other answers in this question can be slightly misleading. Referring landfill.bugzilla.org to as host is correct in this specific example, but if the port was other than 443 then it would be incorrect.
        
        https:// by default uses port 443, so you may omit it in the URL, otherwise it would of looked like this https://landfill.bugzilla.org:443/bugzilla-tip:
        
        Protocol: https://
        Hostname: landfill.bugzilla.org
        Port: 443
        Host: landfill.bugzilla.org:443
        Path: bugzilla-tip
        host and hostname are not the same! It's only the "same" when the default ports on the protocol are being used!
        
        More info: https://tools.ietf.org/html/rfc1738
        ```
    - 转发与重定向
        - Spring 
            - @Controller + thymeleaf
            - @see HttpServletResponse#sendRedirect(String)
    - Cipher
    - 字节转String
    - httpclient
    - Jwt
        - spring-security-jwt
    - javax.servlet.http.Cookie 
        - 各个属性
    - OAuth2
    
    


- 跨域
    - 浏览器安全的基石同源政策
        - 最初，它的含义是指，A网页设置的 Cookie，B网页不能打开，除非这两个网页"同源"
            - 保证用户信息的安全，防止恶意的网站窃取数据，否则 Cookie 可以共享，互联网就毫无安全可言
        - "同源"指的是"三个相同"
            - 协议相同
            - 域名相同
            - 端口相同
        - 如果非同源，三种行为受到限制
            - Cookie、LocalStorage 和 IndexDB 无法读取。
                - Cookie
                    - Cookie 是服务器写入浏览器的一小段信息，只有同源的网页才能共享。但是，两个网页一级域名相同，只是二级域名不同，浏览器允许通过设置document.domain共享 Cookie
                    - 另外，服务器也可以在设置Cookie的时候，指定Cookie的所属域名为一级域名，比如.example.com；这样的话，二级域名和三级域名不用做任何设置，都可以读取这个Cookie。
                        ```
                        Set-Cookie: key=value; domain=.example.com; path=/
                        ```
            - DOM 无法获得。
                - 如果两个网页不同源，就无法拿到对方的DOM。典型的例子是iframe窗口和window.open方法打开的窗口，它们与父窗口无法通信
            - AJAX 请求不能发送。
                - 同源政策规定，AJAX请求只能发给同源的网址，否则就报错
                - 比如在前端部署在A机器，通过A.com访问前端；如果在网页中通过ajax访问B.com/user接口，那么就是跨域不能发送
                - 解决
                    - 架设服务器代理（浏览器请求同源服务器，再由后者请求外部服务）
                        - 前端部署时代理转发会跨域的请求到其他服务器(比如nginx)
                        - nginx
                    - JSONP
                    - WebSocket
                        - WebSocket是一种通信协议，使用ws://（非加密）和wss://（加密）作为协议前缀。该协议不实行同源政策，只要服务器支持，就可以通过它进行跨源通信
                        - 浏览器发出的WebSocket请求的头信息,有一个字段是Origin，表示该请求的请求源（origin），即发自哪个域名
                        - 正是因为有了Origin这个字段，所以WebSocket才没有实行同源政策。因为服务器可以根据这个字段，判断是否许可本次通信。如果该域名在白名单内，服务器就会做出如下回应
                    - CORS
                        - 跨源资源共享(Cross-origin resource sharing)
                        - W3C标准，是跨源AJAX请求的根本解决方法。相比JSONP只能发GET请求，CORS允许任何类型的请求
                        - 它允许浏览器向跨源服务器，发出XMLHttpRequest请求，从而克服了AJAX只能同源使用的限制。
                        - CORS需要浏览器和服务器同时支持
                            - 浏览器
                                - 整个CORS通信过程，都是浏览器自动完成，不需要用户参与。对于开发者来说，CORS通信与同源的AJAX通信没有差别，代码完全一样。浏览器一旦发现AJAX请求跨源，就会自动添加一些附加的头信息，有时还会多出一次附加的请求，但用户不会有感觉
                                - 浏览器将CORS请求分成两类：简单请求（simple request）和非简单请求（not-so-simple request）。
                                    - 简单请求（simple request）
                                        - 只要同时满足以下两大条件，就属于简单请求
                                        ```
                                        （1) 请求方法是以下三种方法之一：
                                        
                                        HEAD
                                        GET
                                        POST
                                        （2）HTTP的头信息不超出以下几种字段：
                                        
                                        Accept
                                        Accept-Language
                                        Content-Language
                                        Last-Event-ID
                                        Content-Type：只限于三个值application/x-www-form-urlencoded、multipart/form-data、text/plain
                                        ```
                                        - 这是为了兼容表单（form），因为历史上表单一直可以发出跨域请求。AJAX 的跨域设计就是，只要表单可以发，AJAX 就可以直接发。
                                        - 凡是不同时满足上面两个条件，就属于非简单请求。
                                        - 对于简单请求，浏览器直接发出CORS请求。具体来说，就是在头信息之中，增加一个Origin字段；浏览器发现这次跨源AJAX请求是简单请求，就自动在头信息之中，添加一个Origin字段
                                            - Origin字段用来说明，本次请求来自哪个源（协议 + 域名 + 端口）。服务器根据这个值，决定是否同意这次请求
                                            - 如果Origin指定的源，不在许可范围内，服务器会返回一个正常的HTTP回应。浏览器发现，这个回应的头信息没有包含Access-Control-Allow-Origin字段（详见下文），就知道出错了，从而抛出一个错误，被XMLHttpRequest的onerror回调函数捕获。注意，这种错误无法通过状态码识别，因为HTTP回应的状态码有可能是200
                                            - 如果Origin指定的域名在许可范围内，服务器返回的响应，会多出几个头信息字段。
                                                ```
                                                Access-Control-Allow-Origin: 
                                                Access-Control-Allow-Credentials: true
                                                Access-Control-Expose-Headers: 
                                                ```
                            -服务器
                                - 因此，实现CORS通信的关键是服务器。只要服务器实现了CORS接口，就可以跨源通信
                                - spring
                                    - 配置
                                    ```
                                    spring:
                                      cors:
                                        allow-origin:
                                        allow-methods:
                                          - POST
                                          - GET
                                          - DELETE
                                          - OPTIONS
                                        allow-headers:
                                        
                                        allow-credentials: true
                                    ```
                                    - 注解
                                        - @CrossOrigin
                                    - WebMvcConfigurerAdapter
                          