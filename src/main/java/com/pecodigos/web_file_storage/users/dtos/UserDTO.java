package com.pecodigos.web_file_storage.users.dtos;

import com.pecodigos.web_file_storage.users.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(@NotBlank String name, @NotBlank String username, @NotBlank String email, @NotNull Role role) {
}
