package com.biuqu.boot.startup.auth;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 容器启动类(默认的启动类，带servlet web容器)
 *
 * @author BiuQu
 * @date 2023/1/27 12:06
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties
@EnableEncryptableProperties
@ComponentScan(basePackages = {"com.biuqu"})
public class AuthStartup extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        SpringApplication.run(AuthStartup.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
    {
        return super.configure(builder);
    }
}
