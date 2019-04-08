# Git Contributors
Service that returns a list of the top contributors

## Description
Given a city name (e.g. Barcelona) the service returns a list of the top contributors
(sorted by number of repositories) in GitHub
The service should give the possibility to choose between the Top 50, Top 100 or Top
150 contributors

## Design Decisions and assumptions
### Structure
Regarding code structure, we can use a single gradle project and base the solution's structure in the Java packages 
or use different gradle modules to encapsulate each component. Both have pros and cons, for instance, 
the former eases finding the classes and the initial understanding of the solution, 
but the later enforces the decoupling between different components (in terms of features and testing) 
while slightly coupling the solution to gradle configurations.

Since I'm currently more satisfied with the later: In this solution I've decided to use gradle modules 
to encapsulate different components, therefore the solution is divided in gradle modules as follows:

```:application``` Contains the business logic (features) and the application adapters as interfaces. 
The goal is to keep this module as much decoupled as possible, only using Java 8 standard library.

Tests are configured using Cucumber and based on the Application Features.

```:infra``` Contains infrastructure application (based on Spring) and a module per each port (HTTP endpoint, remote service, persistence, etc.)

By infrastructure application, I mean the embedded (Tomcat) server managed by Spring Boot. 
I've considered the option of isolating the infrastructure application into a separate module 
but it will make the solution unnecessarily more complex, so I've decided to place these classes in the package
com.acme.git.contributors.infra but inside the module :infra:rest. 

Dependency Injection is used based on spring configuration classes.

JUnit and Mockito are used for unitary testing

Inside infra you can find 2 modules:

```:infra:rest``` The entry point for the service is HTTP based on spring REST.

```:infra:remote``` The implementation of the client for accessing the remote Github service 

### GitHib API chosen
GitHub provides 2 available APIs: 
- REST API v3 (https://developer.github.com/v3/)
- GraphQL API v4 (https://developer.github.com/v4/).

I've decided to use GitHub's REST V3 API, in the scope of this exercise, because I'm more familiar with it 
and I can focus on the solution design and quality of the code.
In a real context, I would also explore GraphQL API v4, because it has clear benefits in terms of flexibility 
and the API looks mature enough, since it was released more than two years ago 
(https://github.blog/2016-09-14-the-github-graphql-api/)   

## How to build and run the service
The service has an embedded Tomcat server available at por ```8080```.
To test the service, just type:

```./gradlew clean test```

To start the service, just type:

```./gradlew clean bootRun```

## 3rd Party services constraints/SLAs
### Overall Github API Rate limits

For API requests using Basic Authentication or OAuth, you can make up to 5000 requests per hour. Authenticated requests are associated with the authenticated user, regardless of whether Basic Authentication or an OAuth token was used. This means that all OAuth applications authorized by a user share the same quota of 5000 requests per hour when they authenticate with different tokens owned by the same user.

For unauthenticated requests, the rate limit allows for up to 60 requests per hour. Unauthenticated requests are associated with the originating IP address, and not the user making requests.

### Specific Rate limit for Github search API

The Github Search API has a custom rate limit. For requests using Basic Authentication, OAuth, or client ID and secret, you can make up to 30 requests per minute. For unauthenticated requests, the rate limit allows you to make up to 10 requests per minute.
