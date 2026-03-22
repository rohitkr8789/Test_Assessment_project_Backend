package com.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardStats {

    private long totalCandidates;
    private long totalMcqQuestions;
    private long totalCodingQuestions;
    private long totalSubmissions;
 
}
