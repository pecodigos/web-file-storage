# Web File Storage <img src="https://media.tenor.com/gHygBs_JkKwAAAAi/moving-boxes.gif" alt="moving boxes gif" width="50"/>


## Overview
This project is a web-based file storage application that enables users to securely upload, retrieve, and delete files. The application features a user-friendly front end and implements JWT (JSON Web Token) authentication for secure access to user files. The backend is built using Spring Boot, while the front end utilizes HTML, CSS, and JavaScript.

![web file storage photo](https://i.imgur.com/QJrwxp3.png)

## Features
- **User Registration**: New users can register and create an account.
- **User Login**: Users can log in to access their files securely using JWT authentication.
- **File Upload**: Users can upload files to their personal storage.
- **File Retrieval**: Users can view and retrieve their uploaded files.
- **File Deletion**: Users can delete files they no longer need.
- **Secure Authentication**: All operations require a valid JWT token for security.
- **Responsive Front End**: The web interface is designed to be user-friendly and responsive.

## Technologies Used
- **Backend**: Spring Boot
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **Frontend**: HTML, CSS, JavaScript
- **Containerization**: Docker
- **Testing**: JUnit, Mockito

## Installation

### Prerequisites
- Java 17 or higher
- Maven
- Docker
- (Database of choice)

### Steps to Install
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <project-directory>
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```
3. Configure application properties in `src/main/resources/application.properties` (if needed).

4. Run the application locally:
   ```bash
   mvn spring-boot:run
   ```

5. (Optional) Build and run with Docker:
    - Build the Docker image:
      ```bash
      docker build -t file-storage-app .
      ```
    - Run the Docker container:
      ```bash
      docker run -p 8080:8080 file-storage-app
      ```

6. Access the application at `http://localhost:8080`.

## API Endpoints
### Authentication
- **POST** `/api/auth/register`: Register a new user.
- **POST** `/api/auth/login`: Log in and receive a JWT token.

### File Operations
- **POST** `/api/files`: Upload a new file (requires JWT).
- **GET** `/api/files`: Retrieve files for the authenticated user (requires JWT).
- **DELETE** `/api/files/{id}`: Delete a file by its ID (requires JWT).

## Frontend Functionalities
- **Home Page**: Information about the application.
- **Registration Page**: Allows users to create a new account.
- **Login Page**: Users can log in to access their files.
- **Storage Page**:
    - Upload files through a simple form.
    - Display a list of uploaded files with options to download or delete each file.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.