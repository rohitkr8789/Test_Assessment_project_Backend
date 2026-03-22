package com.test.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.dto.CodingQuestionStatusResponse;
import com.test.dto.CodingSubmissionRequest;
import com.test.dto.CodingSubmissionResponse;
import com.test.dto.RunCodeResponse;
import com.test.entity.CodingQuestion;
import com.test.entity.DailyTest;
import com.test.entity.User;
import com.test.repository.DailyTestCodingRepository;
import com.test.repository.UserRepository;
import com.test.service.CodingService;
import com.test.service.DailyTestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/candidate/coding")
@RequiredArgsConstructor
public class CandidateCodingController {

    private final CodingService codingService;
    private final DailyTestService dailyTestService;
    private final DailyTestCodingRepository dailyTestCodingRepository;
    private final UserRepository userRepository;

    // 1️⃣ Get Today's Coding Question
    @GetMapping("/today")
    public List<CodingQuestion> getTodayCodingQuestions() {

        DailyTest dailyTest = dailyTestService.createTodayTestIfNotExists();

        return dailyTestCodingRepository
                .findCodingQuestionsByDailyTestId(dailyTest.getId());
    }

    @GetMapping("/today-status")
    public List<CodingQuestionStatusResponse> getTodayCodingQuestionsStatus() {

        DailyTest dailyTest = dailyTestService.createTodayTestIfNotExists();

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long candidateId = user.getId();

        return codingService.getTodayCodingQuestionsWithStatus(candidateId, dailyTest.getId());
    }

    // 2️⃣ Run Code (only compile + run)
    @PostMapping("/run")
    public RunCodeResponse runCode(@RequestBody CodingSubmissionRequest request) throws Exception {

         return codingService.runCode(request);

    }

    // 3️⃣ Submit Code (run test cases + calculate score)
    @PostMapping("/submit")
    public CodingSubmissionResponse submit(@RequestBody CodingSubmissionRequest request) throws Exception {

        return codingService.executeAndCalculateScore(request);

    }

}