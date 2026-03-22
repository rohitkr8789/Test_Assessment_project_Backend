package com.test.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateCodingQuestionRequest {

    private String title;
    private String description;
    private String methodSignature;
    private int marks;

    private List<TestCaseDTO> testCases;

    @Data
    public static class TestCaseDTO {
        private String inputData;
        private String expectedOutput;
        private boolean sample;
    }
}