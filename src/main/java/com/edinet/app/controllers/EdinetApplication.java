package com.edinet.app.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EdinetApplication {

	final Logger logger = LogManager.getLogger(EdinetApplication.class.getName());

  public static void main(String[] args) {
    SpringApplication.run(EdinetApplication.class, args);
  }

}