package com.joel.todoapp.dto;

import com.joel.todoapp.model.TodoStatus;

import java.time.LocalDateTime;

public record TodoResponse(
    Long id,
    String title,
    TodoStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
