package com.test.dto;

import lombok.Data;

@Data
public class TestCaseResult {

    private int testCaseNumber;

    private String expectedOutput;

    private String actualOutput;

    private boolean passed;

}