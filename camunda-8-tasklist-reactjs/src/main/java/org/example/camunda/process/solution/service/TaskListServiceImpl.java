package org.example.camunda.process.solution.service;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.auth.SelfManagedAuthentication;
import io.camunda.tasklist.dto.Form;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("tasklist")
@Service
public class TaskListServiceImpl implements TaskListService {

  CamundaTaskListClient client;

  @Value("${tasklist.client.sm.baseUrl}")
  private String selfManagedBaseUrl;
  @Value("${tasklist.client.sm.clientId}")
  private String selfManagedClientId;
  @Value("${tasklist.client.sm.clientSecret}")
  private String selfManagedClientSecret;
  @Value("${tasklist.client.sm.keyCloakRealm}")
  private String selfManagedKeyCloakRealm;

  @Value("${tasklist.client.saas.clientId}")
  private String saasClientId;
  @Value("${tasklist.client.saas.clientSecret}")
  private String saasClientSecret;
  @Value("${tasklist.client.saas.taskListUrl}")
  private String saasTaskListUrl;

  public CamundaTaskListClient getClient() throws TaskListException {

    if (client == null) {

      // Determine if we should connect to Self Managed? Or to SaaS?

      // Connect to Self Managed
      if(selfManagedBaseUrl != null && selfManagedBaseUrl.length() >= 0) {

        SelfManagedAuthentication sma =
            new SelfManagedAuthentication()
                .clientId(selfManagedClientId)
                .clientSecret(selfManagedClientSecret)
                .keycloakUrl(selfManagedBaseUrl)
                .keycloakRealm(selfManagedKeyCloakRealm);

        client =
            new CamundaTaskListClient.Builder()
                .shouldReturnVariables()
                .taskListUrl(selfManagedBaseUrl + "/tasklist")
                .authentication(sma)
                .build();

      } else {
        // Connect to SaaS

        SaasAuthentication sa =
            new SaasAuthentication()
                .clientId(saasClientId)
                .clientSecret(saasClientSecret);

        client =
            new CamundaTaskListClient.Builder()
                .shouldReturnVariables()
                .taskListUrl(saasTaskListUrl)
                .authentication(sa)
                .build();

      }
    }
    return client;
  }

  public List<Task> getAssigneeTasks(String userId) throws TaskListException {
    return getClient().getAssigneeTasks(userId, TaskState.CREATED, 50, true);
  }

  public Task getTask(String taskId) throws TaskListException {
    return getClient().getTask(taskId);
  }

  public Form getFormById(String formId, String processDefinitionId) throws TaskListException {
    return getClient().getForm(formId, processDefinitionId);
  }

  public Form getFormByKey(String formKey, String processDefinitionId) throws TaskListException {
    String formId = parseFormIdFromKey(formKey);
    return getFormById(formId, processDefinitionId);
  }

  static String parseFormIdFromKey(String formKey) {
    Pattern pattern = Pattern.compile("[^:]+:[^:]+:(.*)");
    Matcher matcher = pattern.matcher(formKey);
    while (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  public Task completeTask(String taskId, Map<String, Object> variablesMap)
      throws TaskListException {
    return getClient().completeTask(taskId, variablesMap);
  }
}
