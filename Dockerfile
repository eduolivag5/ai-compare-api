# Etapa 1: Build (Usando imágenes estándar para evitar errores de codificación)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Aprovechamos la caché de Docker para las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código y compilamos
COPY src ./src
# Forzamos UTF-8 durante el build
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

# Etapa 2: Runtime (Ligera pero compatible)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

# Usamos sh -c para que la variable ${PORT} se lea correctamente
# Añadimos los límites de memoria que son vitales en Render
ENTRYPOINT ["sh", "-c", "java -Xmx380m -Xms380m -jar app.jar --server.port=${PORT}"]