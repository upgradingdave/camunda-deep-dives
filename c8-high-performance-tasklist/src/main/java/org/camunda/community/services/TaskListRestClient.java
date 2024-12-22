package org.camunda.community.services;

import org.camunda.community.CamundaConfig;
import org.camunda.community.model.Form;
import org.camunda.community.model.Task;
import org.camunda.community.model.TaskVariable;
import org.camunda.community.model.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TaskListRestClient {

  static Logger logger = LoggerFactory.getLogger(TaskListRestClient.class);

  private final CamundaConfig camundaConfig;
  private final RestClient restClient;
  private TokenResponse tokenResponse;
  private final AuthService authService;

  TaskRepository taskRepository;

  @Autowired
  public TaskListRestClient(
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
        camundaConfig.getClientId(), camundaConfig.getClientSecret(), camundaConfig.getTaskListAudience());
  }

  public List<Task> findTasksByBusinessKey(String businessKey) {

    refreshBearerTokenIfNeeded();

/*    TaskSearchFilter taskSearch = new TaskSearchFilter()
        .state(TaskState.TASK_CREATED)
        .taskVariableFilter("businessKey", "\\\""+businessKey+"\\\"");
    HttpEntity<TaskSearchFilter> body = new HttpEntity<>(taskSearch);
    */

    ParameterizedTypeReference<List<Task>> typeRef = new ParameterizedTypeReference<>(){};

    //TODO: clean this up
    String body = "{\n" +
        "    \"state\": \"CREATED\",\n" +
        "    \"taskVariables\": [\n" +
        "        {\n" +
        "            \"name\": \"businessKey\",\n" +
        "            \"value\": \"\\\""+businessKey+"\\\"\",\n" +
        "            \"operator\": \"eq\"\n" +
        "        }\n" +
        "    ],\n" +
        "    \"includeVariables\": [\n" +
        "        {\n" +
        "            \"name\": \"businessKey\", \n" +
        "            \"alwaysReturnFullValue\": true\n" +
        "        }\n" +
        "    ]\n" +
        "}";

    List<Task> results = restClient.post()
        .uri(camundaConfig.getTasklistUrl() + "/v1/tasks/search")
        .header("Content-Type", MediaType.APPLICATION_JSON.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .header("Authorization", String.format("Bearer %s", tokenResponse.getAccessToken()))
        .body(body)
        .retrieve()
        .toEntity(typeRef).getBody();

    return results;
  }

  public List<Task> findTasksByAssignee(String userName) {

    refreshBearerTokenIfNeeded();

    /*TaskSearchFilter taskSearch = TaskSearchFilter.builder()
        .state(TaskState.TASK_CREATED)
        .assignee(userName)
        .build();

    HttpEntity<TaskSearchFilter> body = new HttpEntity<>(taskSearch);*/

    ParameterizedTypeReference<List<Task>> typeRef = new ParameterizedTypeReference<>(){};

    //TODO: clean this up
    String body = "{\n" +
        "    \"state\": \"CREATED\",\n" +
        "    \"assignee\": \""+userName+"\",\n" +
        "    \"includeVariables\": [\n" +
        "        {\n" +
        "            \"name\": \"businessKey\", \n" +
        "            \"alwaysReturnFullValue\": true\n" +
        "        }\n" +
        "    ]\n" +
        "}";

    List<Task> results = restClient.post()
        .uri(camundaConfig.getTasklistUrl() + "/v1/tasks/search")
        .header("Content-Type", MediaType.APPLICATION_JSON.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .header("Authorization", String.format("Bearer %s", tokenResponse.getAccessToken()))
        .body(body)
        .retrieve()
        .toEntity(typeRef).getBody();

    return results;
  }

  public Task findTaskById(String taskId) {

    refreshBearerTokenIfNeeded();

    ParameterizedTypeReference<Task> typeRef = new ParameterizedTypeReference<>(){};

    Task result = restClient.get()
        .uri(camundaConfig.getTasklistUrl() + "/v1/tasks/" + taskId)
        .header("Content-Type", MediaType.APPLICATION_JSON.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .header("Authorization", String.format("Bearer %s", tokenResponse.getAccessToken()))
        .retrieve()
        .toEntity(typeRef).getBody();

    return result;
  }

  public List<TaskVariable> findTaskVariablesById(String taskId) {

    refreshBearerTokenIfNeeded();

    ParameterizedTypeReference<List<TaskVariable>> typeRef = new ParameterizedTypeReference<>(){};

    List<TaskVariable> result = restClient.post()
        .uri(camundaConfig.getTasklistUrl() + "/v1/tasks/" + taskId + "/variables/search")
        .header("Content-Type", MediaType.APPLICATION_JSON.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .header("Authorization", String.format("Bearer %s", tokenResponse.getAccessToken()))
        .retrieve()
        .toEntity(typeRef).getBody();

    return result;
  }

  public Form findFormById(String formId) {

    refreshBearerTokenIfNeeded();

    ParameterizedTypeReference<Form> typeRef = new ParameterizedTypeReference<>(){};

    Form result = restClient.get()
        .uri(camundaConfig.getTasklistUrl() + "/v1/forms/" + formId)
        .header("Content-Type", MediaType.APPLICATION_JSON.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .header("Authorization", String.format("Bearer %s", tokenResponse.getAccessToken()))
        .retrieve()
        .toEntity(typeRef).getBody();

    return result;
  }

}
