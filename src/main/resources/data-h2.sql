-- ============================
-- DEMO USER (password: 123456)
-- ============================
INSERT INTO users (username, email, role, password, created_at, updated_at)
VALUES (
    'demo',
    'demo@example.com',
    'ROLE_USER',
    '$2a$10$rdXsv5JDZnTS0jxHZMTfle/wERyAKMZdnsJpKdaG6lpBzTrDEKDNe',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);


-- ============================
-- FROM DEMO USER
-- ============================
INSERT INTO todos (title, status, user_id, created_at, updated_at)
VALUES ('Aprender Docker', 'PENDING', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO todos (title, status, user_id, created_at, updated_at)
VALUES ('Preparar portfolio', 'IN_PROGRESS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO todos (title, status, user_id, created_at, updated_at)
VALUES ('Revisar 150 preguntas DSA', 'IN_PROGRESS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO todos (title, status, user_id, created_at, updated_at)
VALUES ('Configurar CI/CD en GitHub Actions', 'IN_PROGRESS', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO todos (title, status, user_id, created_at, updated_at)
VALUES ('Publicar proyecto en LinkedIn', 'COMPLETED', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
