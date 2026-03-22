package com.test.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CodingSubmissionResponse {

    private int totalTestCases;

    private int passedTestCases;

    private int score;

    private List<TestCaseResult> testCaseResults;

}
