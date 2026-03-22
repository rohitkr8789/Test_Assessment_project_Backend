package com.test.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.dto.MCQResponse;
import com.test.dto.MCQSubmissionRequest;
import com.test.entity.DailyTest;
import com.test.entity.MCQ;
import com.test.entity.TestAttempt;
import com.test.entity.User;
import com.test.repository.DailyTestMCQRepository;
import com.test.repository.DailyTestRepository;
import com.test.repository.TestAttemptRepository;
import com.test.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MCQService {

    private final DailyTestService dailyTestService;
    private final DailyTestMCQRepository dailyTestMCQRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final UserRepository userRepository;
    private final DailyTestRepository dailyTestRepository;


    public List<MCQResponse> getTodayMCQs() {

        DailyTest todayTest = dailyTestService.createTodayTestIfNotExists();

        List<MCQ> mcqs =
                dailyTestMCQRepository.findMCQsByDailyTestId(todayTest.getId());

        return mcqs.stream()
                .map(mcq -> MCQResponse.builder()
                        .id(mcq.getId())
                        .question(mcq.getQuestion())
                        .optionA(mcq.getOptionA())
                        .optionB(mcq.getOptionB())
                        .optionC(mcq.getOptionC())
                        .optionD(mcq.getOptionD())
                        .build())
                .toList();
    }
    
    @Transactional
    public int submitTodayMCQ(MCQSubmissionRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DailyTest todayTest = dailyTestRepository.findByTestDate(LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Today's test not generated"));

        if (testAttemptRepository.existsByCandidate_IdAndDailyTest_Id(
                candidate.getId(),
                todayTest.getId())) {

            throw new RuntimeException("You already attempted today's test");
        }

        List<MCQ> todayMcqs =
                dailyTestMCQRepository.findMCQsByDailyTestId(todayTest.getId());

        int score = 0;

        for (MCQ mcq : todayMcqs) {
            String selectedAnswer = request.getAnswers().get(mcq.getId());

            if (selectedAnswer != null &&
                    selectedAnswer.equalsIgnoreCase(mcq.getCorrectAnswer())) {
                score++;
            }
        }

        System.out.println(">>> SAVING ATTEMPT <<<");
        System.out.println("DailyTest ID = " + todayTest.getId());

        TestAttempt attempt = TestAttempt.builder()
                .candidate(candidate)
                .mcqScore(score)
                .codingScore(0)
                .totalScore(score)
                .attemptTime(LocalDateTime.now())
                .dailyTest(todayTest)
                .build();

        System.out.println("Attempt dailyTest before save = " + attempt.getDailyTest());

        testAttemptRepository.save(attempt);

        return score;
    }
}
