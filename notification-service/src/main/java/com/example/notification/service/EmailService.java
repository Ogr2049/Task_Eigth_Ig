package com.example.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(EmailService.class);
    
    private final JavaMailSender myJavaMailSender;
    
    @Value("${spring.mail.username}")
    private String myFromEmail;
    
    public EmailService(JavaMailSender myJavaMailSender) {
        this.myJavaMailSender = myJavaMailSender;
    }
    
    public void mySendUserCreatedEmail(String myToEmail, String myUserName) {
        String mySubject = "Добро пожаловать на наш сайт!";
        String myText = String.format(
            "Здравствуйте, %s!\n\n" +
            "Ваш аккаунт на сайте был успешно создан.\n" +
            "Мы рады приветствовать вас в нашем сообществе.\n\n" +
            "С уважением,\n" +
            "Команда поддержки",
            myUserName
        );
        
        mySendEmail(myToEmail, mySubject, myText);
        myLoggerInstance.info("Welcome email sent to: {}", myToEmail);
    }
    
    public void mySendUserDeletedEmail(String myToEmail, String myUserName) {
        String mySubject = "Ваш аккаунт был удален";
        String myText = String.format(
            "Здравствуйте, %s!\n\n" +
            "Ваш аккаунт был удалён.\n" +
            "Если это была ошибка или у вас есть вопросы, пожалуйста, свяжитесь с поддержкой.\n\n" +
            "С уважением,\n" +
            "Команда поддержки",
            myUserName
        );
        
        mySendEmail(myToEmail, mySubject, myText);
        myLoggerInstance.info("Account deletion email sent to: {}", myToEmail);
    }
    
    public void mySendCustomEmail(String myToEmail, String mySubject, String myText) {
        mySendEmail(myToEmail, mySubject, myText);
        myLoggerInstance.info("Custom email sent to: {}, subject: {}", myToEmail, mySubject);
    }
    
    private void mySendEmail(String myToEmail, String mySubject, String myText) {
        try {
            SimpleMailMessage myMessage = new SimpleMailMessage();
            myMessage.setFrom(myFromEmail);
            myMessage.setTo(myToEmail);
            myMessage.setSubject(mySubject);
            myMessage.setText(myText);
            
            myJavaMailSender.send(myMessage);
            myLoggerInstance.debug("Email successfully sent to: {}", myToEmail);
        } catch (Exception myException) {
            myLoggerInstance.error("Failed to send email to: {}, error: {}", 
                                 myToEmail, myException.getMessage(), myException);
            throw new RuntimeException("Failed to send email", myException);
        }
    }
    
    public boolean myValidateEmail(String myEmail) {
        return myEmail != null && myEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}