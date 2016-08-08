# Performhance API
RESTful API for corporate peer review and performance tracking application.


# Features
Different user roles such as Employee, Team Leader, Manager and Admin.

Employees can review and comment on their colleagues.

Managers can alter review criteria for a person, team or job title.

Managers can assign employees to a team and select one of them as the team leader, who can read the reviews in that team.

CRUD operations for Organizations, Employees, Teams, JobTitles,

Logging incoming requests.


# Installation
Install JDK and Gradle.

Let Gradle fetch the build dependencies.

Start a local MySQL server and create a schema with name Performhance, or edit application.properties file to connect your own database.

JPA Hibernate creates the tables in database, you do not need to design it.

Start the application and it will be ready for requests.