package com.example.users_test_task.repositoryTests;

import com.example.users_test_task.model.User;
import com.example.users_test_task.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * UserRepo tests
 */
@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    /**
     * set the test user before all the tests
     */
    @BeforeEach
    public void setUpTestUser() {
        testUser = new User();
        testUser.setEmail("test_email@gmail.com");
        testUser.setFirstName("test");
        testUser.setLastName("user");
        testUser.setDateOfBirth(LocalDate.of(2004, 5, 7));
        testUser.setAddress("test address");
        testUser.setPhoneNumber("1234567890");

        testUser = userRepository.save(testUser);
    }

    /**
     * delete all the test users
     */
    @AfterEach
    public void deleteTestUser() {
        userRepository.delete(testUser);
    }

    /**
     * findUserByEmail function test
     */
    @Test
    public void findUserByEmailTest() {
        var foundedUser = userRepository.findUserByEmail(testUser.getEmail()).orElseThrow(
                () -> new RuntimeException("Test user not found")
        );

        assert (foundedUser.equals(testUser));
    }

    /**
     * getUsersByDateOfBirthBetween function test
     *
     * @param from - from date
     * @param to - to date
     * @param expectedResult - user found or not
     */
    @ParameterizedTest
    @CsvSource({
            "2000-01-01, 2001-01-01, false",
            "2000-01-01, 2021-01-01, true"
    })
    public void getUsersByDateOfBirthBetweenTest(LocalDate from, LocalDate to, Boolean expectedResult) {
        var result = userRepository.getUsersByDateOfBirthBetween(from, to);

        assert (expectedResult == result.contains(testUser));
    }
}
