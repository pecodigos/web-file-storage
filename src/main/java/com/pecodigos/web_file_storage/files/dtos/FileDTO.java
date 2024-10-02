package com.pecodigos.web_file_storage.files.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FileDTO(@NotBlank String name, @NotNull long size, @NotNull String uploadDate) {
}
