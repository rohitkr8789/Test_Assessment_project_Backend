package com.test.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "coding_questions")
@Data
public class CodingQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 10000)
    private String description;

    private String methodSignature; 
    // example: public static int sum(int a, int b)

    private int marks;

    private boolean active;
    
    @OneToMany(mappedBy = "codingQuestion",cascade = CascadeType.ALL,orphanRemoval = true)
    	@JsonManagedReference
    	private List<CodingTestCase> testCases;
    
    @OneToMany(mappedBy = "codingQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DailyTestCoding> dailyTestCodings;
    
    @OneToMany(mappedBy = "codingQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CodingSubmission> submissions;
}
