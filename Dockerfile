# Usa una imagen oficial de OpenJDK (ajusta la versión según tu proyecto)
FROM eclipse-temurin:17-jdk-alpine AS build

# Copia el código fuente y compila con Maven
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Imagen final más ligera
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia el JAR generado desde la fase de build
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
