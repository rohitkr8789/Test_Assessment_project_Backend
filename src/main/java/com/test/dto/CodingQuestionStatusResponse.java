package com.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodingQuestionStatusResponse {

    private Long id;
    private String description;
    private int marks;
    private boolean submitted;
   
}