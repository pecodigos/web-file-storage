package com.pecodigos.web_file_storage.users.services;

import com.pecodigos.web_file_storage.exceptions.InvalidUserIdException;
import com.pecodigos.web_file_storage.exceptions.UserAlreadyExistsException;
import com.pecodigos.web_file_storage.exceptions.UserNotFoundException;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.dtos.mapper.UserMapper;
import com.pecodigos.web_file_storage.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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

    public UserDTO update(UUID id, UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already taken.");
        }

        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already taken.");
        }

        return userRepository.findById(id)
                .map(data -> {
                    data.setUsername(userDTO.username());
                    data.setEmail(userDTO.email());
                    data.setPassword(passwordEncoder.encode(userDTO.password()));
                    return userMapper.toDto(data);
                }).orElseThrow(() -> new NoSuchElementException("No user found with that ID."));
    }
}
