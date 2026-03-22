package com.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TodayResultResponse {

    private Long dailyTestId;
    private int totalMcq;
    private int correctAnswers;
    private int score;
    private int rank;
    private int stars;
    private LocalDateTime attemptedAt;
}
