package org.camunda.community.services;

import org.camunda.community.CamundaConfig;
import org.camunda.community.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OperateRestClient {

  static Logger logger = LoggerFactory.getLogger(OperateRestClient.class);

  private final CamundaConfig camundaConfig;
  private final RestClient restClient;
  private TokenResponse tokenResponse;
  private final AuthService authService;

  TaskRepository taskRepository;

  @Autowired
  public OperateRestClient(
      CamundaConfig camundaConfig, TaskRepository taskRepository, AuthService authService) {
    this.camundaConfig = camundaConfig;
    this.restClient = RestClient.builder().build();
    this.taskRepository = taskRepository;
    this.authService = authService;
  }

  public void refreshBearerTokenIfNeeded() {
    if(tokenResponse == null || tokenResponse.isExpired()) {
      refreshBearerToken();
    }
  }

  public void refreshBearerToken() {
    this.tokenResponse = authService.getBearerToken(
        camundaConfig.getClientId(), camundaConfig.getClientSecret(), camundaConfig.getOperateAudience());
  }

  public Integer countActiveInstancesByProcessId(String processId) {

    refreshBearerTokenIfNeeded();

    ParameterizedTypeReference<InstanceSearchResponse> typeRef = new ParameterizedTypeReference<>(){};

    //TODO: clean this up
    String body = "{\n" +
        "    \"filter\": {\n" +
        "        \"bpmnProcessId\": \""+processId+"\",\n" +
        "        \"state\": \"ACTIVE\"\n" +
        "    }\n" +
        "}";

    InstanceSearchResponse response = restClient.post()
        .uri(camundaConfig.getOperateUrl() + "/v1/process-instances/search")
        .header("Content-Type", MediaType.APPLICATION_JSON.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .header("Authorization", String.format("Bearer %s", tokenResponse.getAccessToken()))
        .body(body)
        .retrieve()
        .toEntity(typeRef).getBody();

    return response.getTotal();
  }

}
