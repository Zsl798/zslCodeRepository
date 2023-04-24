package com.lowcode.eureka.mastereureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringCloudMasterEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudMasterEurekaApplication.class, args);
    }

}
