package org.camunda.community;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CamundaConfig.class)
public class ProcessApplication {

  static Logger logger = LoggerFactory.getLogger("ProcessApplication.class");

  public static void main(String[] args) {
    logger.info("Attempting to start Spring Boot Application");
    SpringApplication.run(ProcessApplication.class, args);
    logger.info("Started Spring Boot Application");
  }

}
