package com.test.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.service.DailyTestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/daily-test")
@RequiredArgsConstructor
public class DailyTestController {

    private final DailyTestService service;

    @PostMapping("/generate")
    public String generate() {
        service.createTodayTestIfNotExists();
        return "Daily test created if not exists";
    }
}
