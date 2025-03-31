
# Dream-Shop

Dream-Shop Backend is a comprehensive e-commerce solution that provides RESTful APIs for managing products, user carts, orders, categories, and user authentication. The application is built with Spring Boot and implements JWT-based authentication for secure access.



## Features
- **User Management**: Registration, authentication, and role-based authorization
- **Product Management**: CRUD operations for products with category and image support
- **Cart System**: Add, update, remove items from cart
- **Order Processing**: Create, track, and manage orders
- **Category Management**: Organize products by categories
- **Image Handling**: Upload and manage product images
- **JWT Authentication**: Secure API endpoints with JSON Web Tokens

 
## Technologies
- **Framework**: Spring Boot
- **Security**: Spring Security with JWT Authentication
- **Database**: MySQL
- **Persistence**: Spring Data JPA + Hibernate
- **Authentication**: JWT (JSON Web Tokens)
- **Dependency Management**: Maven
## Installation


1. Clone the repository:

```bash
 git clone https://github.com/ganeshatmakur/dream-shop.git

cd dream-shop-backend
```
2. Build the project:
```bash
    mvn clean install
```
3. Run the application:
```bash
    mvn spring-boot:run
```

    
## Configuration
1. Configure MySQL database in application.properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dreamshop
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
```
2. Configure JWT properties:
```properties
dreamshop.app.jwtSecret=yourJwtSecretKey
dreamshop.app.jwtExpirationMs=86400000
```
## API Reference



Use this endpoint to authenticate and receive an access token.

## Endpoints

### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/users/{userId}/user` | Get user details |
| POST   | `/users/add` | Create a new user |
| PUT    | `/users/{userId}/update` | Update user information |
| DELETE | `/users/{userId}/delete` | Delete a user |

### `Authenticate `
Authenticate a user and log them into the system.
| Method |Endpoint| Description |
|--------|--------|-------------|
|  POST  |`/auth/login` | To Login the User or Admin |
 

### Product Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/products/all` | List all products |
| GET    | `/products/product/{productId}/product` | Get product by ID |
| POST   | `/products/add` | Add a new product (Admin only) |
| PUT    | `/products/product/{productId}/update` | Update a product (Admin only) |
| DELETE | `/products/product/{productId}` | Delete a product (Admin only) |
| GET    | `/products/product/by/brand-and-name` | Filter products by brand and name |
| GET    | `/products/product/by/category-and-brand` | Filter products by category and brand |
| GET    | `/products/product/{name}/products` | Search products by name |
| GET    | `/products/product/by-brand` | Filter products by brand |
| GET    | `/products/product/{category}/all/products` | Get all products in a category |
| GET    | `/products/product/count/by-brand/and-name` | Count products by brand and name |

### Image Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/images/upload` | Upload a new image |
| GET    | `/images/image/download/{imageId}` | Download an image |
| PUT    | `/images/image/{imageId}/update` | Update an image |
| DELETE | `/images/image{imageId}/delete` | Delete an image |



### Cart Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/carts/{cartId}/my-cart` | Retrieve a specific cart |
| POST   | `/carts/{cartId}/clear` | Remove all items from a cart |
| GET    | `/carts/{cartId}/cart/total-price` | Calculate the total price of a cart |

### Cart Items

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/cartItems/item/add` | Add an item to a cart |
| DELETE | `/cartItems/cart/{cartId}/item/{itemId}/remove` | Remove an item from a cart |
| PUT    | `/cartItems/cart/{cartId}/iteam/{itemId}/update` | Update an item in a cart |
| GET    | `/cartItems/cart/get` | Get all items in a cart |

### Category Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/categories/all` | List all categories |
| POST   | `/categories/add` | Create a new category |
| GET    | `/categories/category/{id}/category` | Get category by ID |
| GET    | `/categories/category/{name}/category` | Get category by name |
| DELETE | `/categories/category/{id}/delete` | Delete a category |
| PUT    | `/categories/category/{id}/update` | Update a category |

### Order Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/orders/order` | Create a new order |
| GET    | `/orders/{orderId}/order` | Get order details by ID |
| GET    | `/orders/{userId}/order` | Get all orders for a user |

## Authorization

Some endpoints require specific roles:

- **Admin Access Required**:
  - `POST /products/add`
  - `PUT /products/product/{productId}/update`
  - `DELETE /products/product/{productId}`





