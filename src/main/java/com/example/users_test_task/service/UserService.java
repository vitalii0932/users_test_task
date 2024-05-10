package com.example.users_test_task.service;

import com.example.users_test_task.model.User;
import com.example.users_test_task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

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
     * update user in db
     *
     * @param updatedUser - user data to update
     * @throws IllegalArgumentException if something is wrong
     */
    public void update(User updatedUser) throws IllegalArgumentException {
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

        userRepository.save(user);
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
