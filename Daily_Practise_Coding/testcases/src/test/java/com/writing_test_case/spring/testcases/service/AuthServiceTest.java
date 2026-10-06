package com.writing_test_case.spring.testcases.service;

import com.writing_test_case.spring.testcases.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

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
}
