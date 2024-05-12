package com.example.users_test_task.service;

import com.example.users_test_task.dto.UserDTO;
import com.example.users_test_task.exception.ValidationException;
import com.example.users_test_task.mapper.UserMapper;
import com.example.users_test_task.model.User;
import com.example.users_test_task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * service for working with user entities and db
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ValidationService validationService;

    /**
     * save user in db
     *
     * @param userDTO - user data
     * @return - saved user entity
     * @throws IllegalArgumentException if something is wrong
     * @throws ValidationException      if something is wrong on validation
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public User save(UserDTO userDTO) throws IllegalArgumentException, ValidationException {
        var user = userMapper.toUser(userDTO);
        validationService.isValidUser(user);
        isAgeValid(user.getDateOfBirth());
        isEmailNotExist(user.getEmail());
        return userRepository.save(user);
    }

    /**
     * update some fields in user entity
     *
     * @param fields - fields to update
     * @return updated user entity from db
     * @throws IllegalArgumentException if something is wrong
     * @throws ValidationException      if something is wrong on validation
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public User updateFields(LinkedHashMap<String, Object> fields) throws IllegalArgumentException, ValidationException {
        if (!fields.containsKey("id") && !fields.keySet().toArray()[0].equals("id")) {
            throw new IllegalArgumentException("Error. Incorrect input data. The input data must have id. And id must to be in the first place");
        }

        var userToUpdate = userRepository.findById(Long.valueOf((Integer) fields.get("id"))).orElseThrow(
                () -> new IllegalArgumentException("Error. No user with this ID found")
        );

        var user = new User();
        user.copy(userToUpdate);

        isEmailNotExist((String) fields.get("email"));

        for (var field : fields.keySet()) {
            if (field.equals("id")) {
                continue;
            }
            try {
                Field declaredField = user.getClass().getDeclaredField(field);
                declaredField.setAccessible(true);
                declaredField.set(user, fields.get(field));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Error. Incorrect field: " + field);
            }
        }

        validationService.isValidUser(user);
        isAgeValid(user.getDateOfBirth());
        isDataValid(user.getDateOfBirth());

        return userRepository.save(user);
    }

    /**
     * update user in db
     *
     * @param updatedUserDTO - user data to update
     * @throws IllegalArgumentException if something is wrong
     * @throws ValidationException      if something is wrong on validation
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public User update(UserDTO updatedUserDTO) throws Exception {
        var userToUpdate = userRepository.findById(updatedUserDTO.getId()).orElseThrow(
                () -> new IllegalArgumentException("User not found exception")
        );

        var user = new User();
        user.copy(userToUpdate);

        user.setEmail(updatedUserDTO.getEmail());
        user.setFirstName(updatedUserDTO.getFirstName());
        user.setLastName(updatedUserDTO.getLastName());
        user.setDateOfBirth(updatedUserDTO.getDateOfBirth());
        user.setAddress(updatedUserDTO.getAddress());
        user.setPhoneNumber(updatedUserDTO.getPhoneNumber());

        validationService.isValidUser(user);
        isAgeValid(user.getDateOfBirth());
        isDataValid(user.getDateOfBirth());

        return userRepository.save(user);
    }

    /**
     * check does age is valid
     *
     * @param dateOfBirth - date of birth
     * @throws IllegalArgumentException if something is wrong
     */
    public void isAgeValid(LocalDate dateOfBirth) throws IllegalArgumentException {
        if (dateOfBirth != null && Period.between(dateOfBirth, LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException("Error. I'm sorry, but you're too young");
        }
    }

    /**
     * check does email is not exist
     *
     * @param email - email
     * @throws IllegalArgumentException if something is wrong
     */
    @Transactional(readOnly = true)
    public void isEmailNotExist(String email) {
        if (email != null && !email.isEmpty() && userRepository.findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Error. This email is already used");
        }
    }

    /**
     * check does date is valid
     *
     * @param date - date
     * @throws IllegalArgumentException is something wrong
     */
    public void isDataValid(LocalDate date) throws IllegalArgumentException {
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Error. The date cannot be the future");
        }
    }

    /**
     * delete user by his id
     *
     * @param id - user id
     * @throws RuntimeException if something is wrong
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void delete(Long id) throws RuntimeException {
        if (id == null || userRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Error. User with this id doesnt exist");
        }
        userRepository.deleteById(id);
    }

    /**
     * get users by date of birth from @param from to @param to
     *
     * @param from - from date
     * @param to   - to date
     * @return a list of users
     * @throws IllegalArgumentException if dates dont valid
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByDates(LocalDate from, LocalDate to) throws IllegalArgumentException {
        isDataValid(from);
        isDataValid(to);

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Error. Start date cannot be after end date");
        }
        return userRepository.getUsersByDateOfBirthBetween(from, to);
    }
}
