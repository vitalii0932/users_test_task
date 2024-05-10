package com.example.users_test_task.exception;

import com.example.users_test_task.service.Violation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * validation exception class
 */
@RequiredArgsConstructor
public class ValidationException extends Exception {

    @Getter
    private final List<Violation> violations;
}
