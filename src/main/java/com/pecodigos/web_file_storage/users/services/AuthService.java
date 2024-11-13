package com.pecodigos.web_file_storage.users.services;

import com.pecodigos.web_file_storage.auth.JwtUtil;
import com.pecodigos.web_file_storage.exceptions.InvalidUsernameOrPasswordException;
import com.pecodigos.web_file_storage.exceptions.UserAlreadyExistsException;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.dtos.mapper.UserMapper;
import com.pecodigos.web_file_storage.users.entities.User;
import com.pecodigos.web_file_storage.users.enums.Role;
import com.pecodigos.web_file_storage.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDTO login(UserDTO userDTO) {
        var optionalUser = userRepository.findByUsername(userDTO.username());

        if (optionalUser.isEmpty()) {
            throw new InvalidUsernameOrPasswordException();
        }
        var user = optionalUser.get();

        if (!passwordEncoder.matches(userDTO.password(), user.getPassword())) {
            throw new InvalidUsernameOrPasswordException();
        }

        return userMapper.toDTO(user);
    }

    public UserDTO register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already taken.");
        }

        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already taken.");
        }

        var user = User.builder()
                .name(userDTO.name())
                .username(userDTO.username())
                .email(userDTO.email())
                .password(passwordEncoder.encode(userDTO.password()))
                .role(Role.COMMON)
                .build();

        return userMapper.toDTO(userRepository.save(user));
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
