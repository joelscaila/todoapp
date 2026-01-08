package com.joel.todoapp.repository;

import com.joel.todoapp.model.Todo;
import com.joel.todoapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUserOrderByCreatedAtDesc(User user);
    List<Todo> findByUser(User user);
}
