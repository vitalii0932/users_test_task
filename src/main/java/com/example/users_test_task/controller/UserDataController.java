package com.example.users_test_task.controller;

import com.example.users_test_task.dto.UserDTO;
import com.example.users_test_task.exception.ValidationException;
import com.example.users_test_task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;

/**
 * users data controller
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User data controller", description = "Main controller with the ability to work with user data")
public class UserDataController {

    private final UserService userService;

    /**
     * save user in db
     *
     * @param userDTO - user data from client
     * @return response entity
     */
    @Operation(summary = "Save user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user is successfully saved."),
            @ApiResponse(responseCode = "403", description = "Error saving a user. Possibly incorrect values.")
    })
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.save(userDTO));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getViolations());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * update user entity
     *
     * @param userDTO - new user data from client
     * @return response entity
     */
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user is successfully updated."),
            @ApiResponse(responseCode = "403", description = "Error updating a user. Possibly incorrect values.")
    })
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.update(userDTO));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getViolations());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        }
    }

    /**
     * update some user fields
     *
     * @param fields - new user fields
     * @return response entity
     */
    @Operation(summary = "Update user fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The users fields is successfully updated."),
            @ApiResponse(responseCode = "403", description = "Error updating users fields. Possibly incorrect values.")
    })
    @PostMapping("/update_fields")
    public ResponseEntity<?> updateUserFields(@RequestBody LinkedHashMap<String, Object> fields) {
        try {
            return ResponseEntity.ok(userService.updateFields(fields));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getViolations());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * delete user
     *
     * @param id - user id
     * @return response entity
     */
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user is successfully deleted."),
            @ApiResponse(responseCode = "403", description = "Error deleting a user. Possibly incorrect values.")
    })
    @GetMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam(name = "id") Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok("User with id: " + id + " deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * get users by date of birth from @param from to @param to
     *
     * @param from - from date
     * @param to - to date
     * @return a list of users
     */
    @Operation(summary = "Get a list of users between some dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list of users has been successfully retrieved."),
            @ApiResponse(responseCode = "403", description = "Error when receiving a list of users. The dates may be incorrect.")
    })
    @GetMapping("/get_users_by_dates")
    public ResponseEntity<?> getUsersByDates(
            @RequestParam(name = "from") LocalDate from,
            @RequestParam(name = "to") LocalDate to
            ) {
        try {
            return ResponseEntity.ok(userService.getUsersByDates(from, to));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
