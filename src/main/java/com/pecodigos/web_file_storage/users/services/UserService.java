package com.pecodigos.web_file_storage.users.services;

import com.pecodigos.web_file_storage.exceptions.InvalidUserIdException;
import com.pecodigos.web_file_storage.exceptions.UserNotFoundException;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.dtos.mapper.UserMapper;
import com.pecodigos.web_file_storage.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDTO getCurrentUser() {
        return userMapper.toDto(userRepository
                .findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new));
    }

    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(InvalidUserIdException::new);
    }

    public List<UserDTO> list() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}
