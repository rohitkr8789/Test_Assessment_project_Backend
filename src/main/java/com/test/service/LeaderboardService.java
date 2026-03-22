package com.test.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.test.dto.LeaderboardResponse;
import com.test.dto.TodayResultResponse;
import com.test.entity.DailyTest;
import com.test.entity.TestAttempt;
import com.test.entity.User;
import com.test.repository.DailyTestRepository;
import com.test.repository.TestAttemptRepository;
import com.test.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final TestAttemptRepository repository;
    private final DailyTestService dailyTestService;
    private final UserRepository userRepository;
    private final DailyTestRepository dailyTestRepository;

    public List<LeaderboardResponse> getLeaderboard() {

        List<Object[]> results = repository.getLeaderboard();
        List<LeaderboardResponse> response = new ArrayList<>();

        for (Object[] row : results) {

            String name = (String) row[0];
            Integer score = (Integer) row[1];
            Integer rank = ((Number) row[2]).intValue();

            response.add(new LeaderboardResponse(
                    name,
                    score,
                    rank,
                    calculateStars(score)
            ));
        }

        return response;
    }

    private Integer calculateStars(Integer score) {
        if (score >= 90) return 5;
        if (score >= 75) return 4;
        if (score >= 60) return 3;
        if (score >= 40) return 2;
        return 1;
    }
    
    
    public LeaderboardResponse getMyRank() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        System.out.println("JWT EMAIL: " + email);
        List<Object[]> results = repository.getLeaderboardWithEmail();

        for (Object[] row : results) {

            String rowEmail = (String) row[0];
            System.out.println("DB EMAIL: " + rowEmail);
            if (rowEmail.equals(email)) {

                String name = (String) row[1];
                Integer score = (Integer) row[2];
                Integer rank = ((Number) row[3]).intValue();

                return new LeaderboardResponse(
                        name,
                        score,
                        rank,
                        calculateStars(score)
                );
            }
        }

        return null;
    }
    
    public List<LeaderboardResponse> getTop10() {

        List<Object[]> results = repository.getTop10Leaderboard();
        List<LeaderboardResponse> response = new ArrayList<>();

        for (Object[] row : results) {

            String name = (String) row[0];
            Integer score = (Integer) row[1];
            Integer rank = ((Number) row[2]).intValue();

            response.add(new LeaderboardResponse(
                    name,
                    score,
                    rank,
                    calculateStars(score)
            ));
        }

        return response;
    }
    
    public List<LeaderboardResponse> getTodayLeaderboard() {

        DailyTest todayTest = dailyTestService.createTodayTestIfNotExists();

        List<Object[]> results =
                repository.getDailyLeaderboard(todayTest.getId());

        List<LeaderboardResponse> response = new ArrayList<>();

        for (Object[] row : results) {

            String name = (String) row[0];
            Integer score = (Integer) row[1];
            Integer rank = ((Number) row[2]).intValue();

            response.add(new LeaderboardResponse(
                    name,
                    score,
                    rank,
                    calculateStars(score)
            ));
        }

        return response;
    }
    
    public TodayResultResponse getTodayResult(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DailyTest dailyTest = dailyTestRepository.findByTestDate(LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No test found for today"));

        TestAttempt attempt = repository
                .findByCandidateIdAndDailyTestId(user.getId(), dailyTest.getId())
                .orElseThrow(() -> new RuntimeException("You have not attempted today's test"));

        Integer rank = repository
                .findRankByCandidateIdAndDailyTestId(user.getId(), dailyTest.getId());

        int stars = calculateStars(rank == null ? 0 : rank);

        return TodayResultResponse.builder()
                .dailyTestId(dailyTest.getId())
                .totalMcq(10) // since daily test is fixed 10 MCQs
                .correctAnswers(attempt.getMcqScore() / 10) // if 10 marks per question
                .score(attempt.getTotalScore())
                .rank(rank)
                .stars(stars)
                .attemptedAt(attempt.getAttemptTime())
                .build();
    }
}
