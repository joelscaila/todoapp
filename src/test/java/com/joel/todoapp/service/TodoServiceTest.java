package com.joel.todoapp.service;


import com.joel.todoapp.model.Todo;
import com.joel.todoapp.model.TodoStatus;
import com.joel.todoapp.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTodo() {
        Todo todo = Todo.builder()
                .title("Comprar leche")
                .status(TodoStatus.PENDING)
                .build();

        when(todoRepository.save(todo)).thenReturn(todo);

        Todo result = todoService.save(todo);

        assertNotNull(result);
        assertEquals("Comprar leche", result.getTitle());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void shouldReturnTodoById() {
        Todo todo = Todo.builder()
                .title("Comprar leche")
                .status(TodoStatus.PENDING)
                .build();
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Todo result = todoService.findById(1L);

        assertNotNull(result);
        assertEquals("Comprar leche", result.getTitle());
        verify(todoRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenTodoNotFound() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> todoService.findById(99L));
        verify(todoRepository).findById(99L);
    }
}
