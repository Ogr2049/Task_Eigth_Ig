package com.example.notification.controller;

import com.example.notification.dto.EmailRequestDto;
import com.example.notification.service.RestNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {
    
    @Autowired
    private MockMvc myMockMvc;
    
    @Autowired
    private ObjectMapper myObjectMapper;
    
    @MockBean
    private RestNotificationService myRestNotificationService;
    
    private EmailRequestDto myEmailRequest;
    
    @BeforeEach
    void mySetUp() {
        myEmailRequest = new EmailRequestDto();
        myEmailRequest.setEmail("test@example.com");
        myEmailRequest.setUserName("Иван Иванов");
    }
    
    @Test
    void myTestSendWelcomeEmailSuccess() throws Exception {
        doNothing().when(myRestNotificationService).mySendWelcomeEmail(anyString(), anyString());
        
        myMockMvc.perform(post("/api/v1/notifications/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(myEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome email sent successfully to: test@example.com"));
        
        verify(myRestNotificationService, times(1)).mySendWelcomeEmail("test@example.com", "Иван Иванов");
    }
    
    @Test
    void myTestSendDeletionEmailSuccess() throws Exception {
        doNothing().when(myRestNotificationService).mySendDeletionEmail(anyString(), anyString());
        
        myMockMvc.perform(post("/api/v1/notifications/deletion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(myEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deletion email sent successfully to: test@example.com"));
        
        verify(myRestNotificationService, times(1)).mySendDeletionEmail("test@example.com", "Иван Иванов");
    }
    
    @Test
    void myTestSendCustomEmailSuccess() throws Exception {
        myEmailRequest.setSubject("Test Subject");
        myEmailRequest.setMessage("Test Message");
        
        doNothing().when(myRestNotificationService).mySendCustomEmail(anyString(), anyString(), anyString());
        
        myMockMvc.perform(post("/api/v1/notifications/custom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(myEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Custom email sent successfully to: test@example.com"));
        
        verify(myRestNotificationService, times(1)).mySendCustomEmail("test@example.com", "Test Subject", "Test Message");
    }
    
    @Test
    void myTestSendCustomEmailMissingFields() throws Exception {
        // Missing subject and message
        myEmailRequest.setSubject(null);
        myEmailRequest.setMessage(null);
        
        myMockMvc.perform(post("/api/v1/notifications/custom")
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(myEmailRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Subject and message are required for custom email"));
        
        verify(myRestNotificationService, never()).mySendCustomEmail(anyString(), anyString(), anyString());
    }
    
    @Test
    void myTestHealthCheck() throws Exception {
        myMockMvc.perform(get("/api/v1/notifications/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification service is running"));
    }
}