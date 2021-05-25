


- Spring Cloud
    - Hystrix
    - Ribbon
    
- 限流
    - Hystrix
    - sentinel
    - lua+redis
    - 桶令牌
        - private static RateLimiter queryRateLimiter = RateLimiter.create(8);
        
        
- 存储
    - tidb
    

### 微服务架构设计    
- core[https://gudaoxuri.gitbook.io/microservices-architecture/]
    - 架构演进
    - 接口调用方式有两大类：RPC与Rest
    - 编制与协同(编排)
    - 