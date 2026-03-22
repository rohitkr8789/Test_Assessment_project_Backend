package com.test.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunCodeResponse {

    private String output;
    private boolean success;
    private String errorType;

    private List<TestCaseResult> testCaseResults;

}
