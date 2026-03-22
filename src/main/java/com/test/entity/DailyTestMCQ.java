package com.test.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "daily_test_mcq")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyTestMCQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "daily_test_id", nullable = false)
    private DailyTest dailyTest;

    @ManyToOne
    @JoinColumn(name = "mcq_id", nullable = false)
    @JsonBackReference
    private MCQ mcq;
}
