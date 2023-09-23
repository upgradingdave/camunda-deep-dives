package io.camunda.tasklist;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.camunda.tasklist.auth.JWTAuthentication;
import io.camunda.tasklist.dto.*;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.exception.TaskListRestException;
import io.camunda.tasklist.json.JsonUtils;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.Topology;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

public abstract class TaskListRestApiTests {

  Properties props;
  Long processDefinitionKey;
  ZeebeClient zeebeClient;
  TaskListRestClient taskListRestClient;

  public TaskListRestApiTests() {}

  public Properties loadProps(String propertiesFileName) {
    Properties properties = new Properties();
    InputStream is = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
    try {
      properties.load(is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return properties;
  }

  public void zeebeStatus() {
    Topology topology = zeebeClient.newTopologyRequest().send().join();
    assertTrue(topology.getClusterSize() > 0);
  }

  public void deployProcess() {
    InputStream is = getClass().getClassLoader().getResourceAsStream("bpm/tasklistRestAPIUnitTestProcess.bpmn");
    DeploymentEvent event = zeebeClient.newDeployResourceCommand()
        .addResourceStream(is, "tasklistRestAPIUnitTestProcess.bpmn")
        .send()
        .join();
    processDefinitionKey = event.getProcesses().get(0).getProcessDefinitionKey();
    assertNotNull(processDefinitionKey);
  }

  public void createInstance(Map<String, String> variables) {
    ProcessInstanceEvent event = zeebeClient.newCreateInstanceCommand()
        .processDefinitionKey(processDefinitionKey)
        .variables(variables)
        .send()
        .join();

    Long processInstanceKey = event.getProcessInstanceKey();
    assertNotNull(processInstanceKey);
  }

  public void setup() throws TaskListException, TaskListRestException {
    Map<String, String> variables = new HashMap<>();
    if (findCreatedUnAssignedTasks().size() <= 0) {
      createInstance(variables);
    }
    if (findCreatedAssignedTasks().size() <= 0) {
      variables.put("assignee", "junit");
      createInstance(variables);
    }
  }

  @Test
  public void propertiesTest() {
    assertNotNull(props.getProperty("clientId"));
  }

  @Test
  public void jsonTest() throws JsonProcessingException {
    JsonUtils<AccessTokenRequest> jsonUtils = new JsonUtils<>(AccessTokenRequest.class);
    AccessTokenRequest accessTokenRequest = new AccessTokenRequest("xxx", "xxx", "tasklist.camunda.io", "client_credentials");
    String json = jsonUtils.toJson(accessTokenRequest);
    assertNotNull(json);
    assertEquals("{\"client_id\":\"xxx\",\"client_secret\":\"xxx\",\"audience\":\"tasklist.camunda.io\",\"grant_type\":\"client_credentials\"}", json);

    AccessTokenRequest result = jsonUtils.fromJson(json);
    assertNotNull(result);
    assertEquals("xxx", result.getClient_id());
  }

  @Test
  public void authTest() throws TaskListException {
    taskListRestClient.authenticate();
    assertNotNull(taskListRestClient.getAccessTokenResponse());
    assertEquals(taskListRestClient.getAccessTokenResponse().getToken_type(), "Bearer");
  }

  public List<TaskSearchResponse> findCreatedUnAssignedTasks() throws TaskListException, TaskListRestException {
    TaskSearchRequest taskSearchRequest = new TaskSearchRequest();
    taskSearchRequest.setState(Constants.TASK_STATE_CREATED);
    taskSearchRequest.setAssigned(false);
    taskSearchRequest.setProcessDefinitionKey(processDefinitionKey.toString());
    List<TaskSearchResponse> response = taskListRestClient.searchTasks(taskSearchRequest);
    return response;
  }

  @Test
  public void findCreatedUnAssignedTasksTest() throws TaskListException, TaskListRestException {
    List<TaskSearchResponse> tasks = findCreatedUnAssignedTasks();
    assertTrue(tasks.size() > 0);
  }

  public TaskResponse assignTask(String assignee) throws TaskListException, TaskListRestException {

    String taskId = findCreatedUnAssignedTasks().get(0).getId();

    TaskAssignRequest taskAssignRequest = new TaskAssignRequest();
    taskAssignRequest.setAssignee(assignee);
    taskAssignRequest.setAllowOverrideAssignment(true);
    TaskResponse response = taskListRestClient.assignTask(taskId, taskAssignRequest);
    assertNotNull(response);
    return response;
  }

  @Test
  public void assignTaskTest() throws TaskListException, TaskListRestException {
    TaskResponse response = assignTask("junit");
    assertEquals(response.getAssignee(), "junit");
  }

  public List<TaskSearchResponse> findCreatedAssignedTasks() throws TaskListException, TaskListRestException {
    TaskSearchRequest taskSearchRequest = new TaskSearchRequest();
    taskSearchRequest.setState(Constants.TASK_STATE_CREATED);
    taskSearchRequest.setAssigned(true);
    taskSearchRequest.setAssignee("junit");
    taskSearchRequest.setProcessDefinitionKey(processDefinitionKey.toString());
    List<TaskSearchResponse> response = taskListRestClient.searchTasks(taskSearchRequest);
    return response;
  }

  @Test
  public void findCreatedAssignedTasksTest() throws TaskListException, TaskListRestException {
    List<TaskSearchResponse> tasks = findCreatedAssignedTasks();
    assertTrue(tasks.size() > 0);
  }

  public TaskResponse unassignTask(String taskId) throws TaskListException, TaskListRestException {
    TaskResponse response = taskListRestClient.unassignTask(taskId);
    assertNotNull(response);
    return response;
  }

  @Test
  public void unassignTaskTest() throws TaskListException, TaskListRestException {
    String taskId = findCreatedAssignedTasks().get(0).getId();
    TaskResponse response = unassignTask(taskId);
    assertNull(response.getAssignee());
  }

  /*

  @Test
  public void assignAndUnassignTask() throws TaskListException, TaskListRestException {
    List<TaskSearchResponse> tasks = findCreatedUnAssignedTasks();
    assertTrue(tasks.size() > 0);
    TaskSearchResponse taskSearchResponse = tasks.get(0);
    assertEquals("CREATED", taskSearchResponse.getTaskState());
    assertEquals("2251799813954673", taskSearchResponse.getProcessDefinitionKey());
    assertEquals("User Task Unit Test", taskSearchResponse.getProcessName());

    TaskResponse taskResponse= assignTask(taskSearchResponse.getId(), "dave");

    assertNotNull(taskResponse);
    assertEquals("dave", taskResponse.getAssignee());

    taskResponse= unassignTask(taskSearchResponse.getId());

    assertNotNull(taskResponse);
    assertEquals(null, taskResponse.getAssignee());
  }*/

}
