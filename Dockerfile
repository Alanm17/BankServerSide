# ---------------------------
# Build stage
# ---------------------------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Maven wrapper and give execution permission BEFORE full copy
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Copy the rest of the source code
COPY . .

# Build application
RUN ./mvnw clean package -DskipTests

# ---------------------------
# Run stage
# ---------------------------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
