package com.example.notification.exception;

public class NotificationException extends RuntimeException {
    public NotificationException(String myMessage) {
        super(myMessage);
    }
    
    public NotificationException(String myMessage, Throwable myCause) {
        super(myMessage, myCause);
    }
}