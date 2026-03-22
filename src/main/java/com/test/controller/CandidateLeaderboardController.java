package com.test.controller;

import com.test.dto.LeaderboardResponse;
import com.test.dto.TodayResultResponse;
import com.test.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidate/leaderboard")
@RequiredArgsConstructor
public class CandidateLeaderboardController {

    private final LeaderboardService leaderboardService;

    // ✅ Get today's leaderboard (all candidates)
    @GetMapping("/today")
    public List<LeaderboardResponse> getTodayLeaderboard() {
        return leaderboardService.getTodayLeaderboard();
    }

    // ✅ Get my today result (rank + score)
    @GetMapping("/my-result")
    public TodayResultResponse getMyResult(@RequestParam String email) {
        return leaderboardService.getTodayResult(email);
    }
}
