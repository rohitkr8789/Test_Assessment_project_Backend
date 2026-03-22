package com.test.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.dto.MCQRequest;
import com.test.entity.MCQ;
import com.test.entity.User;
import com.test.repository.MCQRepository;
import com.test.repository.UserRepository;
import com.test.service.MCQSaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/mcq")
@RequiredArgsConstructor
public class AdminMCQController {

    private final MCQRepository mcqRepository;
    private final UserRepository userRepository;
    private final MCQSaveService mcqSaveService;

    @PostMapping
    public String addMCQ(@RequestBody MCQRequest request, Authentication authentication) {

        String email = authentication.getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // normalize answer
        if (request.getCorrectAnswer() != null) {
            request.setCorrectAnswer(request.getCorrectAnswer().substring(0,1).toUpperCase());
        }

        // call common service
        mcqSaveService.saveAll(List.of(request), admin);

        return "MCQ added successfully";
    }
    
    @GetMapping("/all")
    public Page<MCQ> getAllMCQQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String search
    ) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (search != null && !search.isEmpty()) {
            return mcqRepository.findByQuestionContainingIgnoreCase(search, pageable);
        }

        return mcqRepository.findAll(pageable);
    }
    
    @DeleteMapping("/delete/{id}")
    public String deleteMCQ(@PathVariable Long id) {

        mcqRepository.deleteById(id);

        return "MCQ deleted successfully";
    }
}
