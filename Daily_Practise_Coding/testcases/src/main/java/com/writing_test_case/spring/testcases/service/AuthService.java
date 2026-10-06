package com.writing_test_case.spring.testcases.service;

import com.writing_test_case.spring.testcases.dto.RegisterRequest;
import com.writing_test_case.spring.testcases.entity.User;
import com.writing_test_case.spring.testcases.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }

    public User registerUser(RegisterRequest request){

        if(emailExists(request.getEmail())){
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }
}
