package org.camunda.community.services;

import org.camunda.community.CamundaConfig;
import org.camunda.community.model.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class ZeebeRestClient {

  static Logger logger = LoggerFactory.getLogger(ZeebeRestClient.class);

  private final CamundaConfig camundaConfig;
  private final RestClient restClient;
  private TokenResponse tokenResponse;
  private final AuthService authService;

  @Autowired
  public ZeebeRestClient(CamundaConfig camundaConfig, AuthService authService) {
    this.camundaConfig = camundaConfig;
    this.restClient = RestClient.builder().build();
    this.authService = authService;
  }

  public void refreshBearerTokenIfNeeded() {
    if(tokenResponse == null || tokenResponse.isExpired()) {
      refreshBearerToken();
    }
  }

  public void refreshBearerToken() {
    this.tokenResponse = authService.getBearerToken(
        camundaConfig.getClientId(), camundaConfig.getClientSecret(), camundaConfig.getZeebeAudience());
  }

  public Boolean completeJob(String jobKey, Map<String, Object> variables) {

    refreshBearerTokenIfNeeded();

    Map<String, Object> body = new HashMap<>();
    variables.put("variables", variables);

    ResponseEntity<String> result = restClient.post()
        .uri(camundaConfig.getZeebeRest() + "/v2/jobs/" + jobKey + "/completion")
        .header("Content-Type", MediaType.APPLICATION_JSON.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .header("Authorization", String.format("Bearer %s", tokenResponse.getAccessToken()))
        .body(body)
        .retrieve()
        .toEntity(String.class);

    return result.getStatusCode().is2xxSuccessful();
  }


}
