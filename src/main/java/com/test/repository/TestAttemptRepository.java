package com.test.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.test.entity.TestAttempt;
import com.test.entity.User;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {

	Optional<TestAttempt> findByCandidate(User candidate);
	
	boolean existsByCandidate_IdAndDailyTest_Id(Long candidateId, Long dailyTestId);
	
	Optional<TestAttempt> 
	findByCandidateIdAndDailyTestId(Long candidateId, Long dailyTestId);
	
	@Query(value = """
	        SELECT u.name AS candidateName,
	               t.total_score AS score,
	               RANK() OVER (ORDER BY t.total_score DESC) AS rank
	        FROM test_attempts t
	        JOIN users u ON t.candidate_id = u.id
	        ORDER BY t.total_score DESC
	        """, nativeQuery = true)
	List<Object[]> getLeaderboard();
	
	@Query(value = """
	        SELECT u.email AS email,
	               u.name AS candidateName,
	               t.total_score AS score,
	               RANK() OVER (ORDER BY t.total_score DESC) AS rank
	        FROM test_attempts t
	        JOIN users u ON t.candidate_id = u.id
	        """, nativeQuery = true)
	List<Object[]> getLeaderboardWithEmail();
	
	@Query(value = """
	        SELECT u.name AS candidateName,
	               t.total_score AS score,
	               DENSE_RANK() OVER (ORDER BY t.total_score DESC) AS rank
	        FROM test_attempts t
	        JOIN users u ON t.candidate_id = u.id
	        ORDER BY t.total_score DESC
	        LIMIT 10
	        """, nativeQuery = true)
	List<Object[]> getTop10Leaderboard();
	
	
	@Query(value = """
	        SELECT u.name AS candidateName,
	               t.total_score AS score,
	               DENSE_RANK() OVER (ORDER BY t.total_score DESC) AS rank
	        FROM test_attempts t
	        JOIN users u ON t.candidate_id = u.id
	        WHERE t.daily_test_id = :dailyTestId
	        ORDER BY t.total_score DESC
	        """, nativeQuery = true)
	List<Object[]> getDailyLeaderboard(Long dailyTestId);
	
	@Query(value = """
	        SELECT rank FROM (
	            SELECT candidate_id,
	                   RANK() OVER (ORDER BY total_score DESC) as rank
	            FROM test_attempts
	            WHERE daily_test_id = :dailyTestId
	        ) ranked
	        WHERE candidate_id = :candidateId
	        """, nativeQuery = true)
	Integer findRankByCandidateIdAndDailyTestId(Long candidateId, Long dailyTestId);
	
}