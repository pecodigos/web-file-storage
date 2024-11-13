package com.pecodigos.web_file_storage.users.dtos.mapper;

import com.pecodigos.web_file_storage.users.dtos.AuthDTO;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);
    AuthDTO toAuthDto(User user);
}
