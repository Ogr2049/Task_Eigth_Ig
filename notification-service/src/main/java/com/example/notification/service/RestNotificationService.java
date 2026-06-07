package com.example.notification.service;

import org.springframework.stereotype.Service;

@Service
public class RestNotificationService {
    
    private final EmailService myEmailService;
    
    public RestNotificationService(EmailService myEmailService) {
        this.myEmailService = myEmailService;
    }
    
    public void mySendWelcomeEmail(String myEmail, String myUserName) {
        myEmailService.mySendUserCreatedEmail(myEmail, myUserName);
    }
    
    public void mySendDeletionEmail(String myEmail, String myUserName) {
        myEmailService.mySendUserDeletedEmail(myEmail, myUserName);
    }
    
    public void mySendCustomEmail(String myEmail, String mySubject, String myMessage) {
        myEmailService.mySendCustomEmail(myEmail, mySubject, myMessage);
    }
}