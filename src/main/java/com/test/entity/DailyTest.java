package com.test.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_tests",
       uniqueConstraints = @UniqueConstraint(columnNames = "test_date"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_date", nullable = false, unique = true)
    private LocalDate testDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
