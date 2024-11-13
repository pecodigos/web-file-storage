package com.pecodigos.web_file_storage.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AuthDTO(@NotBlank @NotNull UUID id, @NotBlank @NotNull String username, @NotBlank @NotNull String email, @NotBlank @NotNull String password) {
}
