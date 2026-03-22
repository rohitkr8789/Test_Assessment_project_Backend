package com.test.repository;

import com.test.entity.CodingQuestion;
import com.test.entity.DailyTestCoding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DailyTestCodingRepository 
extends JpaRepository<DailyTestCoding, Long> {

List<DailyTestCoding> findByDailyTestId(Long dailyTestId);

@Query("""
   SELECT dtc.codingQuestion
   FROM DailyTestCoding dtc
   WHERE dtc.dailyTest.id = :dailyTestId
   """)
List<CodingQuestion> findCodingQuestionsByDailyTestId(Long dailyTestId);
}