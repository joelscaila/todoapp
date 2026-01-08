package com.joel.todoapp.dto;

import com.joel.todoapp.model.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TodoRequest(
        @NotBlank(message = "Title cannot be empty")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String title,

        @NotNull(message = "Status is required")
        TodoStatus status
) {}

