package com.sylvamo.ehs_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class EhsRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(EhsRestApplication.class, args);
	}

}
