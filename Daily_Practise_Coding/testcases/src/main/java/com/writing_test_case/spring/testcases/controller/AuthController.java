package com.writing_test_case.spring.testcases.controller;

import com.writing_test_case.spring.testcases.dto.RegisterRequest;
import com.writing_test_case.spring.testcases.entity.User;
import com.writing_test_case.spring.testcases.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody RegisterRequest request
            ){
        User user = authService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
