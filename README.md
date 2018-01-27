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


Oauth2 Grant Type process for the application:
-------------------

* Request to get access token to access user registration service:

        curl -X POST -u register-app:secret http://localhost:8080/web/oauth/token?grant_type=client_credentials&client_id=register-app -H "Accept: application/json"

* `auth-server` provides the JSON response with access token :

        {
            "access_token": "4d5364b6-f2b3-4c4a-ae54-96db71941ce4",
            "token_type": "bearer",
            "expires_in": 43199,
            "scope": "read"
        }

* Call user registration service providing the access_token received earlier as Bearer header parameter, and user details along with credentials to register in POST
  Payload. The `resource-server` responds with success or failure confirmation.

        curl -X POST -H "Authorization: Bearer 4d5364b6-f2b3-4c4a-ae54-96db71941ce4" http://localhost:8080/web/user/register
             -d  '{
                      "username" : "user01",
                      "password" : "secret123",
                      "firstName" : "James",
                      "lastName" : "Bill",
                      "email" : "JRBill@mail.com"
                  }'

* Now using the user credentials for the user registered earlier, request for an access token :

        curl -X POST -u trusted-app:secret http://localhost:8080/web/oauth/token?grant_type=password&username=user01&password=secret123 -H "Accept: application/json"

* `auth-server` returns the JSON response with access token :

        {
            "access_token": "45335e23-153b-41be-93fb-2f56df76b6a7",
            "token_type": "bearer",
            "refresh_token": "65baa098-e39c-4b98-8afc-06710b769319",
            "expires_in": 43199,
            "scope": "read write"
        }

* Access resource with header parameter :

        curl -H "Authorization: Bearer 45335e23-153b-41be-93fb-2f56df76b6a7" http://localhost:8080/web/secure/api/weather

* `resource-server` will give JSON response :

		{
			"success":true,
			"page":"admin",
			"user":"admin"
		}

