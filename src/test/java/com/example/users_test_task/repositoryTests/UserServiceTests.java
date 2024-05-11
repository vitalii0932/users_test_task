package com.example.users_test_task.repositoryTests;

import com.example.users_test_task.dto.UserDTO;
import com.example.users_test_task.exception.ValidationException;
import com.example.users_test_task.mapper.UserMapper;
import com.example.users_test_task.model.User;
import com.example.users_test_task.repository.UserRepository;
import com.example.users_test_task.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * UserService tests
 */
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    /**
     * saveUser function test
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
            "test_user@gmail.com, test, user, 2025-01-01, address, 1234567890, false",
            "test_user@gmail.com, test, user, 2007-01-01, address, 1234567890, false"
    })
    public void saveUserTest(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, Boolean expectedResult){
        var user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);

        try {
            userService.save(user);
            assert (userRepository.findUserByEmail(email).isPresent());

            userRepository.delete(userRepository.findUserByEmail(email).get());
        } catch (Exception e) {
            if (expectedResult) {
                System.out.printf("Error in case: %s - %s - %s - %s - %s - %s",
                        email, firstName, lastName, dateOfBirth.toString(), address, phoneNumber);
                assert (false);
            }
            if (e.getClass().equals(ValidationException.class)) {
                System.out.println(((ValidationException) e).getViolations());
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * updateFields function test
     *
     * @param data - fields data
     * @param expectedResult - expected update result
     */
    @Transactional
    @ParameterizedTest
    @MethodSource("fieldsTestData")
    void updateFieldsTest(LinkedHashMap<String, Object> data, boolean expectedResult) {
        var testUser = new User();
        var updatedUser = new User();

        testUser.setEmail("test_user@gmail.com");
        testUser.setFirstName("test");
        testUser.setLastName("user");
        testUser.setDateOfBirth(LocalDate.of(2000, 1, 1));

        testUser = userRepository.save(testUser);

        try {
            if (data.containsKey("id")) {
                if (data.get("id").equals("0")) {
                    data.replace("id", testUser.getId().intValue());
                } else {
                    data.replace("id", 0);
                }
            }

            updatedUser = userService.updateFields(data);

            for (var field : data.keySet()) {
                Field declaredField = updatedUser.getClass().getDeclaredField(field);
                declaredField.setAccessible(true);
                String value = String.valueOf(declaredField.get(updatedUser));
                String expectedValue = String.valueOf(data.get(field));

                assert(expectedValue.equals(value));
            }
        } catch (Exception e) {
            if (expectedResult) {
                System.out.print("Error in case: " + data.toString());
                assert (false);
            }
            e.printStackTrace();
        }

        userRepository.delete(testUser);
        userRepository.delete(updatedUser);
    }

    /**
     * set up test fields and expected result
     *
     * @return a stream of Arguments
     */
    private static Stream<Arguments> fieldsTestData() {
        return Stream.of(
                Arguments.of(createTestData("id", "0", "email", "test_user1@gmail.com"), true),
                Arguments.of(createTestData("id", "0", "email", "test_user@gmail.com"), false),
                Arguments.of(createTestData("id", "-1", "email", "test_user1@gmail.com"), false),
                Arguments.of(createTestData("id", "0", "email", "test_user1@gmail.com", "firstName", "new name"), true),
                Arguments.of(createTestData("email", "test_user1@gmail.com", "firstName", "new name"), false)
        );
    }

    /**
     * set up test fields
     *
     * @param data - data in map
     * @return a map with data about fields
     */
    private static Map<String, Object> createTestData(String... data) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < data.length; i += 2) {
            map.put(data[i], i + 1 < data.length ? data[i + 1] : null);
        }
        return map;
    }

    /**
     * update function test
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
            "test_user1@gmail.com, test, user, 2000-01-01, address, 1234567890, true",
            "test_user1@gmail.com, test, user, 2000-01-01, , 1234567890, true",
            "test_user1@gmail.com, test, user, 2000-01-01, address, , true",
            "test_user@gmail.com, test, user, 2000-01-01, address, 1234567890, false",
            "test_user, test, user, 2000-01-01, address, 1234567890, false",
            "test_user1@gmail.com, , user, 2000-01-01, address, 1234567890, false",
            "test_user1@gmail.com, test, , 2000-01-01, address, 1234567890, false",
            "test_user1@gmail.com, test, user, 2025-01-01, address, 1234567890, false",
            "test_user1@gmail.com, test, user, 2007-01-01, address, 1234567890, false"
    })
    public void updateTest(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, Boolean expectedResult) {
        var testUser = new User();

        testUser.setEmail("test_user@gmail.com");
        testUser.setFirstName("test");
        testUser.setLastName("user");
        testUser.setDateOfBirth(LocalDate.of(2000, 1, 1));

        testUser = userRepository.save(testUser);

        var user = new UserDTO();
        user.setId(testUser.getId());
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);

        try {
            var updatedUser = userService.update(user);

            assert (updatedUser.equals(
                    userMapper.toUser(user)
            ));

            userRepository.delete(userRepository.findUserByEmail(email).get());
        } catch (Exception e) {
            userRepository.delete(testUser);
            if (expectedResult) {
                System.out.printf("Error in case: %s - %s - %s - %s - %s - %s",
                        email, firstName, lastName, dateOfBirth.toString(), address, phoneNumber);
                assert (false);
            }
            if (e.getClass().equals(ValidationException.class)) {
                System.out.println(((ValidationException) e).getViolations());
            } else {
                e.printStackTrace();
            }
        }
    }
}
