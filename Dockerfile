# --- Etapa 1: Build con Maven ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiamos solo el pom.xml primero para descargar las dependencias.
# Esto hace que si cambias el código pero no las librerías,
# Docker use la caché y no tenga que descargar todo de nuevo.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ahora copiamos el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Runtime con Java 21 ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Render asigna el puerto dinámicamente en la variable $PORT
ENV PORT=8080
EXPOSE 8080

# Límites de memoria para evitar que el plan gratuito de Render mate el proceso
ENTRYPOINT ["java", "-Xmx380m", "-Xms380m", "-jar", "app.jar", "--server.port=${PORT}"]