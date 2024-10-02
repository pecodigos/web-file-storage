package com.pecodigos.web_file_storage.users.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank String username, @NotBlank String password) {
}
