# Description

Full-stack, database-first web application (VueJS, Spring Boot, Hibernate, MySQL) to learn modern technologies. The app allows to manage courses and supports 4 different roles:
* Admin (can create users).
* Head teacher (can see everything and set the registration period).
* Teacher (can add courses, see who is registered, etc).
* Student (can subscribe to courses).

# Usage

## Backend

```
cd code/backend/target
mvn package
java -jar  target/courses-management-1.0-SNAPSHOT.jar
```

The backend will be available at http://localhost:8081

If you change the port configuration, don't forget to update the Access-Control-Allow-Origin header.

## Frontend

```
cd code/pi-vuejs
npm install
npm run-script build
npm run dev
```

## Basic usage

Log in as "philippe.kartner" with password "bmotdepasse".

# Documentation

UML diagrams are available in the folder "diagrammes".

# Security

* Access control are enforced client-side and server-side.
* Passwords are stored with the bcrypt algorithm in the database.
* Use Java Keystore to store credentials, as opposed to what is done here.
* NO INPUT validation on the frontend: check for XSS if you care.
* SSL can be activated in 10 seconds by updating the application.properties

# Disclaimer

This web app was made for educational purpose, in March 2018, in two weeks, full-time.
