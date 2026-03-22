package com.test.service;

import org.springframework.stereotype.Service;

import com.test.dto.CreateCodingQuestionRequest;
import com.test.entity.CodingQuestion;
import com.test.entity.CodingTestCase;
import com.test.repository.CodingQuestionRepository;
import com.test.repository.CodingTestCaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCodingService {

    private final CodingQuestionRepository codingQuestionRepository;
    private final CodingTestCaseRepository codingTestCaseRepository;

    public void createCodingQuestion(CreateCodingQuestionRequest request) {

        // 1️⃣ Save Question
        CodingQuestion question = new CodingQuestion();
        question.setTitle(request.getTitle());
        question.setDescription(request.getDescription());
        question.setMethodSignature(request.getMethodSignature());
        question.setMarks(request.getMarks());
        question.setActive(true);

        CodingQuestion savedQuestion = codingQuestionRepository.save(question);

        // 2️⃣ Save Test Cases
        for (CreateCodingQuestionRequest.TestCaseDTO tc : request.getTestCases()) {

            CodingTestCase testCase = new CodingTestCase();
            testCase.setInputData(tc.getInputData());
            testCase.setExpectedOutput(tc.getExpectedOutput());
            testCase.setSample(tc.isSample());
            testCase.setCodingQuestion(savedQuestion);

            codingTestCaseRepository.save(testCase);
        }
    }
}
