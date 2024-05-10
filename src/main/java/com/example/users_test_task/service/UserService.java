package com.example.users_test_task.service;

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

    /**
     * save user in db
     *
     * @param user - user model
     * @return - saved user entity
     */
    public User save(User user) throws IllegalArgumentException {
        isEmailUsed(user.getEmail());
        isAgeValid(user.getDateOfBirth());
        return userRepository.save(user);
    }

    /**
     * update some fields in user entity
     *
     * @param fields - fields to update
     * @return updated user entity from db
     * @throws IllegalArgumentException if something is wrong
     */
    public User updateFields(LinkedHashMap<String, Object> fields) throws IllegalArgumentException {
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

        return userRepository.save(user);
    }

    /**
     * update user in db
     *
     * @param updatedUser - user data to update
     * @throws IllegalArgumentException if something is wrong
     */
    public User update(User updatedUser) throws IllegalArgumentException {
        var user = userRepository.findById(updatedUser.getId()).orElseThrow(
                () -> new IllegalArgumentException("User not found exception")
        );

        isEmailUsed(updatedUser.getEmail());
        isAgeValid(updatedUser.getDateOfBirth());

        user.setEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setDateOfBirth(updatedUser.getDateOfBirth());
        user.setAddress(updatedUser.getAddress());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        return userRepository.save(user);
    }

    /**
     * check does email is valid
     *
     * @param email - user email
     * @throws IllegalArgumentException if something is wrong
     */
    private void isEmailUsed(String email) throws IllegalArgumentException {
        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("This email is used in system now");
        }
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
}
