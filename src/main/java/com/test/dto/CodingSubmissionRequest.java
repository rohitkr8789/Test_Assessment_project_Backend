package com.test.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodingSubmissionRequest {

    private Long questionId;
    private String code;   // only method body OR full method
    private String customInput;
}
