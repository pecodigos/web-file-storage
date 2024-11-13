package com.pecodigos.web_file_storage.users.controllers;

import com.pecodigos.web_file_storage.auth.JwtUtil;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private JwtUtil jwtUtil;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> listUsers() {
        return ResponseEntity.ok(userService.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(name = "id") UUID id, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.update(id, userDTO));
    }
}
