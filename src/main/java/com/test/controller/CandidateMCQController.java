package com.test.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.dto.MCQResponse;
import com.test.dto.MCQSubmissionRequest;
import com.test.dto.MCQSubmissionResponse;
import com.test.entity.MCQ;
import com.test.entity.TestAttempt;
import com.test.entity.User;
import com.test.repository.MCQRepository;
import com.test.repository.TestAttemptRepository;
import com.test.repository.UserRepository;
import com.test.service.MCQService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/candidate/mcq")
@RequiredArgsConstructor
public class CandidateMCQController {

    private final MCQRepository mcqRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final UserRepository userRepository;
    private final MCQService mcqService;

    @GetMapping
    public List<MCQResponse> getMCQs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Page<MCQ> mcqPage = mcqRepository.findAll(PageRequest.of(page, size));

        return mcqPage.getContent()
                .stream()
                .map(mcq -> MCQResponse.builder()
                        .id(mcq.getId())
                        .question(mcq.getQuestion())
                        .optionA(mcq.getOptionA())
                        .optionB(mcq.getOptionB())
                        .optionC(mcq.getOptionC())
                        .optionD(mcq.getOptionD())
                        .build())
                .collect(Collectors.toList());
    }
    
    @PostMapping("/submit")
    public MCQSubmissionResponse submitMCQ(
            @RequestBody MCQSubmissionRequest request
    ) {

        int score = mcqService.submitTodayMCQ(request);

        return MCQSubmissionResponse.builder()
                .totalQuestions(request.getAnswers().size())
                .correctAnswers(score)
                .score(score)
                .build();
    }
    
    @GetMapping("/today")
    public List<MCQResponse> getTodayMCQs() {
        return mcqService.getTodayMCQs();
    }
}
