package com.pecodigos.web_file_manager.users.services;

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

    public User saveUser(UserDTO userDTO) {
        var user = User.builder()
                .name(userDTO.name())
                .username(userDTO.username())
                .email(userDTO.email())
                .password(userDTO.password())
                .role(userDTO.role())
                .build();

        if (user.getRole() == null) {
            user.setRole(Role.COMMON);
        }
        return userRepository.save(user);
    }

    public User updateUser(UUID id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("User not found.");
        }
        var user = optionalUser.get();
        user.setName(userDTO.name());
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());

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
