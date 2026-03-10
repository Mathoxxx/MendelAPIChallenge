# TransactionsAPI

A basic Java 21, Spring Boot 3, and Maven project for the Mendel API Challenge.

## Requirements

- Java 21
- Maven 3.x

## Build and Run

To build the project:
```bash
mvn clean install
```

To run the application:
```bash
mvn spring-boot:run
```

The application will start on port `8080` by default.

## Docker Deployment

You can easily run the application using Docker and Docker Compose. This ensures you don't need Java or Maven installed on your local machine to run the project.

To build and start the container in detached mode:
```bash
docker-compose up -d --build
```

The API will be accessible at `http://localhost:8080`.
You can view the auto-generated Swagger UI documentation at `http://localhost:8080/swagger-ui.html`.

To stop the application:
```bash
docker-compose down
```

## Testing

To run the test suite:
```bash
mvn test
```
