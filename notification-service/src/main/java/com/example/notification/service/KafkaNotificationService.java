package com.example.notification.service;

import com.example.notification.dto.UserEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaNotificationService {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(KafkaNotificationService.class);
    
    private final EmailService myEmailService;
    
    public KafkaNotificationService(EmailService myEmailService) {
        this.myEmailService = myEmailService;
    }
    
    @KafkaListener(topics = "${kafka.topic.user-events}", groupId = "notification-group")
    public void myConsumeUserEvent(UserEventDto myEvent) {
        myLoggerInstance.info("Received Kafka event: {}", myEvent);
        
        switch (myEvent.getEventType()) {
            case USER_CREATED:
                myEmailService.mySendUserCreatedEmail(
                    myEvent.getUserEmail(), 
                    myEvent.getUserName()
                );
                break;
                
            case USER_DELETED:
                myEmailService.mySendUserDeletedEmail(
                    myEvent.getUserEmail(), 
                    myEvent.getUserName()
                );
                break;
                
            default:
                myLoggerInstance.warn("Unknown event type: {}", myEvent.getEventType());
        }
        
        myLoggerInstance.info("Processed event for user: {}", myEvent.getUserEmail());
    }
}