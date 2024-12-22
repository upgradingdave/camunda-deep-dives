package org.camunda.community.services;


import org.camunda.community.CamundaConfig;
import org.camunda.community.model.Person;
import org.camunda.community.model.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class AuthService {

  private final CamundaConfig camundaConfig;
  UserRepository userRepository;
  private final RestClient restClient;

  @Autowired
  public AuthService(CamundaConfig camundaConfig, UserRepository userRepository) {
    this.camundaConfig = camundaConfig;
    this.userRepository = userRepository;
    this.restClient = RestClient.builder().build();
  }

  public Person findUserByUsername(String username) {
    return userRepository.findUserByUsername(username);
  }

  public TokenResponse getBearerToken(
      String clientId,
      String clientSecret,
      String audience) {

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "client_credentials");
    params.add("audience", audience);
    params.add("client_id", clientId);
    params.add("client_secret", clientSecret);

    return restClient.post()
        .uri(camundaConfig.getOAuthApi())
        .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .body(params)
        .retrieve()
        .toEntity(TokenResponse.class).getBody();
  }

}
