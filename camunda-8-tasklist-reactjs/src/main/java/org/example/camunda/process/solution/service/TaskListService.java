package org.example.camunda.process.solution.service;

import io.camunda.tasklist.TaskListRestClient;
import io.camunda.tasklist.auth.JWTAuthentication;
import io.camunda.tasklist.dto.*;
import io.camunda.tasklist.entities.TaskEntity;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.exception.TaskListRestException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaskListService {

  TaskListRestClient taskListRestClient;

  @Autowired TasklistExtraService tasklistExtraService;

  @Value("${tasklist.client.authorizationUrl:undefined}")
  private String authorizationUrl;

  @Value("${tasklist.client.clientId:undefined}")
  private String clientId;

  @Value("${tasklist.client.clientSecret:undefined}")
  private String clientSecret;

  @Value("${tasklist.client.contentType:undefined}")
  private String contentType;

  @Value("${tasklist.client.taskListBaseUrl:undefined}")
  private String taskListBaseUrl;

  public TaskListService() {}

  public TaskListRestClient getClient() throws TaskListException {

    if (taskListRestClient == null) {

      JWTAuthentication jwtAuthentication =
          new JWTAuthentication(authorizationUrl, clientId, clientSecret, contentType);

      taskListRestClient = new TaskListRestClient(jwtAuthentication, taskListBaseUrl);
    }

    return taskListRestClient;
  }

  public List<TaskSearchResponse> getAssigneeTasks(String userId)
      throws TaskListException, TaskListRestException {
    TaskSearchRequest taskSearchRequest = new TaskSearchRequest();
    taskSearchRequest.setAssignee(userId);
    taskSearchRequest.setState(Constants.TASK_STATE_CREATED);
    return getClient().searchTasks(taskSearchRequest);
  }

  public TaskResponse getTask(String taskId) throws TaskListException, TaskListRestException {
    return getClient().getTask(taskId);
  }

  public FormResponse getForm(String formId, String processDefinitionKey)
      throws TaskListException, TaskListRestException {
    return getClient().getForm(formId, processDefinitionKey);
  }

  public TaskResponse completeTask(String taskId, Map<String, Object> variablesMap)
      throws TaskListException, TaskListRestException {
    return getClient().completeTask(taskId, variablesMap);
  }

  public TaskEntity setDueDate(String taskId, Date dueDate) throws TaskListException {

    OffsetDateTime offsetDateTime = dueDate.toInstant().atOffset(ZoneOffset.UTC);
    return tasklistExtraService.setDueDate(taskId, offsetDateTime);
  }
}
