package com.example.users_test_task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^(?:.{0}|\\d{10})$", message = "Error. Phone number should be empty or contain exactly 10 digits")
    private String phoneNumber;

    /**
     * copy values from another class
     *
     * @param user - class to copy
     */
    public void copy(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dateOfBirth = user.getDateOfBirth();
        this.address = user.getAddress();
        this.phoneNumber = user.getPhoneNumber();
    }
}
