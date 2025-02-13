<h1>CoworkingWeb</h1>

<h2>Overview</h2>

CoworkingWeb is a Spring Boot-based web application that allows users to manage coworking space reservations. The application supports two roles: Admin and Customer. It implements JWT-based authentication with Spring Security and uses PostgreSQL for database management.

<h2>Features</h2>

<h3>Admin Capabilities</h3>

Add a new workspace

Remove an existing workspace

View all reservations

Update workspace details

Cancel any reservation

<h3>Customer Capabilities</h3>

Browse available coworking spaces

Make a reservation

View personal reservations

Cancel own reservation

<h2>Tech Stack</h2>
Java (Core features, OOP principles)

Spring Boot - Backend framework

Spring Security (JWT Token) - Authentication and authorization

Spring Data JPA - ORM and database interactions

PostgreSQL - Database management

Docker & Docker Compose - Containerization and deployment

GitHub Actions (maven.yml) - CI/CD pipeline

Mockito - Unit testing

Observer Design Pattern - Used for notifying users when their reservation is canceled or changed

<h2>Setup and Installation</h2>

<h3>1.Prerequisites</h3>

Java 17+

Maven

Docker & Docker Compose

<h3>2.Database Configuration</h3>
Create a PostgreSQL database:

CREATE DATABASE CoworkingSpaceApp;

<h3>3.Running the Application Locally</h3>

 - Clone the repository:

git clone https://github.com/polina3996/CoworkingWeb.git
cd CoworkingWeb

 - Set up the database (PostgreSQL):

docker-compose up -d

mvn clean install
mvn spring-boot:run

<h3>Running with Docker</h3>

 - Build the Docker image:

docker build -t coworking-web .

 - Run the application using Docker Compose:

docker-compose up

<h2>API Endpoints</h2>

<h3>Authentication</h3>

POST /api/auth/login - Login and get JWT token

POST /api/auth/register - Register a new user

<h3>Admin Routes</h3>

GET /api/admin/viewWorkspaces - View all workspaces

POST /api/admin/addWorkspace - Add a new workspace

POST /api/admin/removeWorkspaces - Remove a workspace

GET /api/admin/viewReservations - View all reservations

POST /api/admin/updateWorkspace- Update workspace details

POST /api/admin/removeReservation - Cancel any reservation

<h3>Customer Routes</h3>

GET /api/customer/browseAvailableWorkspaces - Browse available coworking spaces

POST /api/customer/makeAReservation - Make a reservation

GET /api/customer/myReservations - View personal reservations

POST /api/customer/cancelMyReservation - Cancel own reservation

<h2>Testing</h2>

The application includes unit tests using Mockito.
Run tests with:

mvn test

<h2>CI/CD Pipeline</h2>

The project uses GitHub Actions (maven.yml) to automate builds and tests on each push and pull request.

