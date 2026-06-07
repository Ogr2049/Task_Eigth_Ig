package com.example.service;

import com.example.dto.UserEventDto;
import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;
import com.example.entity.UserEntity;
import com.example.exception.NotFoundException;
import com.example.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private final UserRepository myUserRepository;
    private final KafkaTemplate<String, Object> myKafkaTemplate;
    
    @Autowired
    public UserServiceImpl(UserRepository myUserRepository, 
                          KafkaTemplate<String, Object> myKafkaTemplate) {
        this.myUserRepository = myUserRepository;
        this.myKafkaTemplate = myKafkaTemplate;
    }
    
    @Override
    @Transactional
    public UserResponseDto myCreateUser(UserRequestDto myUserRequestDto) {
        myLoggerInstance.info("Creating new user with email: {}", myUserRequestDto.getUserEmail());
        
        if (myUserRepository.existsByUserEmail(myUserRequestDto.getUserEmail())) {
            throw new IllegalArgumentException("Email уже зарегистрирован: " + myUserRequestDto.getUserEmail());
        }
        
        UserEntity myUserEntity = new UserEntity(
            myUserRequestDto.getUserName(),
            myUserRequestDto.getUserEmail(),
            myUserRequestDto.getUserAge()
        );
        
        UserEntity mySavedUser = myUserRepository.save(myUserEntity);
        myLoggerInstance.info("User created successfully with ID: {}", mySavedUser.getUserId());
        
        // Отправка события USER_CREATED в Kafka
        mySendUserEvent(UserEventDto.EventType.USER_CREATED, mySavedUser);
        
        return myConvertToDto(mySavedUser);
    }
    
    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "myGetUserByIdFallback")
    @Retry(name = "userService")
    @Transactional(readOnly = true)
    public UserResponseDto myGetUserById(Long myUserId) {
        myLoggerInstance.info("Fetching user with ID: {}", myUserId);
        
        UserEntity myUserEntity = myUserRepository.findById(myUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + myUserId));
        
        return myConvertToDto(myUserEntity);
    }
    
    public UserResponseDto myGetUserByIdFallback(Long myUserId, Throwable myThrowable) {
        myLoggerInstance.error("Fallback triggered for getUserById with ID: {}, Error: {}", 
                              myUserId, myThrowable.getMessage());
        
        UserResponseDto myFallbackResponse = new UserResponseDto();
        myFallbackResponse.setUserId(myUserId);
        myFallbackResponse.setUserName("Service Temporary Unavailable");
        myFallbackResponse.setUserEmail("fallback@example.com");
        myFallbackResponse.setUserAge(0);
        myFallbackResponse.setUserCreatedAt(LocalDateTime.now());
        
        return myFallbackResponse;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> myGetAllUsers() {
        myLoggerInstance.info("Fetching all users");
        
        List<UserEntity> myUserEntities = myUserRepository.findAll();
        
        return myUserEntities.stream()
                .map(this::myConvertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public UserResponseDto myUpdateUser(Long myUserId, UserRequestDto myUserRequestDto) {
        myLoggerInstance.info("Updating user with ID: {}", myUserId);
        
        UserEntity myExistingUser = myUserRepository.findById(myUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + myUserId));
        
        if (!myExistingUser.getUserEmail().equals(myUserRequestDto.getUserEmail()) &&
            myUserRepository.existsByUserEmail(myUserRequestDto.getUserEmail())) {
            throw new IllegalArgumentException("Email уже используется: " + myUserRequestDto.getUserEmail());
        }
        
        myExistingUser.setUserName(myUserRequestDto.getUserName());
        myExistingUser.setUserEmail(myUserRequestDto.getUserEmail());
        myExistingUser.setUserAge(myUserRequestDto.getUserAge());
        
        UserEntity myUpdatedUser = myUserRepository.save(myExistingUser);
        myLoggerInstance.info("User updated successfully with ID: {}", myUserId);
        
        return myConvertToDto(myUpdatedUser);
    }
    
    @Override
    @Transactional
    public void myDeleteUser(Long myUserId) {
        myLoggerInstance.info("Deleting user with ID: {}", myUserId);
        
        UserEntity myUserEntity = myUserRepository.findById(myUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + myUserId));
        
        // Отправка события USER_DELETED в Kafka перед удалением
        mySendUserEvent(UserEventDto.EventType.USER_DELETED, myUserEntity);
        
        myUserRepository.deleteById(myUserId);
        myLoggerInstance.info("User deleted successfully with ID: {}", myUserId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponseDto myGetUserByEmail(String myUserEmail) {
        myLoggerInstance.info("Fetching user by email: {}", myUserEmail);
        
        UserEntity myUserEntity = myUserRepository.findByUserEmail(myUserEmail)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с email: " + myUserEmail));
        
        return myConvertToDto(myUserEntity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean myCheckEmailExists(String myUserEmail) {
        return myUserRepository.existsByUserEmail(myUserEmail);
    }
    
    private UserResponseDto myConvertToDto(UserEntity myUserEntity) {
        UserResponseDto myUserResponseDto = new UserResponseDto();
        myUserResponseDto.setUserId(myUserEntity.getUserId());
        myUserResponseDto.setUserName(myUserEntity.getUserName());
        myUserResponseDto.setUserEmail(myUserEntity.getUserEmail());
        myUserResponseDto.setUserAge(myUserEntity.getUserAge());
        myUserResponseDto.setUserCreatedAt(myUserEntity.getUserCreatedAt());
        return myUserResponseDto;
    }
    
    private void mySendUserEvent(UserEventDto.EventType myEventType, UserEntity myUserEntity) {
        try {
            UserEventDto myEvent = new UserEventDto(
                myEventType,
                myUserEntity.getUserId(),
                myUserEntity.getUserName(),
                myUserEntity.getUserEmail(),
                myUserEntity.getUserAge(),
                LocalDateTime.now()
            );
            
            myKafkaTemplate.send("user-events-topic", myEvent);
            myLoggerInstance.info("User event sent to Kafka: {} for user ID: {}", 
                                myEventType, myUserEntity.getUserId());
        } catch (Exception myException) {
            myLoggerInstance.error("Failed to send user event to Kafka: {}", 
                                 myException.getMessage(), myException);
            // Не бросаем исключение, чтобы не ломать основной поток
        }
    }
}