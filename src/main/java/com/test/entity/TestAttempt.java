package com.test.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "test_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "attempt_time")
    private LocalDateTime attemptTime;

    @Column(name = "coding_score", nullable = false)
    @Builder.Default
    private int codingScore = 0;

    @Column(name = "mcq_score", nullable = false)
    @Builder.Default
    private int mcqScore = 0;

    @Column(name = "total_score", nullable = false)
    @Builder.Default
    private int totalScore = 0;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private User candidate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "daily_test_id", nullable = false)
    private DailyTest dailyTest;
}