package com.pecodigos.web_file_storage.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AuthDTO(@NotNull UUID id, @NotBlank @NotNull String username, @NotBlank @NotNull String password) {
}
