package com.example.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(ApiGatewayApplication.class);
    
    public static void main(String[] myArgs) {
        myLoggerInstance.info("Starting API Gateway...");
        SpringApplication.run(ApiGatewayApplication.class, myArgs);
    }
}