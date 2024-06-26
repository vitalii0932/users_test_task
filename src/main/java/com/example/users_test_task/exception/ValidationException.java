package com.example.users_test_task.exception;

import com.example.users_test_task.service.Violation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * validation exception class
 */
@Getter
@RequiredArgsConstructor
public class ValidationException extends Exception {

    private final List<Violation> violations;
}
