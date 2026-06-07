package com.example.gateway.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MyFallbackController {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(MyFallbackController.class);
    
    @GetMapping("/myFallback/userService")
    public ResponseEntity<Map<String, Object>> myUserServiceFallback() {
        myLoggerInstance.warn("Fallback triggered for User Service");
        
        Map<String, Object> myResponse = new HashMap<>();
        myResponse.put("timestamp", java.time.LocalDateTime.now());
        myResponse.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        myResponse.put("error", "Service Unavailable");
        myResponse.put("message", "User Service временно недоступен. Пожалуйста, попробуйте позже.");
        myResponse.put("path", "/api/v1/users/**");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(myResponse);
    }
    
    @GetMapping("/myFallback/notificationService")
    public ResponseEntity<Map<String, Object>> myNotificationServiceFallback() {
        myLoggerInstance.warn("Fallback triggered for Notification Service");
        
        Map<String, Object> myResponse = new HashMap<>();
        myResponse.put("timestamp", java.time.LocalDateTime.now());
        myResponse.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        myResponse.put("error", "Service Unavailable");
        myResponse.put("message", "Notification Service временно недоступен. Пожалуйста, попробуйте позже.");
        myResponse.put("path", "/api/v1/notifications/**");
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(myResponse);
    }
}