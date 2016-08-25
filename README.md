# Performhance API
RESTful API for corporate peer review and performance tracking application.


# Features
Supports multiple Organizations.

Different user roles such as Employee, Team Leader and Manager.

Managers can alter review criteria for a person, team, job title or a given list of users.

Managers can assign employees to a team and select one of them as the team leader, who can read the reviews in that team.

Employees can review and comment on their colleagues.

Search functionality for Employees and Teams.

CRUD operations for Organizations, Employees, Teams, Job Titles, Criteria, Reviews.


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
