package com.joel.todoapp.mapper;

import com.joel.todoapp.dto.TodoResponse;
import com.joel.todoapp.model.Todo;
import com.joel.todoapp.model.TodoStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoMapperTest {

    private final TodoMapper mapper = new TodoMapper();

    @Test
    void toResponse_shouldMapFieldsCorrectly() {
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = LocalDateTime.now();

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test");
        todo.setStatus(TodoStatus.PENDING);
        todo.setCreatedAt(created);
        todo.setUpdatedAt(updated);

        TodoResponse response = mapper.toResponse(todo);

        assertEquals(1L, response.id());
        assertEquals("Test", response.title());
        assertEquals(TodoStatus.PENDING, response.status());
        assertEquals(created, response.createdAt());
        assertEquals(updated, response.updatedAt());
    }
}
