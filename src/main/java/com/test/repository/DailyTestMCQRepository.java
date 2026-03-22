package com.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.test.entity.DailyTestMCQ;
import com.test.entity.MCQ;

public interface DailyTestMCQRepository extends JpaRepository<DailyTestMCQ, Long> {
	@Query(value = "SELECT * FROM mcq_questions ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
	List<MCQ> getRandom10();
	
	@Query("""
		       SELECT dtm.mcq 
		       FROM DailyTestMCQ dtm 
		       WHERE dtm.dailyTest.id = :dailyTestId
		       """)
		List<MCQ> findMCQsByDailyTestId(Long dailyTestId);
}
