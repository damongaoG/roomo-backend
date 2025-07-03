# Roomo Backend

A Spring Boot-based REST API backend for the Roomo property listing platform.

## Prerequisites

Before running this project, ensure you have the following installed:

- **Java 17 or higher** (Java 17, 21, or 24 recommended)
- **Maven 3.8.x or higher**
- **PostgreSQL** (or access to a PostgreSQL database like Supabase)

## Technology Stack

- Spring Boot 3.2.0
- Spring Security with JWT authentication
- Spring Data JPA
- PostgreSQL database
- Lombok for reducing boilerplate code
- Maven for dependency management

## Project Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd roomo-backend
```

### 2. Configure Java Version

Check your Java version:
```bash
java -version
```

If you need to switch Java versions on macOS:
```bash
# List available Java versions
/usr/libexec/java_home -V

# Set JAVA_HOME to a specific version (example for Java 17)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH
```

For a permanent solution, add the above export commands to your `~/.zshrc` or `~/.bash_profile`.

### 3. Database Configuration

The application is configured to connect to a PostgreSQL database. Update the database configuration in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-database-host:5432/your-database-name
    username: your-username
    password: your-password
```

**Note**: The current configuration connects to a Supabase PostgreSQL instance. Make sure to update these credentials for your environment.

### 4. Build the Project

```bash
mvn clean compile
```

If you encounter Lombok-related compilation errors with newer Java versions (e.g., Java 24), the project is already configured with the latest Lombok version (1.18.38) to ensure compatibility.

## Running the Application

### Using Maven Spring Boot Plugin

```bash
mvn spring-boot:run
```

### Using Java JAR

First, build the JAR file:
```bash
mvn clean package
```

Then run:
```bash
java -jar target/roomo-backend-1.0-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
  - Request body: `{ "username": "string", "password": "string" }`
  - Response: `{ "token": "jwt-token" }`

### Listings (Requires Authentication)
All listing endpoints require a valid JWT token in the Authorization header: `Authorization: Bearer <token>`

- `POST /api/listings` - Create a new listing (Requires LISTER role)
- `GET /api/listings/{id}` - Get a specific listing
- `PUT /api/listings/{id}` - Update a listing (Requires LISTER role)

## Security Configuration

The application uses JWT tokens for authentication. Features include:
- Stateless session management
- Role-based access control (LISTER role for property management)
- CORS configuration for cross-origin requests
- BCrypt password encoding

## Troubleshooting

### Java Version Issues

If you encounter compilation errors related to Java version:

1. Ensure you're using Java 17 or higher
2. For Java 24 compatibility issues, verify Lombok version is 1.18.38 or higher in `pom.xml`

### Lombok Compilation Errors

The project uses Lombok annotations. If you see errors like "cannot find symbol" for getters/setters:

1. Ensure your IDE has Lombok plugin installed
2. Enable annotation processing in your IDE
3. The Maven configuration already includes the necessary annotation processor

### Database Connection Issues

If the application fails to connect to the database:

1. Verify PostgreSQL is running and accessible
2. Check database credentials in `application.yml`
3. Ensure the database exists and user has proper permissions

### Port Already in Use

If port 8080 is already in use, you can change it in `application.yml`:
```yaml
server:
  port: 8081
```

## Development Tips

- The application uses Spring profiles. Create `application-dev.yml` for local development settings
- Logging level is set to DEBUG for `com.roomo` package - adjust in `application.yml` as needed
- JWT secret should be changed for production environments

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

[Add your license information here] 