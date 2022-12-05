package org.example.camunda.process.solution;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
@Deployment(resources = "classpath*:/models/*.*")
public class ProcessApplication {

  Logger logger = LoggerFactory.getLogger(ProcessApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(ProcessApplication.class, args);
  }
}
