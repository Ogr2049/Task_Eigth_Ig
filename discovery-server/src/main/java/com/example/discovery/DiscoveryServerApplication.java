package com.example.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(DiscoveryServerApplication.class);
    
    public static void main(String[] myArgs) {
        myLoggerInstance.info("Starting Discovery Server (Eureka)...");
        SpringApplication.run(DiscoveryServerApplication.class, myArgs);
    }
}