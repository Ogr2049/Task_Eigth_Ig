package com.example.dto;

import java.time.LocalDateTime;

public class UserEventDto {
    public enum EventType {
        USER_CREATED,
        USER_DELETED
    }
    
    private EventType myEventType;
    private Long myUserId;
    private String myUserName;
    private String myUserEmail;
    private Integer myUserAge;
    private LocalDateTime myEventTime;
    
    public UserEventDto() {}
    
    public UserEventDto(EventType myEventType, Long myUserId, String myUserName, 
                       String myUserEmail, Integer myUserAge, LocalDateTime myEventTime) {
        this.myEventType = myEventType;
        this.myUserId = myUserId;
        this.myUserName = myUserName;
        this.myUserEmail = myUserEmail;
        this.myUserAge = myUserAge;
        this.myEventTime = myEventTime;
    }
    
    public EventType getEventType() { return myEventType; }
    public void setEventType(EventType myEventType) { this.myEventType = myEventType; }
    
    public Long getUserId() { return myUserId; }
    public void setUserId(Long myUserId) { this.myUserId = myUserId; }
    
    public String getUserName() { return myUserName; }
    public void setUserName(String myUserName) { this.myUserName = myUserName; }
    
    public String getUserEmail() { return myUserEmail; }
    public void setUserEmail(String myUserEmail) { this.myUserEmail = myUserEmail; }
    
    public Integer getUserAge() { return myUserAge; }
    public void setUserAge(Integer myUserAge) { this.myUserAge = myUserAge; }
    
    public LocalDateTime getEventTime() { return myEventTime; }
    public void setEventTime(LocalDateTime myEventTime) { this.myEventTime = myEventTime; }
}