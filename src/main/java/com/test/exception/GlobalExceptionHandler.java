package com.test.exception;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {

        String message = ex.getMessage();

        if (message != null && message.startsWith("Line")) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", "COMPILE_ERROR",
                            "message", message
                    )
            );
        }

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", "ERROR",
                        "message", message
                )
        );
    }
}
