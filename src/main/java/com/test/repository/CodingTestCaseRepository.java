package com.test.repository;

import com.test.entity.CodingTestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodingTestCaseRepository extends JpaRepository<CodingTestCase, Long> {

    List<CodingTestCase> findByCodingQuestionId(Long codingQuestionId);
    
   
}
