package com.example.notification.dto;

public class EmailRequestDto {
    private String myEmail;
    private String myUserName;
    private String mySubject;
    private String myMessage;
    
    public EmailRequestDto() {}
    
    public String getEmail() { return myEmail; }
    public void setEmail(String myEmail) { this.myEmail = myEmail; }
    
    public String getUserName() { return myUserName; }
    public void setUserName(String myUserName) { this.myUserName = myUserName; }
    
    public String getSubject() { return mySubject; }
    public void setSubject(String mySubject) { this.mySubject = mySubject; }
    
    public String getMessage() { return myMessage; }
    public void setMessage(String myMessage) { this.myMessage = myMessage; }
}