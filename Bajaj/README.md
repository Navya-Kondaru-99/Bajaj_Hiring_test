# Webhook SQL App

This is a Spring Boot application that:
- Sends a POST request to generate a webhook on startup
- Solves a SQL problem based on the response
- Sends the solution to the webhook URL using a JWT token

## How to Run

1. Make sure you have Java 11+ and Maven installed.
2. In the project root, run:
   ```
   mvn spring-boot:run
   ```
3. The app will execute the required logic on startup.
