package com.joel.todoapp.service;


import com.joel.todoapp.dto.TodoRequest;
import com.joel.todoapp.dto.TodoResponse;
import com.joel.todoapp.mapper.TodoMapper;
import com.joel.todoapp.model.Todo;
import com.joel.todoapp.model.TodoStatus;
import com.joel.todoapp.model.User;
import com.joel.todoapp.repository.TodoRepository;
import com.joel.todoapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private TodoMapper mapper;

    @InjectMocks
    private TodoService todoService;

    private User user;
    private Todo todo;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("joel");

        todo = new Todo();
        todo.setId(10L);
        todo.setTitle("Test task");
        todo.setStatus(TodoStatus.PENDING);
        todo.setUser(user);
    }

    @Test
    void createForCurrentUser_shouldCreateTodo() {
        TodoRequest request = new TodoRequest("New task", TodoStatus.PENDING);

        when(userService.getCurrentUser()).thenReturn(user);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        when(mapper.toResponse(todo)).thenReturn(new TodoResponse(
                10L, "Test task", TodoStatus.PENDING, null, null
        ));

        TodoResponse response = todoService.saveForCurrentUser(request);

        // Assert
        assertEquals(10L, response.id());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void updateForCurrentUser_shouldUpdateTodo() {
        TodoRequest request = new TodoRequest("Updated", TodoStatus.COMPLETED);

        when(userService.getCurrentUser()).thenReturn(user);
        when(todoRepository.findById(10L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);
        when(mapper.toResponse(todo)).thenReturn(new TodoResponse(
                10L, "Updated", TodoStatus.COMPLETED, null, null
        ));

        TodoResponse response = todoService.updateForCurrentUser(10L, request);

        // Assert
        assertEquals("Updated", response.title());
        assertEquals(TodoStatus.COMPLETED, response.status());
    }

    @Test
    void updateForCurrentUser_shouldThrowWhenTodoNotOwned() {
        User otherUser = new User();
        otherUser.setId(99L);

        todo.setUser(otherUser);

        when(userService.getCurrentUser()).thenReturn(user);
        when(todoRepository.findById(10L)).thenReturn(Optional.of(todo));

        TodoRequest request = new TodoRequest("x", TodoStatus.PENDING);
        // Assert
        assertThrows(AccessDeniedException.class, () ->
                todoService.updateForCurrentUser(10L, request)
        );
    }

    @Test
    void deleteForCurrentUser_shouldDeleteTodo() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(todoRepository.findById(10L)).thenReturn(Optional.of(todo));

        todoService.deleteForCurrentUser(10L);

        verify(todoRepository).delete(todo);
    }

    @Test
    void assignToUser_shouldAssignTodoToTargetUser() {
        User target = new User();
        target.setId(2L);

        TodoRequest request = new TodoRequest("Assigned", TodoStatus.PENDING);

        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        when(mapper.toResponse(todo)).thenReturn(new TodoResponse(
                10L, "Assigned", TodoStatus.PENDING, null, null
        ));

        TodoResponse response = todoService.assignToUser(2L, request);

        // Assert
        assertEquals("Assigned", response.title());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void updateAsSupervisor_shouldUpdateAnyTodo() {
        TodoRequest request = new TodoRequest("Supervisor edit", TodoStatus.COMPLETED);

        when(todoRepository.findById(10L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);
        when(mapper.toResponse(todo)).thenReturn(new TodoResponse(
                10L, "Supervisor edit", TodoStatus.COMPLETED, null, null
        ));

        TodoResponse response = todoService.updateAsSupervisor(10L, request);

        // Assert
        assertEquals("Supervisor edit", response.title());
    }

    @Test
    void findAllForCurrentUser_shouldReturnTodos() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(todoRepository.findAllByUserOrderByCreatedAtDesc(user))
                .thenReturn(List.of(todo));

        TodoResponse mapped = new TodoResponse(
                10L, "Test task", TodoStatus.PENDING, null, null
        );

        when(mapper.toResponse(todo)).thenReturn(mapped);

        List<TodoResponse> result = todoService.findAllForCurrentUser();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test task", result.get(0).title());
    }

    @Test
    void findAllForCurrentUser_shouldReturnEmptyList() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(todoRepository.findAllByUserOrderByCreatedAtDesc(user))
                .thenReturn(List.of());

        List<TodoResponse> result = todoService.findAllForCurrentUser();

        assertTrue(result.isEmpty());
    }


    @Test
    void deleteAsSupervisor_shouldDeleteTodo() {
        when(todoRepository.findById(10L)).thenReturn(Optional.of(todo));

        todoService.deleteAsSupervisor(10L);

        // Assert
        verify(todoRepository).delete(todo);
    }

    @Test
    void deleteAsSupervisor_shouldThrowWhenTodoNotFound() {
        when(todoRepository.findById(10L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(RuntimeException.class, () ->
                todoService.deleteAsSupervisor(10L)
        );
    }

    @Test
    void findAllForUser_shouldReturnTodos() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(todoRepository.findAllByUserOrderByCreatedAtDesc(user))
                .thenReturn(List.of(todo));

        TodoResponse mapped = new TodoResponse(
                10L, "Test task", TodoStatus.PENDING, null, null
        );

        when(mapper.toResponse(todo)).thenReturn(mapped);

        List<TodoResponse> result = todoService.findAllForUser(2L);

        // Assert
        assertEquals(1, result.size());
    }




}
