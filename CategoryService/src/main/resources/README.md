# E-commerce Backend

A REST API for an e-commerce application, built with Java and Spring Boot. It provides endpoints for authentication, product and category management, carts, addresses, and order placement.

## Features

- User registration and login with JWT authentication
- Category and product CRUD operations, product search, and image uploads
- Shopping cart management
- Address management
- Order placement and payment details
- MySQL persistence, with an H2 development profile
- Redis cache manager configuration
- OpenAPI / Swagger documentation

## Tech stack

- Java 
- Spring Boot
- Spring MVC, Spring Data JPA, and Spring Security
- MySQL or H2
- Redis
- Maven Wrapper

## Getting started

1. Clone the repository and enter the application directory:

   ```bash
   git clone https://github.com/Adi-Gits/E-commerce-Backend.git
   cd E-Commerce-Backend/CategoryService
   ```

2. Create the MySQL database configured by the application (currently `ECOMMERCE_BACKEND`). Provide the database connection values and JWT signing secret through environment variables rather than committing credentials:

   | Environment variable | Purpose |
   | --- | --- |
   | `SPRING_DATASOURCE_URL` | MySQL JDBC URL, e.g. `jdbc:mysql://localhost:3306/ECOMMERCE_BACKEND` |
   | `SPRING_DATASOURCE_USERNAME` | MySQL username |
   | `SPRING_DATASOURCE_PASSWORD` | MySQL password |
   | `SPRING_APP_JWTSECRET` | JWT signing secret |

3. Start Redis if you plan to use caching. The application expects Redis at `localhost:6379` by default.

4. Build and run with the Maven Wrapper, explicitly selecting the MySQL profile:

   **Linux / macOS / WSL**

   ```bash
   ./mvnw clean package
   ./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=MySQL
   ```

   **Windows PowerShell**

   ```powershell
   .\mvnw.cmd clean package
   .\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=MySQL"
   ```

   The service listens on `http://localhost:8080` unless configured otherwise.

### H2 profile

For local development without MySQL, run with the H2 profile instead:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=H2
```

The H2 profile uses an in-memory database, so its data is not retained after the application stops.

Run tests with:

```bash
./mvnw test
```

## API

All endpoints are under `/api`. Interactive API documentation is available at:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

| Area | Method | Endpoint | Purpose |
| --- | --- | --- | --- |
| Authentication | `POST` | `/api/auth/signup` | Register a user |
| Authentication | `POST` | `/api/auth/login` | Authenticate and receive the JWT cookie |
| Authentication | `GET` | `/api/auth/username` | Get the authenticated username |
| Authentication | `GET` | `/api/auth/user` | Get the authenticated user |
| Authentication | `POST` | `/api/auth/signout` | Clear the authentication cookie |
| Categories | `GET` | `/api/public/categories` | List categories (supports pagination and sorting) |
| Categories | `POST` | `/api/admin/categories` | Create a category |
| Categories | `PUT` | `/api/admin/categories/{catId}` | Update a category |
| Categories | `DELETE` | `/api/admin/categories/{catId}` | Delete a category |
| Products | `GET` | `/api/public/products` | List products (supports pagination and sorting) |
| Products | `GET` | `/api/public/categories/{categoryId}/products` | List products in a category |
| Products | `GET` | `/api/public/product/keyword/{keyword}` | Search products by keyword |
| Products | `POST` | `/api/admin/categories/{categoryId}/product` | Add a product to a category |
| Products | `PUT` | `/api/admin/products/{productId}` | Update a product |
| Products | `DELETE` | `/api/admin/products/{productId}` | Delete a product |
| Products | `PUT` | `/api/products/{productId}/image` | Upload or replace a product image (`multipart/form-data`, field `image`) |
| Cart | `POST` | `/api/carts/products/{productId}/quantity/{quantity}` | Add a product to the cart |
| Cart | `GET` | `/api/carts` | List carts |
| Cart | `GET` | `/api/carts/users/cart` | Get the current user's cart |
| Cart | `PUT` | `/api/carts/products/{productId}/quantity/{operation}` | Change a cart item's quantity |
| Cart | `DELETE` | `/api/carts/{cartId}/product/{productId}` | Remove a product from a cart |
| Addresses | `POST` | `/api/addresses` | Create an address |
| Addresses | `GET` | `/api/addresses` | List addresses |
| Addresses | `GET` | `/api/addresses/{addressId}` | Get an address |
| Addresses | `GET` | `/api/users/addresses` | List the current user's addresses |
| Addresses | `PUT` | `/api/addresses/{addressId}` | Update an address |
| Addresses | `DELETE` | `/api/addresses/{addressId}` | Delete an address |
| Orders | `POST` | `/api/order/users/payments/{paymentMethod}` | Place an order |

Paginated category and product endpoints accept `pageNumber`, `pageSize`, `sortBy`, and `sortOrder` query parameters. The category defaults are page `0`, size `50`, sorted by `categoryId` ascending; product listings default to `productId`.

## Caching

The application configures Redis as its Spring Cache backend, using `localhost:6379` by default. Before relying on caching, ensure Redis is running and declare a cache name on each `@Cacheable` method; the category listing currently has `@Cacheable` without a cache name.

## Notes

- Configure database credentials and JWT secrets locally; do not commit real secrets.
- Review the security rules and any development/demo account initialization before deploying this project to a production environment.
