package com.example.users_test_task.serviceTests;

import com.example.users_test_task.exception.ValidationException;
import com.example.users_test_task.model.User;
import com.example.users_test_task.service.ValidationService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * ValidationService tests
 */
@SpringBootTest
public class ValidationServiceTests {
    
    @Autowired
    private ValidationService validationService;

    /**
     * isValidUser function test
     *
     * @param email - user email
     * @param firstName - user first name
     * @param lastName - user last name
     * @param dateOfBirth - user DoB
     * @param address - user address
     * @param phoneNumber - user phone number
     * @param expectedResult - user saved or not
     */
    @Transactional
    @ParameterizedTest
    @CsvSource({
            "test_user@gmail.com, test, user, 2000-01-01, address, 1234567890, true",
            "test_user@gmail.com, test, user, 2000-01-01, , 1234567890, true",
            "test_user@gmail.com, test, user, 2000-01-01, address, , true",
            "test_user, test, user, 2000-01-01, address, 1234567890, false",
            "test_user@gmail.com, , user, 2000-01-01, address, 1234567890, false",
            "test_user@gmail.com, test, , 2000-01-01, address, 1234567890, false",
            "test_user@gmail.com, test, user, 2025-01-01, address, 1234567890, false"
    })
    public void isValidUserTest(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, Boolean expectedResult) {
        var user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);

        try {
            assert (expectedResult == validationService.isValidUser(user));
        } catch (ValidationException e) {
            if (expectedResult) {
                System.out.printf("Error in case: %s - %s - %s - %s - %s - %s",
                        email, firstName, lastName, dateOfBirth.toString(), address, phoneNumber);
                assert (false);
            }
            System.out.println(e.getViolations());
        }
    }
}
