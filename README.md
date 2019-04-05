# Git Contributors
Service that returns a list of the top contributors

## Description
Given a city name (e.g. Barcelona) the service returns a list of the top contributors
(sorted by number of repositories) in GitHub
The service should give the possibility to choose between the Top 50, Top 100 or Top
150 contributors

## Design Decisions and assumptions
The business logic is located in the ```application``` module, keeping this module as much decoupled as possible, only using Java 8 standard library.

The entry point for the service is HTTP based on spring REST. This port is isolated in the module ```:infra:rest```.

Dependency Injection is used based on spring configuration classes.

Gradle is used (including the wrapper)

## How to build and run the service
The service has an embedded Tomcat server available at por 8080.
To start the service, just type:
./gradlew clean bootRun
