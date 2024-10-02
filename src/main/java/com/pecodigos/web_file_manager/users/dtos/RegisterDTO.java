package com.pecodigos.web_file_manager.users.dtos;

import com.pecodigos.web_file_manager.users.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(@NotBlank String name, @NotBlank String username, @NotBlank String email, @NotBlank String password, @NotNull Role role) {
}
