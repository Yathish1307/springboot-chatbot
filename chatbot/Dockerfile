# Use Maven to build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use a minimal JRE image to run the app
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/chatbot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 