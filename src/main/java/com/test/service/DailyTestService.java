package com.test.service;

import com.test.entity.CodingQuestion;
import com.test.entity.DailyTest;
import com.test.entity.DailyTestCoding;
import com.test.entity.DailyTestMCQ;
import com.test.entity.MCQ;
import com.test.repository.CodingQuestionRepository;
import com.test.repository.DailyTestCodingRepository;
import com.test.repository.DailyTestMCQRepository;
import com.test.repository.DailyTestRepository;
import com.test.repository.MCQRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyTestService {

    private final DailyTestRepository dailyTestRepository;
    private final MCQRepository mcqRepository;
    private final DailyTestMCQRepository dailyTestMCQRepository;
    private final DailyTestCodingRepository dailyTestCodingRepository;
    private final CodingQuestionRepository codingQuestionRepository;

    // 🔥 Automatically runs every midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailyTestAtMidnight() {
        createTodayTestIfNotExists();
    }

    // 🔥 Safe method used by APIs also
    public DailyTest createTodayTestIfNotExists() {

        LocalDate today = LocalDate.now();

        // 1️⃣ Check if today's test exists
        DailyTest dailyTest = dailyTestRepository
                .findByTestDate(today)
                .orElseGet(() -> {

                    DailyTest newTest = DailyTest.builder()
                            .testDate(today)
                            .createdAt(LocalDateTime.now())
                            .build();

                    return dailyTestRepository.saveAndFlush(newTest);
                });

        // ==========================
        // ASSIGN MCQ QUESTIONS
        // ==========================

        boolean hasMcqs = !dailyTestMCQRepository
                .findMCQsByDailyTestId(dailyTest.getId())
                .isEmpty();

        if (!hasMcqs) {

            List<MCQ> randomMcqs = mcqRepository.getRandom10();

            for (MCQ mcq : randomMcqs) {

                DailyTestMCQ mapping = DailyTestMCQ.builder()
                        .dailyTest(dailyTest)
                        .mcq(mcq)
                        .build();

                dailyTestMCQRepository.save(mapping);
            }
        }

        // ==========================
        // ASSIGN CODING QUESTIONS
        // ==========================

        List<DailyTestCoding> existingCoding =
                dailyTestCodingRepository.findByDailyTestId(dailyTest.getId());

        if (existingCoding.size() < 2) {

            List<CodingQuestion> randomCoding =
                    codingQuestionRepository.getRandom2CodingQuestions();

            for (CodingQuestion cq : randomCoding) {

                DailyTestCoding mapping = DailyTestCoding.builder()
                        .dailyTest(dailyTest)
                        .codingQuestion(cq)
                        .build();

                dailyTestCodingRepository.save(mapping);
            }
        }

        return dailyTest;
    }
}