package com.writing_test_case.spring.testcases.service;

import com.writing_test_case.spring.testcases.dto.RegisterRequest;
import com.writing_test_case.spring.testcases.entity.User;
import com.writing_test_case.spring.testcases.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    // emailExists() -> email exists
    @Test
    void emailExists_shouldReturnTrue_whenEmailExists(){
        String email = "abc@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = authService.emailExists(email);

        assertTrue(result);

        verify(userRepository).existsByEmail(email);
    }


    @Test
    void emailExists_shouldReturnFalse_whenEmailDoesNotExists(){
        String email = "abc@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        boolean result = authService.emailExists(email);

        assertFalse(result);

        verify(userRepository).existsByEmail(email);
    }

    @Test
    void registerUser_shouldSaveUser_whenEmailDoesNotExists(){

        //arrange

        RegisterRequest request = new RegisterRequest();

        request.setName("Dhruvit");
        request.setEmail("dhruvit@gmail.com");
        request.setPassword("password");

        //email doesn't exists
        when(userRepository.existsByEmail("dhruvit@gmail.com"))
                .thenReturn(false);

        User savedUser = new User();

        savedUser.setId(1L);
        savedUser.setName("Dhruvit");
        savedUser.setEmail("dhruvit@gmail.com");
        savedUser.setPassword("password");

        // mock save

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        //act

        User result = authService.registerUser(request);

        //assert

        assertNotNull(result);

        assertEquals(1L,result.getId());

        assertEquals("Dhruvit",result.getName());

        assertEquals("dhruvit@gmail.com",result.getEmail());

        assertEquals("password",result.getPassword());

        //verify repositroy interactions

        verify(userRepository).existsByEmail("dhruvit@gmail.com");

        verify(userRepository).save(any(User.class));

    }

    @Test
    void registerUser_shouldThrowException_whenEmailAlreadyExists(){
        // arrange
        RegisterRequest request = new RegisterRequest();

        request.setName("Dhruvit");
        request.setEmail("dhruvit@gmail.com");
        request.setPassword("password123");

        // email already exists

        when(userRepository.existsByEmail("dhruvit@gmail.com")).thenReturn(true);

        // act+assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.registerUser(request)
        );

        // verify exception message
        assertEquals(
                "Email already registerd",
                exception.getMessage()
        );

        // veify save() was never called
        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void registerUser_shouldEncodePasswordBeforeSaving(){

        RegisterRequest request = new RegisterRequest();
        request.setName("Dhruvit");
        request.setEmail("dhruvit@gmail.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("dhruvit@gmail.com")).thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("$2a$12$hashedPasswordExample");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArguments(0));

        User result = authService.registerUser(request);

        // Assert
        assertEquals(
                "$2a$12$hashedPasswordExample",
                result.getPassword()
        );

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));

    }


}
