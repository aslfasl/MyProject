package com.example.project.configuration;

import com.example.project.converter.Converter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    public ObjectMapper mapper() {
        return JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .findAndAddModules()
                .build();
    }

    @Bean
    public Converter creatorConverter(ObjectMapper objectMapper) {
        Converter converter = new Converter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    public Docket swaggerConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).forCodeGeneration(true)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey())).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.regex("/*/.*"))
                .build()
                .tags(new Tag("Gym Service", "All APIs from project"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Gym management system",
                "Simple CRUD project with a little bit of logic",
                "1.0",
                "Free",
                new springfox.documentation.service.Contact("Andrey Volkov", "url@url.url", "soupec@yandex.ru"),
                "API License",
                "http://license.qq",
                Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey("Token Access", AUTHORIZATION_HEADER, SecurityScheme.In.HEADER.name());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(securityReference()).build();
    }

    private List<SecurityReference> securityReference() {
        AuthorizationScope[] authorizationScope =
                {new AuthorizationScope("Unlimited", "Full api Permission")};
        return Collections.singletonList(new SecurityReference("Token Access", authorizationScope));
    }


}


