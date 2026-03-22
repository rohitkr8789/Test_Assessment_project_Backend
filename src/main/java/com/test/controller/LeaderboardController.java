package com.test.controller;

import com.test.dto.LeaderboardResponse;
import com.test.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService service;

    @GetMapping
    public List<LeaderboardResponse> getLeaderboard() {
        return service.getLeaderboard();
    }
    
    @GetMapping("/me")
    public LeaderboardResponse getMyRank() {
        return service.getMyRank();
    }
    
    @GetMapping("/top10")
    public List<LeaderboardResponse> getTop10() {
        return service.getTop10();
    }
    
    @GetMapping("/today")
    public List<LeaderboardResponse> getTodayLeaderboard() {
        return service.getTodayLeaderboard();
    }
}
