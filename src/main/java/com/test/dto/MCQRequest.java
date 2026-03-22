package com.test.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class MCQRequest {

    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer; // A, B, C, D
}
