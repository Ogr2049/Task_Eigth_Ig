package com.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MyCircuitBreakerServiceTest {
    
    @InjectMocks
    private MyCircuitBreakerService myCircuitBreakerService;
    
    @Test
    void myTestCircuitBreakerSuccess() {
        String myResult = myCircuitBreakerService.myPerformOperation("test");
        assertThat(myResult).isEqualTo("Success: test");
    }
    
    @Test
    void myTestCircuitBreakerFallback() {
        String myResult = myCircuitBreakerService.myPerformOperation("error");
        assertThat(myResult).contains("Fallback response for: error");
    }
}