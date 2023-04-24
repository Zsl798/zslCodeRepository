package com.lowcode.modeltool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = { "com.lowcode.modeltool","com.lowcode" })
@MapperScan("com.lowcode.modeltool.*.*.mapper")
public class ModeltoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModeltoolApplication.class, args);
    }


}
