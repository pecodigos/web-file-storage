package com.pecodigos.web_file_storage.users.services;

import com.pecodigos.web_file_storage.exceptions.InvalidUsernameOrPasswordException;
import com.pecodigos.web_file_storage.exceptions.UserAlreadyExistsException;
import com.pecodigos.web_file_storage.users.dtos.AuthDTO;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.dtos.mapper.UserMapper;
import com.pecodigos.web_file_storage.users.entities.User;
import com.pecodigos.web_file_storage.users.enums.Role;
import com.pecodigos.web_file_storage.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private BCryptPasswordEncoder passwordEncoder;

    public AuthDTO login(AuthDTO authDTO) {
        var optionalUser = userRepository.findByUsername(authDTO.username());

        if (optionalUser.isEmpty()) {
            throw new InvalidUsernameOrPasswordException();
        }
        var user = optionalUser.get();

        if (!passwordEncoder.matches(authDTO.password(), user.getPassword())) {
            throw new InvalidUsernameOrPasswordException();
        }

        return userMapper.toAuthDto(user);
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

        return userMapper.toDto(userRepository.save(user));
    }

    public UserDTO update(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already taken.");
        }

        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already taken.");
        }

        return userRepository.findById(userDTO.id())
                .map(data -> {
                    data.setName(userDTO.name());
                    data.setUsername(userDTO.username());
                    data.setEmail(userDTO.email());
                    data.setPassword(passwordEncoder.encode(userDTO.password()));
                    return userMapper.toDto(data);
                }).orElseThrow(() -> new NoSuchElementException("No user found with that ID."));
    }

    public void delete(AuthDTO authDTO) {
        userRepository.deleteById(authDTO.id());
    }

    public boolean isAuthorized(UUID id, String username) {
        return userRepository.findById(id)
                .map(user -> user.getUsername().equals(username))
                .orElse(false);
    }
}
