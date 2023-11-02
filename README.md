# ShareAll

This project is created for learning and the practical application of acquired knowledge.

## About the Project

ShareAll is a web application that enables users to store and share text-based content. Some of its key features include:

- Randomly generated and unique URL links.
- User registration and authentication.
- Creating and sending posts, including the option to send posts anonymously.
- Authorization for users to delete and edit their own posts.
- Storage of text-based content in local memory.
- Automatic deletion of expired posts.

## Technology Stack

ShareAll is built using the following technologies:

- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- JUnit 5

## App Architecture

![pet](https://github.com/Apolones/shareAll/assets/85924175/c4fdff76-c1ea-465f-ac80-c616084acdf0)

## Setup

To set up and run the project, follow these steps:

1. Install MySQL.
2. Configure the database properties in "application.properties".
3. Build and run the [UrlGenerator](https://github.com/Apolones/UrlGenerator) project.
4. Build the shareAll project using the following command:
```
mvn clean package
```
5. Run the project:
```
java -jar target/shareall-0.0.1.jar
```
6. Access the application in your browser at:
```
http://localhost:8080/posts
```
