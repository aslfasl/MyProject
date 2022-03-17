package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    // FIXED. May be it's not a half exactly right now, hard to count until all todos deleted
    // TODO: 16.03.2022 cleanup mess and all commented code, it remains in VCS use it
    // TODO: 16.03.2022 remove .mvn from repo and add to gitignore
    // TODO: 16.03.2022 reduce sonar issues to a half from 75 currently

}
