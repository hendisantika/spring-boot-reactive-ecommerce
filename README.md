# Spring Boot Reactive E-Commerce

A reactive e-commerce application built with Spring Boot 3.5.6, Spring WebFlux, MongoDB, and RabbitMQ. This project
demonstrates the implementation of a fully reactive microservice architecture with real-time messaging capabilities.

## Technologies

- **Java 21** - Programming language
- **Spring Boot 3.5.6** - Application framework
- **Spring WebFlux** - Reactive web framework
- **Spring Data MongoDB Reactive** - Reactive MongoDB integration
- **Spring AMQP** - RabbitMQ integration
- **Spring Security** - Security and authentication
- **MongoDB 5.0.15** - NoSQL database
- **RabbitMQ** - Message broker
- **Lombok** - Boilerplate code reduction
- **Testcontainers** - Integration testing
- **Maven** - Build tool

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

- Java 21 or higher
- Maven 3.8+
- Docker and Docker Compose (for MongoDB and RabbitMQ)

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

Run unit tests:

```bash
./mvnw test
```

Run integration tests:

```bash
./mvnw verify
```

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
   java -version  # Should be 21 or higher
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

## License

This project is created for educational purposes.

## Author

- **Hendi Santika**
- Email: hendisantika@gmail.com
- Telegram: @hendisantika34

## Version History

- **0.0.1-SNAPSHOT** - Initial development version
    - Spring Boot 3.5.6
    - Java 21 support
    - Reactive MongoDB and WebFlux
    - RabbitMQ integration
    - Security implementation
