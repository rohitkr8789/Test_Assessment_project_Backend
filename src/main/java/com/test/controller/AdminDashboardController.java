package com.test.controller;

import org.springframework.web.bind.annotation.*;
import com.test.dto.AdminDashboardStats;
import com.test.repository.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final UserRepository userRepository;
    private final MCQRepository mcqRepository;
    private final CodingQuestionRepository codingRepository;
    private final CodingSubmissionRepository submissionRepository;

    @GetMapping("/dashboard-stats")
    public AdminDashboardStats getStats() {

        long totalCandidates = userRepository.count();
        long totalMcq = mcqRepository.count();
        long totalCoding = codingRepository.count();
        long totalSubmissions = submissionRepository.count();

        return new AdminDashboardStats(
                totalCandidates,
                totalMcq,
                totalCoding,
                totalSubmissions
        );
    }
}