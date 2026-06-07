package com.example.notification;

import com.example.notification.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    
    @Mock
    private JavaMailSender myJavaMailSender;
    
    @InjectMocks
    private EmailService myEmailService;
    
    @Test
    void myTestSendUserCreatedEmail() {
        // Given
        String myTestEmail = "test@example.com";
        String myTestName = "Иван Иванов";
        
        // When
        myEmailService.mySendUserCreatedEmail(myTestEmail, myTestName);
        
        // Then
        verify(myJavaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    void myTestSendUserDeletedEmail() {
        // Given
        String myTestEmail = "test@example.com";
        String myTestName = "Иван Иванов";
        
        // When
        myEmailService.mySendUserDeletedEmail(myTestEmail, myTestName);
        
        // Then
        verify(myJavaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    void myTestSendCustomEmail() {
        // Given
        String myTestEmail = "test@example.com";
        String myTestSubject = "Test Subject";
        String myTestMessage = "Test Message";
        
        // When
        myEmailService.mySendCustomEmail(myTestEmail, myTestSubject, myTestMessage);
        
        // Then
        verify(myJavaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    void myTestValidateEmail() {
        // Given
        String myValidEmail = "test@example.com";
        String myInvalidEmail = "invalid-email";
        
        // When & Then
        assert(myEmailService.myValidateEmail(myValidEmail));
        assert(!myEmailService.myValidateEmail(myInvalidEmail));
    }
}