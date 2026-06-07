package com.example.notification.controller;

import com.example.notification.dto.EmailRequestDto;
import com.example.notification.service.RestNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Controller", description = "API для отправки уведомлений по email")
public class NotificationController {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(NotificationController.class);
    
    private final RestNotificationService myRestNotificationService;
    
    public NotificationController(RestNotificationService myRestNotificationService) {
        this.myRestNotificationService = myRestNotificationService;
    }
    
    @Operation(summary = "Отправить приветственное письмо")
    @PostMapping("/welcome")
    public ResponseEntity<String> mySendWelcomeEmail(@RequestBody EmailRequestDto myRequest) {
        myLoggerInstance.info("REST request to send welcome email to: {}", myRequest.getEmail());
        
        try {
            myRestNotificationService.mySendWelcomeEmail(myRequest.getEmail(), myRequest.getUserName());
            return ResponseEntity.ok("Welcome email sent successfully to: " + myRequest.getEmail());
        } catch (Exception myException) {
            myLoggerInstance.error("Failed to send welcome email: {}", myException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to send email: " + myException.getMessage());
        }
    }
    
    @Operation(summary = "Отправить письмо об удалении аккаунта")
    @PostMapping("/deletion")
    public ResponseEntity<String> mySendDeletionEmail(@RequestBody EmailRequestDto myRequest) {
        myLoggerInstance.info("REST request to send deletion email to: {}", myRequest.getEmail());
        
        try {
            myRestNotificationService.mySendDeletionEmail(myRequest.getEmail(), myRequest.getUserName());
            return ResponseEntity.ok("Deletion email sent successfully to: " + myRequest.getEmail());
        } catch (Exception myException) {
            myLoggerInstance.error("Failed to send deletion email: {}", myException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to send email: " + myException.getMessage());
        }
    }
    
    @Operation(summary = "Отправить кастомное письмо")
    @PostMapping("/custom")
    public ResponseEntity<String> mySendCustomEmail(@RequestBody EmailRequestDto myRequest) {
        myLoggerInstance.info("REST request to send custom email to: {}", myRequest.getEmail());
        
        if (myRequest.getSubject() == null || myRequest.getMessage() == null) {
            return ResponseEntity.badRequest()
                .body("Subject and message are required for custom email");
        }
        
        try {
            myRestNotificationService.mySendCustomEmail(
                myRequest.getEmail(), 
                myRequest.getSubject(), 
                myRequest.getMessage()
            );
            return ResponseEntity.ok("Custom email sent successfully to: " + myRequest.getEmail());
        } catch (Exception myException) {
            myLoggerInstance.error("Failed to send custom email: {}", myException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to send email: " + myException.getMessage());
        }
    }
    
    @Operation(summary = "Проверить работу сервиса")
    @GetMapping("/health")
    public ResponseEntity<String> myHealthCheck() {
        return ResponseEntity.ok("Notification service is running");
    }
}