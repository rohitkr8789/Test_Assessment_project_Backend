package com.test.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "daily_test_coding")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyTestCoding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "daily_test_id")
    private DailyTest dailyTest;

    @ManyToOne
    @JoinColumn(name = "coding_question_id")
    @JsonBackReference
    private CodingQuestion codingQuestion;
}
