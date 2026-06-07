package com.example.notification;

import com.example.notification.dto.UserEventDto;
import com.example.notification.service.EmailService;
import com.example.notification.service.KafkaNotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaNotificationServiceTest {
    
    @Mock
    private EmailService myEmailService;
    
    @InjectMocks
    private KafkaNotificationService myKafkaNotificationService;
    
    @Test
    void myTestConsumeUserCreatedEvent() {
        // Given
        UserEventDto myEvent = new UserEventDto(
            UserEventDto.EventType.USER_CREATED,
            1L,
            "Иван Иванов",
            "ivan@example.com",
            30,
            LocalDateTime.now()
        );
        
        // When
        myKafkaNotificationService.myConsumeUserEvent(myEvent);
        
        // Then
        verify(myEmailService, times(1)).mySendUserCreatedEmail("ivan@example.com", "Иван Иванов");
    }
    
    @Test
    void myTestConsumeUserDeletedEvent() {
        // Given
        UserEventDto myEvent = new UserEventDto(
            UserEventDto.EventType.USER_DELETED,
            1L,
            "Иван Иванов",
            "ivan@example.com",
            30,
            LocalDateTime.now()
        );
        
        // When
        myKafkaNotificationService.myConsumeUserEvent(myEvent);
        
        // Then
        verify(myEmailService, times(1)).mySendUserDeletedEmail("ivan@example.com", "Иван Иванов");
    }
    
    @Test
    void myTestConsumeUnknownEvent() {
        // Given
        UserEventDto myEvent = new UserEventDto();
        myEvent.setEventType(null); // Unknown type
        
        // When
        myKafkaNotificationService.myConsumeUserEvent(myEvent);
        
        // Then
        verify(myEmailService, never()).mySendUserCreatedEmail(anyString(), anyString());
        verify(myEmailService, never()).mySendUserDeletedEmail(anyString(), anyString());
    }
}