package com.example.users_test_task.service;

import com.example.users_test_task.dto.UserDTO;
import com.example.users_test_task.exception.ValidationException;
import com.example.users_test_task.mapper.UserMapper;
import com.example.users_test_task.model.User;
import com.example.users_test_task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;

/**
 * service for working with user entities and db
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ValidationService validationService;

    /**
     * save user in db
     *
     * @param user - user entity
     * @return - saved user entity
     * @throws IllegalArgumentException if something is wrong
     * @throws ValidationException if something is wrong on validation
     */
    public User save(User user) throws IllegalArgumentException, ValidationException {
        if (validationService.isValidUser(user)) {
            isAgeValid(user.getDateOfBirth());
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * save user in db
     *
     * @param userDTO - user data
     * @return - saved user entity
     * @throws IllegalArgumentException if something is wrong
     * @throws ValidationException if something is wrong on validation
     */
    public User save(UserDTO userDTO) throws IllegalArgumentException, ValidationException {
        var user = userMapper.toUser(userDTO);
        if (validationService.isValidUser(user)) {
            isAgeValid(user.getDateOfBirth());
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * update some fields in user entity
     *
     * @param fields - fields to update
     * @return updated user entity from db
     * @throws IllegalArgumentException if something is wrong
     * @throws ValidationException if something is wrong on validation
     */
    public User updateFields(LinkedHashMap<String, Object> fields) throws IllegalArgumentException, ValidationException {
        if (!fields.containsKey("id") && !fields.keySet().toArray()[0].equals("id")) {
            throw new IllegalArgumentException("Incorrect input data. The input data must have id. And id must to be in the first place");
        }

        var user = userRepository.findById((Long) fields.get("id")).orElseThrow(
                () -> new IllegalArgumentException("No user with this ID found")
        );

        for (var field : fields.keySet()) {
            if (field.equals("id")) {
                continue;
            }
            try {
                Field declaredField = user.getClass().getDeclaredField(field);
                declaredField.setAccessible(true);
                declaredField.set(user, fields.get(field));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Incorrect field: " + field);
            }
        }

        return save(user);
    }

    /**
     * update user in db
     *
     * @param updatedUserDTO - user data to update
     * @throws IllegalArgumentException if something is wrong
     * @throws ValidationException if something is wrong on validation
     */
    public User update(UserDTO updatedUserDTO) throws IllegalArgumentException, ValidationException {
        var user = userRepository.findById(updatedUserDTO.getId()).orElseThrow(
                () -> new IllegalArgumentException("User not found exception")
        );

        user.setEmail(updatedUserDTO.getEmail());
        user.setFirstName(updatedUserDTO.getFirstName());
        user.setLastName(updatedUserDTO.getLastName());
        user.setDateOfBirth(updatedUserDTO.getDateOfBirth());
        user.setAddress(updatedUserDTO.getAddress());
        user.setPhoneNumber(updatedUserDTO.getPhoneNumber());

        return save(user);
    }

    /**
     * check does age is valid
     *
     * @param dateOfBirth - date of birth
     * @throws IllegalArgumentException if something is wrong
     */
    private void isAgeValid(LocalDate dateOfBirth) throws IllegalArgumentException {
        if (Period.between(LocalDate.now(), dateOfBirth).getYears() < 18) {
            throw new IllegalArgumentException("I'm sorry, but you're too young");
        }
    }

    /**
     * delete user by his id
     *
     * @param id - user id
     * @throws RuntimeException if something is wrong
     */
    public void delete(Long id) throws RuntimeException {
        if (userRepository.findById(id).isEmpty()) {
            throw new RuntimeException("User with this id doesnt exist");
        }
        userRepository.deleteById(id);
    }
}
