package org.example.camunda.process.solution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProcessApplication {

  Logger logger = LoggerFactory.getLogger(ProcessApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(ProcessApplication.class, args);
  }
}
