# Spring Boot Reactive E-Commerce

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-reactive-ecommerce/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-reactive-ecommerce/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-brightgreen?logo=springboot)

A reactive e-commerce application built with Spring Boot 4.1.2, Spring WebFlux, MongoDB, and RabbitMQ. This project
demonstrates the implementation of a fully reactive microservice architecture with real-time messaging capabilities.

## Technologies

- **Java 25** - Programming language (LTS)
- **Spring Boot 4.1.2** - Application framework
- **Spring WebFlux** - Reactive web framework
- **Spring Data MongoDB Reactive** - Reactive MongoDB integration
- **Spring AMQP** - RabbitMQ integration
- **Spring Security 7** - Security and authentication
- **MongoDB** - NoSQL database (5.0.15 via Docker Compose, 8.0 in tests)
- **RabbitMQ** - Message broker (4.x in tests)
- **Lombok** - Boilerplate code reduction
- **Testcontainers 2** - Integration testing with real MongoDB and RabbitMQ
- **Maven 3.10** (via Maven Wrapper) - Build tool

## Features

- Reactive REST API endpoints for item management
- Shopping cart functionality
- User authentication and authorization
- RabbitMQ-based asynchronous item processing
- MongoDB for persistent data storage
- Role-based access control (USER and INVENTORY roles)
- Real-time item search with multiple criteria

## Architecture

The application follows a layered architecture:

- **Controllers**: REST endpoints (HomeController, AmqpItemController)
- **Services**: Business logic (CartService, UserService, AmqpItemService)
- **Repositories**: Data access layer (ItemRepository, CartRepository, UserRepository)
- **Entities**: Domain models (Item, Cart, CartItem, User)
- **Configuration**: Security and message queue setup

## Prerequisites

- Java 25 (JDK)
- Maven 3.9+ (or use the bundled `./mvnw`)
- Docker and Docker Compose (for MongoDB and RabbitMQ, and for running the tests)

## Getting Started

### 1. Start Dependencies

Start MongoDB and RabbitMQ using Docker Compose:

```bash
docker-compose up -d
```

This will start:

- **MongoDB** on port 27017 (credentials: citizix/S3cret)
- **RabbitMQ** on port 5672
- **RabbitMQ Management Console** on port 15672 (http://localhost:15672, guest/guest)

### 2. Build the Application

```bash
./mvnw clean install
```

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on the default Spring Boot port (8080).

## Default Users

The application initializes with two default users:

| Username | Password | Roles                     |
|----------|----------|---------------------------|
| jay      | jay      | ROLE_USER                 |
| admin    | admin    | ROLE_USER, ROLE_INVENTORY |

## API Endpoints

### Public Endpoints

#### Get All Items

```bash
GET /items
```

Returns all available items in the inventory.

#### Search Items

```bash
GET /items/search?name=<name>&price=<price>&useAnd=<true|false>
```

Search for items by name and/or price.

### Authenticated Endpoints

All endpoints below require Basic Authentication.

#### Get User Cart

```bash
GET /cart
Authorization: Basic <credentials>
```

Returns the current user's shopping cart.

#### Add Item to Cart

```bash
POST /cart/add/{itemId}
Authorization: Basic <credentials>
```

Adds an item to the user's cart.

### Admin Endpoints

Requires INVENTORY role.

#### Add New Item via AMQP

```bash
POST /amqp/items/add
Authorization: Basic admin:admin
Content-Type: application/json

{
  "name": "Product Name",
  "price": 29.99
}
```

Adds a new item to the inventory asynchronously via RabbitMQ.

## Configuration

### Application Properties

Configuration is located in `src/main/resources/application.yml`:

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

logging:
  level:
    org.springframework.data.mongodb.core.ReactiveMongoTemplate: DEBUG
```

### MongoDB Configuration

If you need to customize MongoDB connection, add to `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://citizix:S3cret@localhost:27017/ecommerce?authSource=admin
```

## Testing

Tests use [Testcontainers](https://testcontainers.com/) to start MongoDB and RabbitMQ automatically, so a running Docker
daemon is required (no need to start `docker-compose` first). The containers are defined once in
`TestcontainersConfiguration` and wired into Spring Boot via `@ServiceConnection`.

Run unit tests (surefire):

```bash
./mvnw test
```

Run unit and integration tests (`*IT` classes, failsafe):

```bash
./mvnw verify
```

## Continuous Integration

- **GitHub Actions** (`.github/workflows/maven.yml`) builds and tests every push and pull request to `main` on
  Temurin JDK 25 using `./mvnw verify`. Test reports are uploaded when a build fails, and the dependency graph is
  submitted on pushes to `main`.
- **Dependabot** (`.github/dependabot.yml`) checks daily for Maven, GitHub Actions and Docker Compose updates. Updates
  are grouped (Spring Boot, Testcontainers, minor/patch Maven, GitHub Actions) and use conventional commit prefixes.

## Project Structure

```
src/main/java/com/hendisantika/springbootreactiveecommerce/
├── config/
│   ├── MessageConfiguration.java       # RabbitMQ configuration
│   └── SecurityConfiguration.java      # Security configuration
├── constans/
│   └── MessageConstants.java           # Message queue constants
├── controller/
│   ├── AmqpItemController.java         # AMQP endpoints
│   └── HomeController.java             # Main REST endpoints
├── entity/
│   ├── Cart.java                       # Shopping cart entity
│   ├── CartItem.java                   # Cart item entity
│   ├── Item.java                       # Product entity
│   └── User.java                       # User entity with UserDetails
├── loader/
│   ├── CartLoader.java                 # Cart data initialization
│   ├── ItemLoader.java                 # Item data initialization
│   └── UserLoader.java                 # User data initialization
├── repository/
│   ├── CartRepository.java             # Cart repository
│   ├── ItemRepository.java             # Item repository
│   └── UserRepository.java             # User repository
└── service/
    ├── AmqpItemService.java            # AMQP message processing
    ├── CartService.java                # Cart business logic
    └── UserService.java                # User authentication service
```

## Example Usage

### 1. Login and Get Cart

```bash
curl -u jay:jay http://localhost:8080/cart
```

### 2. View All Items

```bash
curl http://localhost:8080/items
```

### 3. Add Item to Cart

```bash
curl -X POST -u jay:jay http://localhost:8080/cart/add/{itemId}
```

### 4. Add New Item (Admin Only)

```bash
curl -X POST -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{"name":"Wireless Mouse","price":19.99}' \
  http://localhost:8080/amqp/items/add
```

## Troubleshooting

### MongoDB Connection Issues

Ensure MongoDB is running:

```bash
docker ps | grep mongo
```

If not running, start it:

```bash
docker-compose up -d mongodb
```

### RabbitMQ Connection Issues

Ensure RabbitMQ is running:

```bash
docker ps | grep rabbit
```

Check RabbitMQ Management Console: http://localhost:15672

### Application Won't Start

1. Verify Java version:
   ```bash
   java -version  # Should be 25
   ```

2. Clean and rebuild:
   ```bash
   ./mvnw clean install
   ```

3. Check if ports 8080, 27017, and 5672 are available

## Development Notes

- The application uses Spring WebFlux for reactive programming
- All database operations are non-blocking using Reactor types (Mono/Flux)
- RabbitMQ is used for asynchronous item processing
- Spring Security provides authentication and role-based authorization
- Testcontainers are used for integration tests
- Lombok and the Spring Boot configuration processor are declared as explicit `annotationProcessorPaths`, because
  JDK 23+ no longer discovers annotation processors on the classpath automatically
- The RabbitMQ items queue is durable, since RabbitMQ 4 rejects transient non-exclusive queues

## License

This project is created for educational purposes.

## Author

- **Hendi Santika**
- Email: hendisantika@gmail.com
- Telegram: @hendisantika34

## Version History

- **0.0.1-SNAPSHOT** - Current development version
    - Spring Boot 4.1.2 (Spring Security 7, Jackson 3)
    - Java 25 support
    - Testcontainers 2 for MongoDB and RabbitMQ in tests
    - GitHub Actions CI on JDK 25 and grouped Dependabot updates
- **Earlier** - Initial development version
    - Spring Boot 3.5.6
    - Java 21 support
    - Reactive MongoDB and WebFlux
    - RabbitMQ integration
    - Security implementation
