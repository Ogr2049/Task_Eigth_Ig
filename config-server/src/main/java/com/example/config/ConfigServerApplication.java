package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(ConfigServerApplication.class);
    
    public static void main(String[] myArgs) {
        myLoggerInstance.info("Starting Config Server...");
        SpringApplication.run(ConfigServerApplication.class, myArgs);
    }
}