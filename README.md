# bq-service-auth服务使用说明
- bq微服务解决方案参见
	```xml
    <dependency>
        <groupId>com.biuqu</groupId>
        <artifactId>bq-parent</artifactId>
        <version>1.0.3</version>
    </dependency>
	```
 中的[`README`文档](https://github.com/woollay/bq-parent/blob/main/README.MD) 
- 本服务基于SpringSecurity-OAuth2-Authorization-Server二次封装，主要是为了简化JwtToken的生成和鉴权，当然也支持灵活的配置扩展：
- 本服务具备完整的独立服务能力，核心依赖：
    ```xml
    <dependency>
        <groupId>com.biuqu</groupId>
        <artifactId>bq-boot-base</artifactId>
        <version>1.0.3</version>
    </dependency>
    ```

## 1. 为什么要写bq-service-auth服务

- `SpringSecurity-OAuth2-Authorization-Server`本身虽然支持了多种场景的OAuth2认证，但是很难适用于已经存在账密体系的系统迁移改造，好的auth服务必须能够支持账密体系的扩展；
- `bq-service-auth`完全是通过`SpringSecurity-OAuth2-Authorization-Server`框架的扩展点来实现的（没有用到任何反射），但是也基本上把框架改了个底朝天，好在不辱使命，终于进一步加深了我对对框架的认知；
- 因为`SpringSecurity-OAuth2-Authorization-Server`和jdk版本的关系，由于想兼容更多的JDK版本，所以JDK版本为1.8，对应的框架最多只能选择`0.2.3`，从而导致`bq-service-auth`使用的SpringBoot版本和SpringCloud版本全部降了版本(SpringBoot:`2.7.4`->`2.5.12`，SpringCloud:`2021.0.5`->`3.0.6`，AlibabaSpringCloud:`2021.0.5.0`->`2021.0.1.0`,…)，如果你也遇到了版本兼容的问题，你就知道这个过程是多么的痛苦；
- 整合了sleuth/zipkin，并同时整合了logback，服务做到了Access Log和运行日志均具有链路追踪ID；
- 整合了`CircuitBreaker`/Sentinel服务降级、`Nacos`/`Eureka`服务注册中心，使之具有完备的服务能力；

## 2. 使用bq-service-auth服务有什么好处
- 按照框架扩展点的方式去实现了auth认证服务，以后要是升级JDK 11+后，可以非常方便的升级各种兼容的Spring版本；
- 对报文做了缓存和耗时记录，并贯穿了链路追踪ID，基本上可以达到开箱即用；
- 整合了docker脚本和docker-compose两种方式，可以非常方便地构建docker集群(目前只验证了MacOS)；

## 3. bq-service-auth最佳实践
- `bq-service-auth`最佳实践是配合[bq-service-gateway](https://github.com/woollay/bq-service-gateway) 中一起使用，以达到JwtToken会话生成和鉴权的分离；


