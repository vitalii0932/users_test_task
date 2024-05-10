package com.example.users_test_task.service;

import com.example.users_test_task.exception.ValidationException;
import com.example.users_test_task.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * service for validate user
 */
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;

    /**
     *  check does user is valid
     *
     * @param user - user entity
     * @return true if user is valid
     * @throws ValidationException if user not valid
     */
    public boolean isValidUser(User user) throws ValidationException {
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        if (!constraintViolations.isEmpty()) {
            throw new ValidationException(buildViolationsList(constraintViolations));
        }

        return true;
    }

    /**
     * buildViolationsList function
     *
     * @param constraintViolations - constraintViolations from validation
     * @return a list of Violation
     * @param <T> - type
     */
    private <T> List<Violation> buildViolationsList(Set<ConstraintViolation<T>> constraintViolations) {
        return constraintViolations.stream()
                .map(violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .toList();
    }
}
