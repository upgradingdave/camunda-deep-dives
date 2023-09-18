package io.camunda.tasklist;

import io.camunda.tasklist.auth.JWTAuthentication;
import io.camunda.tasklist.dto.TaskSearchRequest;
import io.camunda.tasklist.dto.TaskSearchResponse;
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

  public void searchTest(Properties props) throws TaskListException, TaskListRestException {
    JWTAuthentication saasAuthentication = new JWTAuthentication(
        props.getProperty("authorizationUrl"),
        props.getProperty("clientId"),
        props.getProperty("clientSecret"),
        props.getProperty("contentType")
    );

    TaskListRestClient taskListRestClient =
        new TaskListRestClient(saasAuthentication, props.getProperty("taskListBaseUrl"));
    TaskSearchRequest taskSearchRequest = new TaskSearchRequest();
    List<TaskSearchResponse> response = taskListRestClient.search(taskSearchRequest);
    assertNotNull(response);
  }

}
