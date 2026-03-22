package com.test.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.dto.MCQRequest;
import com.test.entity.MCQ;
import com.test.entity.User;
import com.test.repository.MCQRepository;

@Service
public class MCQSaveService {

    @Autowired
    private MCQRepository mcqRepository;

    public List<MCQ> saveAll(List<MCQRequest> list, User admin) {

        List<MCQ> mcqs = list.stream().map(data ->

            MCQ.builder()
                .question(data.getQuestion())
                .optionA(data.getOptionA())
                .optionB(data.getOptionB())
                .optionC(data.getOptionC())
                .optionD(data.getOptionD())
                .correctAnswer(data.getCorrectAnswer().toUpperCase())
                .createdBy(admin)
                .createdAt(LocalDateTime.now())
                .build()

        ).toList();

        return mcqRepository.saveAll(mcqs);
    }
}
