package com.example.users_test_task.service;

import com.example.users_test_task.model.User;
import com.example.users_test_task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User save(User user) {
        return userRepository.save(user);
    }
}
