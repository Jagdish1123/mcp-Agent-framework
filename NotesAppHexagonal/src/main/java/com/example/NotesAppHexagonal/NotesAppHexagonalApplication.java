package com.example.NotesAppHexagonal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NotesAppHexagonalApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesAppHexagonalApplication.class, args);
	}
}
