package com.test.controller;

import java.util.List;
import com.test.service.MCQSaveService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.test.dto.MCQRequest;
import com.test.entity.MCQ;
import com.test.entity.User;
import com.test.repository.UserRepository;
import com.test.service.AIService;

@RestController
@RequestMapping("/admin/ai")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AIController {

    private final MCQSaveService mcqSaveService;
	private final AIService aiService;
	private final UserRepository userRepository;


    @PostMapping("/generate")
    public List<MCQRequest> generate(@RequestParam String topic) {
        return aiService.generateMCQ(topic);
    }

    @PostMapping("/save")
    public List<MCQ> save(@RequestBody List<MCQRequest> list,
                          Authentication authentication) {

        String email = authentication.getName();

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        list.forEach(mcq -> {
            if (mcq.getCorrectAnswer() != null) {
                mcq.setCorrectAnswer(mcq.getCorrectAnswer().substring(0,1).toUpperCase());
            }
        });

        return mcqSaveService.saveAll(list, admin);
    }
}