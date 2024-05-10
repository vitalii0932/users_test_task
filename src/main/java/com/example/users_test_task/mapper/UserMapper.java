package com.example.users_test_task.mapper;

import com.example.users_test_task.dto.UserDTO;
import com.example.users_test_task.model.User;
import org.mapstruct.Mapper;

/**
 * user entity mapper
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDTO userDTO);
    UserDTO toUSerDTO(User user);
}
