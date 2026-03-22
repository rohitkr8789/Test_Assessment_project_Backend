package com.test.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coding_test_cases")
@Data
@NoArgsConstructor
public class CodingTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inputData;

    private String expectedOutput;

    private boolean sample; // visible to candidate

    @ManyToOne
    @JoinColumn(name = "coding_question_id")
    @JsonBackReference
    private CodingQuestion codingQuestion;
}
