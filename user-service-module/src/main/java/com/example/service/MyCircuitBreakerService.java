package com.example.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MyCircuitBreakerService {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(MyCircuitBreakerService.class);
    
    @CircuitBreaker(name = "userService", fallbackMethod = "myFallbackMethod")
    public String myPerformOperation(String myInput) {
        myLoggerInstance.info("Executing operation with input: {}", myInput);
        

        if ("error".equals(myInput)) {
            throw new RuntimeException("Simulated error for Circuit Breaker testing");
        }
        
        return "Success: " + myInput;
    }
    
    public String myFallbackMethod(String myInput, Throwable myThrowable) {
        myLoggerInstance.warn("Fallback method triggered for input: {}, error: {}", 
                            myInput, myThrowable.getMessage());
        return "Fallback response for: " + myInput;
    }
}