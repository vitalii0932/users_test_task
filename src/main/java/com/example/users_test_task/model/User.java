package com.example.users_test_task.model;

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

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Past(message = "Date of birth should be in the past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    private String address;

    @Pattern(regexp="^(?:.{0}|\\d{10})$", message="Phone number should be empty or contain exactly 5 digits")
    private String phoneNumber;
}
