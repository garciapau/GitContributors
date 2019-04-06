# Git Contributors
Service that returns a list of the top contributors

## Description
Given a city name (e.g. Barcelona) the service returns a list of the top contributors
(sorted by number of repositories) in GitHub
The service should give the possibility to choose between the Top 50, Top 100 or Top
150 contributors

## Design Decisions and assumptions
### Structure
The solution is divided in gradle modules as follows:

```:application``` Contains the business logic (features) and the applications adapters as interfaces. The goal is to keep this module as much decoupled as possible, only using Java 8 standard library.

Tests are configured using Cucumber and based on the Application Features

Dependency Injection is used based on spring configuration classes.

```:infra``` Contains a module per each port (HTTP endpoint, remote service, persistence, etc.)

JUnit and Mockito are used for unitary testing

Inside this module we can find:

```:infra:rest``` The entry point for the service is HTTP based on spring REST.

```:infra:remote``` The implementation of the client for accessing the remote Github service 


## How to build and run the service
The service has an embedded Tomcat server available at por ```8080```.
To test the service, just type:

```./gradlew clean test```

To start the service, just type:

```./gradlew clean bootRun```
