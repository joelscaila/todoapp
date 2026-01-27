package com.joel.todoapp.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleTodoNotFound_shouldReturnNotFoundResponse() {
        TodoNotFoundException ex = new TodoNotFoundException();

        ResponseEntity<Object> response = handler.handleTodoNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Object bodyObj = response.getBody();
        assertNotNull(bodyObj);

        Map<?, ?> body = assertInstanceOf(Map.class, bodyObj);

        assertEquals("Todo not found", body.get("error"));
        assertEquals(404, body.get("status"));
        assertTrue(body.containsKey("timestamp"));
    }

    @Test
    void handleGeneric_shouldReturnInternalServerError() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<Object> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Object bodyObj = response.getBody();
        assertNotNull(bodyObj);

        Map<?, ?> body = assertInstanceOf(Map.class, bodyObj);

        assertEquals("Internal server error", body.get("error"));
        assertEquals(500, body.get("status"));
        assertTrue(body.containsKey("timestamp"));
    }
}
