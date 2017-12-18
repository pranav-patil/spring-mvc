package com.library.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @see <a href="http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api">Setting Up Swagger 2 with a Spring REST API</a>
 * @see <a href="http://springfox.github.io/springfox/docs/snapshot">Springfox Reference Documentation</a>
 * @see <a href="https://g00glen00b.be/documenting-rest-api-swagger-springfox/">Documenting your REST API with Swagger and Springfox</a>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Pranav Patil", "http://emprovisetech.blogspot.com/", "PranavPatil@emprovise.com");
        return new ApiInfoBuilder().title("Spring Services API")
                .description("Spring services API for Weather")
                .contact(contact)
                .termsOfServiceUrl("https://getterms.io/")
                .version("1.0").build();
    }
}
