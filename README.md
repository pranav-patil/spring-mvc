# Spring 5 MVC Showcase
[Spring 5](https://docs.spring.io/spring/docs/current/spring-framework-reference/index.html) MVC Application demonstrates the capabilities of the Spring 5 framework ranging from Spring MVC, Spring JPA, Spring Security.
Also helps in understanding of new techniques such as Thymeleaf templates for UI development, Selma for object mappings, Swagger/SpringFox for API documentation etc.

In this showcase you'll see the following in action:

* Rest Services using @RestController
* Mapping Objects using [Selma](http://www.selma-java.org/)
* Spring JPA using [Hibernate](https://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html/index.html) for H2 SQL database
* Validation
* [Thymeleaf template](http://www.thymeleaf.org) using Spring expression language
* Exception Handling
* [Swagger](https://swagger.io/) documentation using [Spring Fox](http://springfox.github.io/springfox/docs/snapshot)
* [Quartz Job Scheduler](http://www.quartz-scheduler.org/) with database
* [Lombok](https://projectlombok.org/), log definitions with annotations
* Spring Security with [OAuth2](http://projects.spring.io/spring-security-oauth/docs/oauth2.html)

To run the application:
-------------------
From the command line with Git and Maven:

    $ git clone https://github.com/pranav-patil/spring-mvc.git
    $ cd spring-mvc
    $ mvn jetty:run (by default uses 8080]
