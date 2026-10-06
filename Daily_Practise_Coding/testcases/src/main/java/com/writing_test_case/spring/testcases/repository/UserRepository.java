package com.writing_test_case.spring.testcases.repository;

import com.writing_test_case.spring.testcases.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
}
