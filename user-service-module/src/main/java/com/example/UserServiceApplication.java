package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(UserServiceApplication.class);
    
    public static void main(String[] myArgs) {
        myLoggerInstance.info("Starting User Service Application with Spring Cloud support");
        myLoggerInstance.info("Service will register with Eureka Discovery Server");
        myLoggerInstance.info("Configuration will be loaded from Config Server");
        
        SpringApplication.run(UserServiceApplication.class, myArgs);
        
        myLoggerInstance.info("User Service Application started successfully");
        myLoggerInstance.info("Available endpoints:");
        myLoggerInstance.info("- Swagger UI: http://localhost:8081/swagger-ui.html");
        myLoggerInstance.info("- API Docs: http://localhost:8081/api-docs");
        myLoggerInstance.info("- Eureka Dashboard: http://localhost:8761");
        myLoggerInstance.info("- Circuit Breaker health: http://localhost:8081/actuator/health");
    }
}