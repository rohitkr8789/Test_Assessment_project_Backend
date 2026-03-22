package com.test.dto;

import lombok.Data;
import java.util.Map;

@Data
public class MCQSubmissionRequest {

    // key = questionId
    // value = selectedAnswer (A, B, C, D)
    private Map<Long, String> answers;
}
