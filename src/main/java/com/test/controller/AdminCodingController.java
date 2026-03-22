package com.test.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.dto.CreateCodingQuestionRequest;
import com.test.entity.CodingQuestion;
import com.test.repository.CodingQuestionRepository;
import com.test.service.AdminCodingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/coding")
@RequiredArgsConstructor
public class AdminCodingController {

    private final AdminCodingService adminCodingService;
    private final CodingQuestionRepository codingQuestionRepo;

    // ✅ Create Coding Question + Test Cases
    @PostMapping("/create")
    public String createCodingQuestion(@RequestBody CreateCodingQuestionRequest request) {
        adminCodingService.createCodingQuestion(request);
        return "Coding question created successfully";
    }
    
    @GetMapping("/all")
    public List<CodingQuestion> getAllCodingQuestions() {

        return codingQuestionRepo.findAll();

    }
    
    @DeleteMapping("/delete/{id}")
    public String deleteCodingQuestion(@PathVariable Long id) {

    	codingQuestionRepo.deleteById(id);

        return "Coding Question Deleted";
    }
}
