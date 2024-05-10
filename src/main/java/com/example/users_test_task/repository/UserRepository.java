package com.example.users_test_task.repository;

import com.example.users_test_task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * repository for user entity
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * get user data from db with some email
     *
     * @param email - user email
     * @return optional user
     */
    Optional<User> findUserByEmail(String email);
}
