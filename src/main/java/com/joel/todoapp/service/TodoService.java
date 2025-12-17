package com.joel.todoapp.service;

import com.joel.todoapp.model.Todo;
import com.joel.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    public List<Todo> findAll() {
        return repo.findAll();
    }

    public List<Todo> findAllOrdered() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public Todo findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Todo not found"));
    }

    public Todo save(Todo todo) {
        return repo.save(todo);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
