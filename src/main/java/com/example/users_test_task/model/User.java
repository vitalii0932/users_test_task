package com.example.users_test_task.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * user entity class
 */
@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Error. Email is required")
    @Email(message = "Error. Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Error. First name is required")
    private String firstName;

    @NotBlank(message = "Error. Last name is required")
    private String lastName;

    @Past(message = "Error. Date of birth should be in the past")
    private LocalDate dateOfBirth;

    private String address;

    @Pattern(regexp="^(?:.{0}|\\d{10})$", message="Error. Phone number should be empty or contain exactly 10 digits")
    @Nullable
    private String phoneNumber;
}
