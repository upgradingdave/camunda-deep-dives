package org.camunda.community.rest;

import org.camunda.community.model.Person;
import org.camunda.community.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
  private AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping(value = "/users/{username}")
  public Person findUserByUsername(@PathVariable String username) {
    LOGGER.info("findUserByUsername");
    return authService.findUserByUsername(username);
  }

}
