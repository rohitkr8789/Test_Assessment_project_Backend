package com.test.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MCQResponse {

    private Long id;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
}
