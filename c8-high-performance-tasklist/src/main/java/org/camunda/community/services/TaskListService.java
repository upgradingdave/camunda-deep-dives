package org.camunda.community.services;


import org.camunda.community.CamundaConfig;
import org.camunda.community.model.Task;
import org.camunda.community.model.TaskSearchFilter;
import org.camunda.community.model.TaskState;
import org.camunda.community.model.TokenResponse;
import org.camunda.community.utils.SimpleCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TaskListService {

  static Logger logger = LoggerFactory.getLogger(TaskListService.class);

  private CamundaConfig camundaConfig;
  private RestClient restClient;
  private TokenResponse tokenResponse;

  TaskRepository taskRepository;

  @Autowired
  public TaskListService(CamundaConfig camundaConfig, TaskRepository taskRepository) {
    this.camundaConfig = camundaConfig;
    this.restClient = RestClient.builder().build();
    this.taskRepository = taskRepository;
  }

  public void refreshBearerTokenIfNeeded() {
    if(tokenResponse == null || tokenResponse.isExpired()) {
      refreshBearerToken();
    }
  }

  public void refreshBearerToken() {
    this.tokenResponse = getBearerToken();
  }

  public TokenResponse getBearerToken() {

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "client_credentials");
    params.add("audience", camundaConfig.getTaskListAudience());
    params.add("client_id", camundaConfig.getClientId());
    params.add("client_secret", camundaConfig.getClientSecret());

    return restClient.post()
        .uri(camundaConfig.getOAuthApi())
        .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .body(params)
        .retrieve()
        .toEntity(TokenResponse.class).getBody();
  }

  public Task saveTaskInDB(Task task) {
    return taskRepository.save(task);
  }

  public void deleteTaskInDBByBusinessKey(String businessKey) {
    taskRepository.deleteTaskByBusinessKey(businessKey);
  }

  public List<Task> findTasksInDBByBusinessKey(String businessKey) {
    return taskRepository.getTasksByBusinessKey(businessKey);
  }

  public List<Task> findTasksInDBByAssignee(String userName) {
    return taskRepository.findTasksByAssignee(userName);
  }

  public List<Task> findTasksByBusinessKey(String businessKey) {

    refreshBearerTokenIfNeeded();

/*    TaskSearchFilter taskSearch = new TaskSearchFilter()
        .state(TaskState.TASK_CREATED)
        .taskVariableFilter("businessKey", "\\\""+businessKey+"\\\"");
    HttpEntity<TaskSearchFilter> body = new HttpEntity<>(taskSearch);
    */

    ParameterizedTypeReference<List<Task>> typeRef = new ParameterizedTypeReference<List<Task>>(){};

    //TODO: clean this up
    String body = "{\n" +
        "    \"state\": \"CREATED\",\n" +
        "    \"taskVariables\": [\n" +
        "        {\n" +
        "            \"name\": \"businessKey\",\n" +
        "            \"value\": \"\\\""+businessKey+"\\\"\",\n" +
        "            \"operator\": \"eq\"\n" +
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

    if(results == null || results.isEmpty()) {
      results = findTasksInDBByBusinessKey(businessKey);
    } else {
      // remove from cache if any exist
      deleteTaskInDBByBusinessKey(businessKey);
    }

    return results;
  }

  public List<Task> findTasksByAssignee(String userName) {

    refreshBearerTokenIfNeeded();

    /*TaskSearchFilter taskSearch = TaskSearchFilter.builder()
        .state(TaskState.TASK_CREATED)
        .assignee(userName)
        .build();

    HttpEntity<TaskSearchFilter> body = new HttpEntity<>(taskSearch);*/

    ParameterizedTypeReference<List<Task>> typeRef = new ParameterizedTypeReference<List<Task>>(){};

    //TODO: clean this up
    String body = "{\n" +
        "    \"state\": \"CREATED\",\n" +
        "    \"assignee\": \""+userName+"\"\n" +
        "}";

    List<Task> results = restClient.post()
        .uri(camundaConfig.getTasklistUrl() + "/v1/tasks/search")
        .header("Content-Type", MediaType.APPLICATION_JSON.toString())
        .header("Accept", MediaType.APPLICATION_JSON.toString())
        .header("Authorization", String.format("Bearer %s", tokenResponse.getAccessToken()))
        .body(body)
        .retrieve()
        .toEntity(typeRef).getBody();

    // Look inside the DB Cache of tasks and add those if we didn't find them from tasklist
    List<Task> cachedTasks = findTasksInDBByAssignee(userName);

    for(Task task : cachedTasks) {
      if(!results.contains(task)) {
        logger.warn(String.format("Task %s not found", task.getId()));
        results.add(task);
      }
    }

    return results;
  }

}
