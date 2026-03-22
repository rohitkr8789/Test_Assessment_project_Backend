package com.test.repository;

import com.test.entity.CodingQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, Long> {

    @Query(value = "SELECT * FROM coding_questions ORDER BY RANDOM() LIMIT 2", nativeQuery = true)
    List<CodingQuestion> getRandom2CodingQuestions();

}