


- 微服务之间调用可以
    - Dubbo
    - Spring Cloud Feign
- 应对外部请求
    - 微服务网关
        - zuul
        - Spring Cloud Gateway
- 微服务网关
    - - 微服务网关是介于客户端和服务器端之间的中间层，所有的外部请求都会先经过微服务网关
    - 不同的微服务一般会有不同的网络地址，而外部客户端（例如手机APP）可能需要调用多个服务的接口才能完成一个业务需求，如果让客户端直接与各个微服务通信
        - 客户端会多次请求不同的微服务，增加了客户端的复杂性，难以重构，随着项目的迭代，可能需要重新划分微服务
            - 减少了客户端与各个微服务之间的交互次数
        - 认证复杂，每个服务都需要独立认证
            - 易于认证。可在微服务网关上进行认证，然后再将请求转发到后端的微服务，而无须在每个微服务中进行认证
        - 监控
            - 易于监控。可在微服务网关收集监控数据并将其推送到外部系统进行分析
        - 等等
    
    
- Zuul
    - 限流整合了Hystrix
    - 负载均衡整合了Ribbon
    
    - 默认的路由规则
        - 访问$ZUUL_URL/指定微服务名/** 会被转发到 指定微服务 的/**
        - spring.application.name: 微服务名
        - 比如一个Zuul微服务注册到Eureka Server上，且能通过http://127.0.0.1:8040访问；那么访问http://127.0.0.1:8040/microservice-provider-user/users/1 ，请求会被转发到了microservice-provider-user 服务的/users/1 端点
    - 自定义指定微服务的访问路径
        - 配置zuul.routes.指定微服务的serviceId = 指定路径
            - microservice-provider-user微服务就会被映射到/user/**路径
            ```
            zuul:
              routes:
                microservice-provider-user: /user/**
            
            ```
            ```
            zuul:
              routes:
                user-route:                   # 该配置方式中，user-route只是给路由一个名称，可以任意起名。
                  service-id: microservice-provider-user
                  path: /user/**              # service-id对应的路径
            ```
        - 同时指定path和URL
            - 这样就可以将/user/** 映射到http://localhost:8000/**
            - 需要注意的是，使用这种方式配置的路由不会作为HystrixCommand执行，同时也不能使用Ribbon来负载均衡多个URL，需要额外的配置
            ```
            zuul:
              routes:
                user-route:                   # 该配置方式中，user-route只是给路由一个名称，可以任意起名。
                  url: http://localhost:8000/ # 指定的url
                  path: /user/**              # url对应的路径。
            ```
        - 路由前缀
            ```
            zuul:
              routes:
                microservice-provider-user: 
                  path: /user/**
                  strip-prefix: false          
            ```
            - 这样访问Zuul的/user/1路径，请求将会被转发到microservice-provider-user的/user/1
        