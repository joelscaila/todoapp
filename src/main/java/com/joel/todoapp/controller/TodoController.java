package com.joel.todoapp.controller;

import com.joel.todoapp.dto.TodoRequest;
import com.joel.todoapp.dto.TodoResponse;
import com.joel.todoapp.mapper.TodoMapper;
import com.joel.todoapp.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService service;
    private final TodoMapper mapper;

    public TodoController(TodoService service, TodoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all todo items for the authenticated user")
    @GetMapping
    public List<TodoResponse> getAll() {
        return service.findAllForCurrentUser();
    }

    @Operation(summary = "Get a todo item by its ID (only if it belongs to the authenticated user)")
    @GetMapping("/{id}")
    public TodoResponse getById(@PathVariable Long id) {
        return mapper.toResponse(service.findByIdForCurrentUser(id));
    }

    @Operation(summary = "Create a new todo item for the authenticated user")
    @PostMapping
    public TodoResponse create(@Valid @RequestBody TodoRequest request) {
        return service.saveForCurrentUser(request);
    }

    @Operation(summary = "Update an existing todo item (only if it belongs to the authenticated user)")
    @PutMapping("/{id}")
    public TodoResponse update(@Valid @PathVariable Long id, @RequestBody TodoRequest request) {
        return service.updateForCurrentUser(id, request);
    }

    @Operation(summary = "Delete a todo item by its ID (only if it belongs to the authenticated user)")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteForCurrentUser(id);
    }

    @Operation(summary = "Assign a task to a user (Supervisor only))")
    @PreAuthorize("hasRole('SUPERVISOR')")
    @PostMapping("/assign/{userId}")
    public ResponseEntity<TodoResponse> assign(
            @Valid
            @PathVariable Long userId,
            @RequestBody TodoRequest request) {

        return ResponseEntity.ok(service.assignToUser(userId, request));
    }

    @Operation(summary = "Update any user's todo item (Supervisor only))")
    @PreAuthorize("hasRole('SUPERVISOR')")
    @PutMapping("/supervisor/{todoId}")
    public TodoResponse updateAsSupervisor(
            @Valid
            @PathVariable Long todoId,
            @RequestBody TodoRequest request
    ) {
        return service.updateAsSupervisor(todoId, request);
    }

    @PreAuthorize("hasRole('SUPERVISOR')")
    @DeleteMapping("/supervisor/{todoId}")
    public void deleteAsSupervisor(@PathVariable Long todoId) {
        service.deleteAsSupervisor(todoId);
    }

    @PreAuthorize("hasRole('SUPERVISOR')")
    @GetMapping("/supervisor/user/{userId}")
    public List<TodoResponse> getTodosForUser(@PathVariable Long userId) {
        return service.findAllForUser(userId);
    }


}
