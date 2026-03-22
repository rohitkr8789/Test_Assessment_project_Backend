package com.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MCQSubmissionResponse {

    private int totalQuestions;
    private int correctAnswers;
    private int score;
}
