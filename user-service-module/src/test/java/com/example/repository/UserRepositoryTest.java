package com.example.repository;

import com.example.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    
    @Autowired
    private TestEntityManager myEntityManager;
    
    @Autowired
    private UserRepository myUserRepository;
    
    @Test
    void myTestFindByUserEmail() {
        // Given
        UserEntity myUserEntity = new UserEntity("Иван Иванов", "ivan@example.com", 30);
        myEntityManager.persist(myUserEntity);
        myEntityManager.flush();
        
        // When
        Optional<UserEntity> myFoundUser = myUserRepository.findByUserEmail("ivan@example.com");
        
        // Then
        assertThat(myFoundUser).isPresent();
        assertThat(myFoundUser.get().getUserName()).isEqualTo("Иван Иванов");
    }
    
    @Test
    void myTestExistsByUserEmail() {
        // Given
        UserEntity myUserEntity = new UserEntity("Иван Иванов", "ivan@example.com", 30);
        myEntityManager.persist(myUserEntity);
        myEntityManager.flush();
        
        // When
        boolean myExists = myUserRepository.existsByUserEmail("ivan@example.com");
        boolean myNotExists = myUserRepository.existsByUserEmail("notexists@example.com");
        
        // Then
        assertThat(myExists).isTrue();
        assertThat(myNotExists).isFalse();
    }
    
    @Test
    void myTestFindUserById() {
        // Given
        UserEntity myUserEntity = new UserEntity("Иван Иванов", "ivan@example.com", 30);
        UserEntity mySavedUser = myEntityManager.persist(myUserEntity);
        myEntityManager.flush();
        
        // When
        Optional<UserEntity> myFoundUser = myUserRepository.findUserById(mySavedUser.getUserId());
        
        // Then
        assertThat(myFoundUser).isPresent();
        assertThat(myFoundUser.get().getUserEmail()).isEqualTo("ivan@example.com");
    }
    
    @Test
    void myTestFindUserByEmailAddress() {
        // Given
        UserEntity myUserEntity = new UserEntity("Иван Иванов", "ivan@example.com", 30);
        myEntityManager.persist(myUserEntity);
        myEntityManager.flush();
        
        // When
        Optional<UserEntity> myFoundUser = myUserRepository.findUserByEmailAddress("ivan@example.com");
        
        // Then
        assertThat(myFoundUser).isPresent();
        assertThat(myFoundUser.get().getUserName()).isEqualTo("Иван Иванов");
    }
}