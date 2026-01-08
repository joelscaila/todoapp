package com.joel.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joel.todoapp.dto.TodoRequest;
import com.joel.todoapp.dto.TodoResponse;
import com.joel.todoapp.mapper.TodoMapper;
import com.joel.todoapp.model.Todo;
import com.joel.todoapp.model.TodoStatus;
import com.joel.todoapp.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(TodoControllerTest.TestSecurityConfig.class)
class TodoControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class TestSecurityConfig {
    }


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @MockBean
    private TodoMapper mapper;

    @Test
    @WithMockUser(username = "joel", roles = {"USER"})
    void getAll_shouldReturn200() throws Exception {
        when(todoService.findAllForCurrentUser()).thenReturn(List.of());

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_shouldReturn401_whenNoAuth() throws Exception {
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "joel", roles = {"USER"})
    void createTodo_shouldReturn200() throws Exception {
        TodoRequest request = new TodoRequest("Task", TodoStatus.PENDING);

        when(todoService.saveForCurrentUser(any()))
                .thenReturn(new TodoResponse(1L, "Task", TodoStatus.PENDING, null, null));

        mockMvc.perform(post("/api/todos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "joel", roles = {"USER"})
    void createTodo_shouldReturn400_whenInvalid() throws Exception {
        TodoRequest request = new TodoRequest("", null);

        mockMvc.perform(post("/api/todos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "boss", roles = {"SUPERVISOR"})
    void assignTodo_shouldReturn200_forSupervisor() throws Exception {
        TodoRequest request = new TodoRequest("Task", TodoStatus.PENDING);

        when(todoService.assignToUser(eq(2L), any()))
                .thenReturn(new TodoResponse(1L, "Task", TodoStatus.PENDING, null, null));

        mockMvc.perform(post("/api/todos/assign/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "joel", roles = {"USER"})
    void assignTodo_shouldReturn403_forUser() throws Exception {
        TodoRequest request = new TodoRequest("Task", TodoStatus.PENDING);

        mockMvc.perform(post("/api/todos/assign/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "boss", roles = {"SUPERVISOR"})
    void deleteAsSupervisor_shouldReturn200() throws Exception {

        doNothing().when(todoService).deleteAsSupervisor(10L);

        mockMvc.perform(delete("/api/todos/supervisor/10")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "joel", roles = {"USER"})
    void deleteAsSupervisor_shouldReturn403_forUser() throws Exception {
        mockMvc.perform(delete("/api/todos/supervisor/10")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPERVISOR")
    void getTodosForUser_shouldReturn200() throws Exception {

        when(todoService.findAllForUser(2L)).thenReturn(List.of());

        mockMvc.perform(get("/api/todos/supervisor/user/2"))
                .andExpect(status().isOk());

        verify(todoService).findAllForUser(2L);
    }

    @Test
    @WithMockUser(roles = "SUPERVISOR")
    void updateAsSupervisor_shouldReturn200() throws Exception {

        TodoRequest request = new TodoRequest("Task", TodoStatus.PENDING);

        when(todoService.updateAsSupervisor(eq(10L), any()))
                .thenReturn(new TodoResponse(10L, "Task", TodoStatus.PENDING, null, null));

        mockMvc.perform(put("/api/todos/supervisor/10")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(todoService).updateAsSupervisor(eq(10L), any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void update_shouldReturn200() throws Exception {
        TodoRequest request = new TodoRequest("Task", TodoStatus.PENDING);

        when(todoService.updateForCurrentUser(eq(5L), any()))
                .thenReturn(new TodoResponse(5L, "Task", TodoStatus.PENDING, null, null));

        mockMvc.perform(put("/api/todos/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void delete_shouldReturn200() throws Exception {
        doNothing().when(todoService).deleteForCurrentUser(5L);

        mockMvc.perform(delete("/api/todos/5").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_shouldReturn200() throws Exception {

        Todo todo = new Todo();
        todo.setId(5L);
        todo.setTitle("Task");

        TodoResponse response = new TodoResponse(5L, "Task", TodoStatus.PENDING, null, null);

        when(todoService.findByIdForCurrentUser(5L)).thenReturn(todo);
        when(mapper.toResponse(todo)).thenReturn(response);

        mockMvc.perform(get("/api/todos/5"))
                .andExpect(status().isOk());
    }










}

