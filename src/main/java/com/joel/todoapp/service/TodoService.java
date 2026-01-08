package com.joel.todoapp.service;

import com.joel.todoapp.dto.TodoRequest;
import com.joel.todoapp.dto.TodoResponse;
import com.joel.todoapp.mapper.TodoMapper;
import com.joel.todoapp.model.Todo;
import com.joel.todoapp.model.User;
import com.joel.todoapp.repository.TodoRepository;
import com.joel.todoapp.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TodoMapper mapper;

    public TodoService(TodoRepository todoRepository,
                       UserService userService,
                       UserRepository userRepository,
                       TodoMapper mapper) {
        this.todoRepository = todoRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<TodoResponse> findAllForCurrentUser() {
        User user = userService.getCurrentUser();

        return todoRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    public Todo findByIdForCurrentUser(Long id) {
        User user = userService.getCurrentUser();
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        if (!todo.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Unauthorized access");
        }

        return todo;
    }

    public TodoResponse updateForCurrentUser(Long id, TodoRequest request) {
        Todo todo = findByIdForCurrentUser(id);
        todo.setTitle(request.title());
        todo.setStatus(request.status());
        Todo saved = todoRepository.save(todo);
        return mapper.toResponse(saved);
    }

    public void deleteForCurrentUser(Long id) {
        Todo todo = findByIdForCurrentUser(id);
        todoRepository.delete(todo);
    }

    public TodoResponse saveForCurrentUser(TodoRequest request) {
        User user = userService.getCurrentUser();
        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setStatus(request.status());
        todo.setUser(user);
        Todo saved = todoRepository.save(todo);
        return mapper.toResponse(saved);
    }

    public TodoResponse assignToUser(Long userId, TodoRequest request) {
        User targetUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setStatus(request.status());
        todo.setUser(targetUser);
        Todo saved = todoRepository.save(todo);
        return mapper.toResponse(saved);
    }

    public TodoResponse updateAsSupervisor(Long id, TodoRequest request) {

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        todo.setTitle(request.title());
        todo.setStatus(request.status());

        Todo saved = todoRepository.save(todo);
        return mapper.toResponse(saved);
    }

    public void deleteAsSupervisor(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        todoRepository.delete(todo);
    }

    public List<TodoResponse> findAllForUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return todoRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


}
