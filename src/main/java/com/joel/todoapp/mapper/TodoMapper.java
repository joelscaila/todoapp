package com.joel.todoapp.mapper;

import com.joel.todoapp.dto.TodoResponse;
import com.joel.todoapp.model.Todo;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {

    public TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getStatus(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }
}
