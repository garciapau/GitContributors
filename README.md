# Git Contributors
Service that returns a list of the top contributors



## Out of the scope / TODO
Environment specific configuration
Service clustering
Authentication (Basic Authentication, OAuth...)
Integration Test
Stress Test
Constants and literals are defined in the same class where they are used



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

Tests are configured using Cucumber based on the Application Features, which can be summarized as:
  Scenario: Retrieve GitHub contributors by city
    Given a Github service API
    When user requests top {50, 100 or 150} contributors of city 'A'
    Then a list of top contributors ia returned sorted by number of repositories (descendant)

Cucumber is addressed to Functional/Acceptance testing, covering the whole application. I've defined it to cover 
the business logic, located in the ```:application``` module, where the features are isolated.
In our case, Cucumber's definition looks like unitary tests when it isn't because:
 - We are leaving both REST ports testing out of its scope: Our own endpoint and the client to external service, which is mocked. 
 - Application logic remains quite simple.

```:infra``` Contains infrastructure application (based on Spring) and a module per each port (HTTP endpoint, 
remote service, persistence, etc.)

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

### Service Reliability/Performance
Since we are consuming a consuming 3rd party service, I need to ensure that it does not affect my service performance.
Therefore I want to decouple my service's performance from Github's availability. 
I'm feeling more concerned about this than about the own service performance. This is why I've decided to focus on 
providing this 3rd party service isolation over adding authentication to GitHub client or asynchrony, parallelism to my service.
Said this, I need a mechanism that will take the most from the concepts of client cache and fallback (from circuit breakers scope).
The idea is, for properly formed requests, the service will always consume GitHub first. If it fails or time outs, then 
the response will be retrieved from the cache, if exists.
There are several caches available but I prefer, for the purpose of this exercise, create my own cache.
Also persisting the data (instead of keeping it in memory) sounds completely advisable, but I will keep it 
out of the scope of this exercise.

## How to build and run the service
The service has an embedded Tomcat server available at por ```8080```.
To test the service, just type:

```./gradlew clean test```

To start the service, just type:

```./gradlew clean bootRun```

## 3rd Party services constraints/SLAs
### Overall Github API Rate limits

For API requests using Basic Authentication or OAuth, you can make up to 5000 requests per hour. Authenticated requests 
are associated with the authenticated user, regardless of whether Basic Authentication or an OAuth token was used. 
This means that all OAuth applications authorized by a user share the same quota of 5000 requests per hour 
when they authenticate with different tokens owned by the same user.

For unauthenticated requests, the rate limit allows for up to 60 requests per hour. 
Unauthenticated requests are associated with the originating IP address, and not the user making requests.
Search API returns maximum 100 results per page

### Specific Rate limit for Github search API

The Github Search API has a custom rate limit. For requests using Basic Authentication, OAuth, or client ID and secret, 
you can make up to 30 requests per minute. For unauthenticated requests, the rate limit allows you to make up to 10 requests per minute.

