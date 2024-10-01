package com.pecodigos.web_file_manager.users.controllers;

import com.pecodigos.web_file_manager.users.dtos.UserDTO;
import com.pecodigos.web_file_manager.users.entities.User;
import com.pecodigos.web_file_manager.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable(value = "id") UUID id) {
        Optional<User> optionalUser = userService.getUserById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = optionalUser.get();
        UserDTO userDTO = new UserDTO(
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(), // NEED TO CHANGE -> After register implementation remove password from UserDTO, future Pedro
                user.getRole()
        );

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();

        if (userList.isEmpty()) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<UserDTO> userDTOList = userList.stream()
                .map(user -> new UserDTO(
                        user.getName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPassword(), // ALSO NEED TO BE CHANGED -> TODO
                        user.getRole()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(userDTOList);
    }

    @PostMapping("/")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            var user = userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(name = "id") UUID id, @Valid @RequestBody UserDTO userDTO) {
        try {
            var updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "id") UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
