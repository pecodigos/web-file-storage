package com.pecodigos.web_file_storage.users.dtos;

import com.pecodigos.web_file_storage.users.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserDTO(@NotBlank @NotNull UUID id, @NotBlank @NotNull String name, @NotBlank @NotNull String username, @NotBlank @NotNull String email, @NotBlank @NotNull String password, @NotNull Role role) {
}
