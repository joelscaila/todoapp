# TodoApp – API REST con Spring Boot, JWT y PostgreSQL

Aplicación backend profesional para la gestión de tareas (TODOs), desarrollada con **Spring Boot 3**, **JWT**, **PostgreSQL**, **Docker**, **JPA/Hibernate** y **Swagger/OpenAPI**.  
Incluye autenticación segura, validaciones, tests automatizados y un entorno completamente dockerizado para facilitar la ejecución.

---

## 🚀 Tecnologías principales

- Java 21  
- Spring Boot 3  
- Spring Security + JWT  
- PostgreSQL 15  
- Docker & Docker Compose  
- JPA/Hibernate  
- Swagger / OpenAPI 3  
- JUnit 5 + Mockito  

---

## 🐳 Cómo ejecutar el proyecto

El proyecto está preparado para que cualquier recruiter pueda levantarlo con un solo comando:

```bash
docker compose up -d
```

Esto iniciará:

- La API de Spring Boot  
- La base de datos PostgreSQL  
- La carga automática de datos iniciales (usuario demo + TODOs)

---

## 👤 Usuario demo

Para probar la API sin necesidad de registrarse:

```
username: demo
password: 123456
```

---

## 📘 Documentación Swagger

Una vez levantado el proyecto, accede a:

🔗 http://localhost:8080/swagger-ui.html

### ⚠️ Importante para evitar errores 403

1. Haz login en el endpoint `/api/auth/login`  
2. Copia el token JWT que recibes  
3. Pulsa el botón **“Authorize”** arriba a la derecha en Swagger  
4. Introduce el token:

```
<tu_token>
```

Esto habilita todos los endpoints protegidos.

---

## 📡 Endpoints principales

### 🔐 Autenticación

| Método | Endpoint           | Descripción        |
|--------|--------------------|--------------------|
| POST   | /api/auth/register | Registrar usuario  |
| POST   | /api/auth/login    | Obtener JWT        |

### 📝 TODOs

| Método | Endpoint          | Descripción             |
|--------|-------------------|-------------------------|
| GET    | /api/todos        | Listar todos del usuario |
| POST   | /api/todos        | Crear un TODO           |
| GET    | /api/todos/{id}   | Obtener un TODO         |
| PUT    | /api/todos/{id}   | Actualizar un TODO      |
| DELETE | /api/todos/{id}   | Eliminar un TODO        |

---

## 🏗️ Arquitectura

```
Controller → Service → Repository → PostgreSQL
```

- **Controller**: expone endpoints REST  
- **Service**: lógica de negocio  
- **Repository**: acceso a datos con JPA  
- **Security**: JWT, filtros y configuración  
- **Exception**: manejo global de errores  
- **DTOs**: validaciones y transporte de datos  

---

## 📁 Estructura del proyecto

```
src/main/java/com/joel/todoapp
 ├── config
 ├── controller
 ├── dto
 ├── exception
 ├── mapper
 ├── model
 ├── repository
 ├── security
 └── service
```

---

## 🧪 Tests

El proyecto incluye tests para:

- Servicios  
- Controladores  
- Seguridad  
- Excepciones  
- Mappers  

Ejecutar tests:

```bash
mvn test
```

---

## 🧠 Decisiones técnicas

- JWT para autenticación stateless y escalable  
- Docker Compose para ejecución sin configuración manual  
- PostgreSQL como base de datos realista  
- Perfiles H2/Postgres para desarrollo y testing  
- DTOs + validaciones para integridad de datos  
- GlobalExceptionHandler para respuestas consistentes  
- Swagger para documentación clara y navegable  

---

## 🔮 Próximas mejoras

- Migraciones con Flyway  
- Paginación en endpoints  
- Roles avanzados (admin)  
- CI/CD con GitHub Actions  
- Métricas con Spring Actuator  
- Tests de integración con Testcontainers  
