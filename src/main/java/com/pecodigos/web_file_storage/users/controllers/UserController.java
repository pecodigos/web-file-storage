package com.pecodigos.web_file_storage.users.controllers;

import com.pecodigos.web_file_storage.users.dtos.LoginDTO;
import com.pecodigos.web_file_storage.users.dtos.RegisterDTO;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.entities.User;
import com.pecodigos.web_file_storage.users.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
                        user.getRole()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(userDTOList);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        try {
            var user = userService.loginUser(loginDTO);

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            return ResponseEntity.ok().body("Login successful!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        try {
            var user = userService.registerUser(registerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(name = "id") UUID id, @Valid @RequestBody RegisterDTO registerDTO) {
        try {
            var updatedUser = userService.updateUser(id, registerDTO);
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
