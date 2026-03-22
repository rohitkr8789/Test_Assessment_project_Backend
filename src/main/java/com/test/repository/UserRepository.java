package com.test.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    Optional<User> findByEmailAndDateOfBirth(String email, LocalDate dateOfBirth);
    
    
    
}
