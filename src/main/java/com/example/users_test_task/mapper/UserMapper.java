package com.example.users_test_task.mapper;

import com.example.users_test_task.dto.UserDTO;
import com.example.users_test_task.model.User;
import org.mapstruct.Mapper;

/**
 * user entity mapper
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * userDTO class to user class
     *
     * @param userDTO - userDTO class
     * @return user class
     */
    User toUser(UserDTO userDTO);
}
