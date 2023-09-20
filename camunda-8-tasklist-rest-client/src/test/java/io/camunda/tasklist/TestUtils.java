package io.camunda.tasklist;

import io.camunda.tasklist.auth.JWTAuthentication;
import io.camunda.tasklist.dto.*;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.exception.TaskListRestException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestUtils {

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

  public void authTest(Properties props) throws TaskListException {
    JWTAuthentication saasAuthentication = new JWTAuthentication(
        props.getProperty("authorizationUrl"),
        props.getProperty("clientId"),
        props.getProperty("clientSecret"),
        props.getProperty("contentType")
    );
    TaskListRestClient taskListRestClient =
        new TaskListRestClient(saasAuthentication, props.getProperty("taskListBaseUrl"));
    taskListRestClient.authenticate();
    assertNotNull(taskListRestClient.getAccessTokenResponse());
    assertEquals(taskListRestClient.getAccessTokenResponse().getToken_type(), "Bearer");
  }

  public List<TaskSearchResponse> findCreatedUnAssignedTasks(Properties props) throws TaskListException, TaskListRestException {
    JWTAuthentication saasAuthentication = new JWTAuthentication(
        props.getProperty("authorizationUrl"),
        props.getProperty("clientId"),
        props.getProperty("clientSecret"),
        props.getProperty("contentType")
    );

    TaskListRestClient taskListRestClient =
        new TaskListRestClient(saasAuthentication, props.getProperty("taskListBaseUrl"));
    TaskSearchRequest taskSearchRequest = new TaskSearchRequest();
    taskSearchRequest.setState(Constants.TASK_STATE_CREATED);
    taskSearchRequest.setAssigned(false);
    taskSearchRequest.setProcessDefinitionKey(Constants.USER_TASK_UNIT_TEST_PROCESS_KEY);
    List<TaskSearchResponse> response = taskListRestClient.searchTasks(taskSearchRequest);
    assertNotNull(response);
    return response;
  }

  public TaskResponse assignTask(Properties props, String taskId, String assignee) throws TaskListException, TaskListRestException {
    JWTAuthentication saasAuthentication = new JWTAuthentication(
        props.getProperty("authorizationUrl"),
        props.getProperty("clientId"),
        props.getProperty("clientSecret"),
        props.getProperty("contentType")
    );

    TaskListRestClient taskListRestClient =
        new TaskListRestClient(saasAuthentication, props.getProperty("taskListBaseUrl"));
    TaskAssignRequest taskAssignRequest = new TaskAssignRequest();
    taskAssignRequest.setAssignee(assignee);
    taskAssignRequest.setAllowOverrideAssignment(true);
    TaskResponse response = taskListRestClient.assignTask(taskId, taskAssignRequest);
    assertNotNull(response);
    return response;
  }

}
