package com.test.repository;

import com.test.entity.DailyTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyTestRepository extends JpaRepository<DailyTest, Long> {

    Optional<DailyTest> findByTestDate(LocalDate testDate);
}
