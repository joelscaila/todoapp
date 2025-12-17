package com.joel.todoapp.controller;

import com.joel.todoapp.model.Todo;
import com.joel.todoapp.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @Operation(summary = "Get all todo items")
    @GetMapping
    public List<Todo> getAll() {
        return service.findAll();
    }

    @Operation(summary = "Get a todo item by its ID")
    @GetMapping("/{id}")
    public Todo getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Create a new todo item")
    @PostMapping
    public Todo create(@RequestBody Todo todo) {
        return service.save(todo);
    }

    @Operation(summary = "Update an existing todo item")
    @PutMapping("/{id}")
    public Todo update(@PathVariable Long id, @RequestBody Todo todo) {
        todo.setId(id);
        return service.save(todo);
    }

    @Operation(summary = "Delete a todo item by its ID")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
