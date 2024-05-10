package com.example.users_test_task.controller;

import com.example.users_test_task.dto.UserDTO;
import com.example.users_test_task.exception.ValidationException;
import com.example.users_test_task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * users data controller
 */
@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
public class UserDataController {

    private final UserService userService;

    /**
     * save user in db
     *
     * @param userDTO - user data from client
     * @return response entity
     */
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.save(userDTO));
        } catch (ValidationException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * update user entity
     *
     * @param userDTO - new user data from client
     * @return response entity
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.update(userDTO));
        } catch (ValidationException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * update some user fields
     *
     * @param fields - new user fields
     * @return response entity
     */
    @PostMapping("/update_fields")
    public ResponseEntity<?> updateUserFields(@RequestBody LinkedHashMap<String, Object> fields) {
        try {
            return ResponseEntity.ok(userService.updateFields(fields));
        } catch (ValidationException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * delete user
     *
     * @param id - user id
     * @return response entity
     */
    @GetMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam(name = "id") Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok("User with id: " + id + " deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
