package com.example.users_test_task.controllerTests;

import com.example.users_test_task.model.User;
import com.example.users_test_task.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private User testUser;

    /**
     * saveUser function test
     *
     * @param email          - user email
     * @param firstName      - user first name
     * @param lastName       - user last name
     * @param dateOfBirth    - user DoB
     * @param address        - user address
     * @param phoneNumber    - user phone number
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
    public void saveUserTest(String email, String firstName, String lastName, LocalDate dateOfBirth,
                             String address, String phoneNumber, Boolean expectedResult) throws Exception {
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
     * @param email          - user email
     * @param firstName      - user first name
     * @param lastName       - user last name
     * @param dateOfBirth    - user DoB
     * @param address        - user address
     * @param phoneNumber    - user phone number
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
    public void updateUserTest(String email, String firstName, String lastName, LocalDate dateOfBirth,
                               String address, String phoneNumber, Boolean expectedResult) throws Exception {
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

    /**
     * updateFields function test
     *
     * @param data           - fields data
     * @param expectedResult - expected update result
     */
    @Transactional
    @ParameterizedTest
    @MethodSource("fieldsTestData")
    void updateUserFieldsTest(LinkedHashMap<String, Object> data, boolean expectedResult) throws Exception {
        setUpTestUser();

        if (data.containsKey("id")) {
            if (data.get("id").equals("0")) {
                data.replace("id", testUser.getId().intValue());
            } else {
                data.replace("id", 0);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(data);

        MvcResult result = mockMvc.perform(post("/api/v1/users/update_fields")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(expectedResult ? status().isOk() : status().isForbidden())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assert (expectedResult ? response.contains("id") : response.contains("Error"));

        try {
            userRepository.delete(
                    userRepository.findUserByEmail(
                            (data.containsKey("email") && data.get("email") != null && !data.get("email").toString().isBlank()) ?
                                    (String) data.get("email") :
                                    testUser.getEmail()).orElseThrow(
                            () -> new RuntimeException("This user not saved")
                    )
            );
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
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
     * deleteUser function test
     *
     * @param isUserExist    - is user exist in db
     * @param expectedResult - expected result
     * @throws Exception if something wrong
     */
    @ParameterizedTest
    @CsvSource({
            "true, true",
            "false, false"
    })
    public void deleteUserTest(Boolean isUserExist, Boolean expectedResult) throws Exception {
        Long id;

        if (isUserExist) {
            setUpTestUser();

            id = testUser.getId();
        } else {
            id = (long) -1;
        }

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users/delete")
                        .param("id", String.valueOf(id)))
                .andExpect(expectedResult ? status().isOk() : status().isForbidden())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assert (expectedResult ? response.contains("deleted") : response.contains("Error"));

        deleteTestUser();
    }

    /**
     * getUsersByDates function test
     *
     * @param from           - from date
     * @param to             - to date
     * @param expectedResult - expected result
     * @throws Exception if something wrong
     */
    @ParameterizedTest
    @CsvSource({
            "1999-01-01, 2020-01-01, true",
            "2001-01-01, 2020-01-01, true",
            "2020-01-01, 2000-01-01, false"
    })
    public void getUsersByDatesTest(String from, String to, Boolean expectedResult) throws Exception {
        setUpTestUser();

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users/get_users_by_dates")
                        .param("from", from)
                        .param("to", to))
                .andExpect(expectedResult ? status().isOk() : status().isForbidden())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assert (expectedResult ? response.startsWith("[") && response.endsWith("]") : response.contains("Error"));

        deleteTestUser();
    }

    private void setUpTestUser() {
        testUser = new User();

        testUser.setEmail("test_user@gmail.com");
        testUser.setFirstName("test");
        testUser.setLastName("user");
        testUser.setDateOfBirth(LocalDate.of(2000, 1, 1));

        testUser = userRepository.save(testUser);
    }

    private void deleteTestUser() {
        userRepository.delete(testUser);
    }
}
