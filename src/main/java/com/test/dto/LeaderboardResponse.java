package com.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardResponse {

    private String candidateName;
    private Integer score;
    private Integer rank;
    private Integer stars;
}
