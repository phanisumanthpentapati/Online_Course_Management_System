package com.example.onilecms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnilecmsApplication {

	public static void main(String[] args) {

		System.out.println(
				"SPRING_DATASOURCE_URL = "
						+ System.getenv("SPRING_DATASOURCE_URL")
		);

		SpringApplication.run(OnilecmsApplication.class, args);
	}
}