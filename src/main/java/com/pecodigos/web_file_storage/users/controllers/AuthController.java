package com.pecodigos.web_file_storage.users.controllers;

import com.pecodigos.web_file_storage.security.auth.JwtUtil;
import com.pecodigos.web_file_storage.users.dtos.AuthDTO;
import com.pecodigos.web_file_storage.users.dtos.UserDTO;
import com.pecodigos.web_file_storage.users.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Object> update(@RequestBody UserDTO userDTO, Principal principal) {
        if (!authService.isAuthorized(userDTO.id(), principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have access to this account.");
        }
        return ResponseEntity.ok(authService.update(userDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(AuthDTO authDTO, Principal principal) {
        if (!authService.isAuthorized(authDTO.id(), principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have access to this account.");
        }

        authService.delete(authDTO);
        return ResponseEntity.noContent().build();
    }
}
