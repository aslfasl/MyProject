package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // TODO: 08.03.2022 put it in ConfigClass
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }
// TODO: 16.03.2022 cleanup mess and all commented code, it remains in VCS use it = DONE
    // TODO: 16.03.2022 remove .mvn from repo and add to gitignore = DONE
    // TODO: 16.03.2022 reduce sonar issues to a half from 75 currently


}
