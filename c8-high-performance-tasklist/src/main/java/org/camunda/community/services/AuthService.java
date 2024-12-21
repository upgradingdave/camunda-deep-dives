package org.camunda.community.services;


import org.camunda.community.CamundaConfig;
import org.camunda.community.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private CamundaConfig camundaConfig;
  UserRepository userRepository;

  @Autowired
  public AuthService(CamundaConfig camundaConfig, UserRepository userRepository) {
    this.camundaConfig = camundaConfig;
    this.userRepository = userRepository;
  }

  public Person findUserByUsername(String username) {
    return userRepository.findUserByUsername(username);
  }

}
