package com.pecodigos.web_file_manager.users.services;

import com.pecodigos.web_file_manager.users.dtos.RegisterDTO;
import com.pecodigos.web_file_manager.users.dtos.UserDTO;
import com.pecodigos.web_file_manager.users.entities.User;
import com.pecodigos.web_file_manager.users.enums.Role;
import com.pecodigos.web_file_manager.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(RegisterDTO registerDTO) {
        var user = User.builder()
                .name(registerDTO.name())
                .username(registerDTO.username())
                .email(registerDTO.email())
                .password(registerDTO.password())
                .role(registerDTO.role())
                .build();

        if (user.getRole() == null) {
            user.setRole(Role.COMMON);
        }
        return userRepository.save(user);
    }

    public User updateUser(UUID id, RegisterDTO registerDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("User not found.");
        }
        var user = optionalUser.get();
        user.setName(registerDTO.name());
        user.setUsername(registerDTO.username());
        user.setEmail(registerDTO.email());
        user.setPassword(registerDTO.password());
        user.setRole(registerDTO.role());

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("User not found.");
        }
        userRepository.delete(optionalUser.get());
    }
}
