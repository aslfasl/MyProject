package com.example.project;

import com.example.project.entity.AppUser;
import com.example.project.entity.Role;
import com.example.project.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class Application {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// TODO: 08.03.2022 put it in ConfigClass
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();

	}

	// TODO: 08.03.2022 put it in ConfigClass
	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MANAGER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			userService.saveUser(new AppUser(null, "Nicolas Cage", "NickCage", "qwerty", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "John Travolta", "FaceUp", "123456", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "Jackie Chan", "jc", "4444", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "Arnold Schwarzenegger", "Arny", "55555", new ArrayList<>()));

			userService.addRoleToUser("NickCage", "ROLE_USER");
			userService.addRoleToUser("FaceUp", "ROLE_MANAGER");
			userService.addRoleToUser("jc", "ROLE_ADMIN");
			userService.addRoleToUser("Arny", "ROLE_ADMIN");
			userService.addRoleToUser("Arny", "ROLE_USER");
			userService.addRoleToUser("Arny", "ROLE_SUPER_ADMIN");
		};
	}

}
