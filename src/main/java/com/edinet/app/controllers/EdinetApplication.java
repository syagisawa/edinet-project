package com.edinet.app.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages= {"com.edinet"})
@EnableJpaRepositories("com.edinet.domain.repositories")
@EntityScan("com.edinet.domain.models")
public class EdinetApplication {

	final Logger logger = LogManager.getLogger(EdinetApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(EdinetApplication.class, args);
	}

}