# Performhance API [![Build Status](https://travis-ci.org/dnzprmksz/Performhance-API.svg?branch=master)](https://travis-ci.org/dnzprmksz/Performhance-API)

RESTful API for corporate peer review and performance tracking application.


# Features
Supports multiple organizations, not specific to an Organization/Company.

Different user roles such as Employee, Team Leader and Manager.

Managers alter review criteria for a person, team, job title or a given list of users.

Managers assign employees to a team and select one of them as team leader,
who can read the reviews in that team.

Managers and team leaders can see overall performance of an employee or team
member, respectively.

Employees can review and comment on their colleagues.

Search functionality for Employee and Team.

CRUD operations for Organization, Employee, Team, Job Title, Criteria and
Review.


# Documentation
API documentation is available for Swagger.

Go to http://editor.swagger.io and import the app.json file which is in the following path `Performhance-API/src/main/webapp/specs/`


# Installation
Install JDK and Gradle.

You should run following command to compile the application with Gradle. It will also fetch the missing dependencies.

````
gradle build
````

Start a local MySQL server and create a schema with name Performhance, or edit application.properties file to connect your own database.

JPA Hibernate creates the tables in database, you do not need to design it.

Start the application with following command and it will start to listen requests on port 8080.

````
gradle run
````
