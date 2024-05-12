package com.example.users_test_task.controllerTests;

import com.example.users_test_task.dto.UserDTO;
import com.example.users_test_task.model.User;
import com.example.users_test_task.repository.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserDataController test
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserDataControllerTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

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
    @ParameterizedTest
    @CsvSource({
            "test_user@gmail.com, test, user, 2000-01-01, address, 1234567890, true",
            "test_user@gmail.com, test, user, 2000-01-01, , 1234567890, true",
            "test_user@gmail.com, test, user, 2000-01-01, address, , true",
            "test_user, test, user, 2000-01-01, address, 1234567890, false",
            "test_user@gmail.com, , user, 2000-01-01, address, 1234567890, false",
            "test_user@gmail.com, test, , 2000-01-01, address, 1234567890, false",
            "test_user@gmail.com, test, user, 2025-01-01, address, 1234567890, false",
            "test_user@gmail.com, test, user, 2012-01-01, address, 1234567890, false"
    })
    public void saveUserTest(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, Boolean expectedResult) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"email\": \"" + email + "\","
                                + "\"firstName\": \"" + (firstName == null ? "" : firstName) + "\","
                                + "\"lastName\": \"" + (lastName == null ? "" : lastName) + "\","
                                + "\"dateOfBirth\": \"" + dateOfBirth + "\","
                                + "\"address\": \"" + address + "\","
                                + "\"phoneNumber\": \"" + (phoneNumber == null ? "" : phoneNumber) + "\""
                                + "}"))
                .andExpect(expectedResult ? status().isOk() : status().isForbidden())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assert (expectedResult ? response.contains("id") : response.contains("Error"));

        try {
            userRepository.delete(
                    userRepository.findUserByEmail(email).orElseThrow(
                            () -> new RuntimeException("This user not saved")
                    )
        );
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * updateUser function test
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
    public void updateUserTest(String email, String firstName, String lastName, LocalDate dateOfBirth, String address, String phoneNumber, Boolean expectedResult) throws Exception {
        var testUser = new User();

        testUser.setEmail("test_user@gmail.com");
        testUser.setFirstName("test");
        testUser.setLastName("user");
        testUser.setDateOfBirth(LocalDate.of(2000, 1, 1));

        testUser = userRepository.save(testUser);

        MvcResult result = mockMvc.perform(post("/api/v1/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"id\": \"" + testUser.getId() + "\","
                                + "\"email\": \"" + email + "\","
                                + "\"firstName\": \"" + (firstName == null ? "" : firstName) + "\","
                                + "\"lastName\": \"" + (lastName == null ? "" : lastName) + "\","
                                + "\"dateOfBirth\": \"" + dateOfBirth + "\","
                                + "\"address\": \"" + address + "\","
                                + "\"phoneNumber\": \"" + (phoneNumber == null ? "" : phoneNumber) + "\""
                                + "}"))
                .andExpect(expectedResult ? status().isOk() : status().isForbidden())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assert (expectedResult ? response.contains("id") : response.contains("Error"));

        try {
            userRepository.delete(
                    userRepository.findUserByEmail(email).orElseThrow(
                            () -> new RuntimeException("This user not saved")
                    )
            );
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
