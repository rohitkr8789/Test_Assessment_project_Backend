package com.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodingQuestionResponse {

    private Long questionId;
    private String title;
    private String description;
    private String methodSignature;
    private int marks;
}