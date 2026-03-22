package com.test.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.test.entity.MCQ;
@Repository
public interface MCQRepository extends JpaRepository<MCQ, Long>{

	@Query(value = "SELECT * FROM mcq_questions ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
	List<MCQ> getRandom10();

	Page<MCQ> findAll(Pageable pageable);
	
	Page<MCQ> findByQuestionContainingIgnoreCase(String question, Pageable pageable);
}
