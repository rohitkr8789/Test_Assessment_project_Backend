package com.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.entity.CodingSubmission;

public interface CodingSubmissionRepository extends JpaRepository<CodingSubmission, Long> {

    Optional<CodingSubmission> findByCandidateIdAndDailyTestIdAndCodingQuestionId(
            Long candidateId,
            Long dailyTestId,
            Long codingQuestionId
    );
}