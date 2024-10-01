package com.pecodigos.web_file_manager.users.controllers;

import com.pecodigos.web_file_manager.users.dtos.UserDTO;
import com.pecodigos.web_file_manager.users.entities.User;
import com.pecodigos.web_file_manager.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
}
