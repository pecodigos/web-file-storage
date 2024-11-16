package com.pecodigos.web_file_storage.users.controllers;

import com.pecodigos.web_file_storage.auth.JwtUtil;
import com.pecodigos.web_file_storage.users.dtos.AuthDTO;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthDTO authDTO) {
        try {
            var loggedInUser = authService.login(authDTO);
            String token = jwtUtil.generateToken(authDTO.username());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", loggedInUser.id().toString());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.update(userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        authService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
