package com.pecodigos.web_file_storage.files.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FileDTO(@NotBlank String name, @NotBlank String path, @NotNull long size, @NotNull LocalDate uploadDate) {
}
