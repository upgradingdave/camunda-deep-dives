package io.camunda.tasklist;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.camunda.tasklist.auth.JWTAuthentication;
import io.camunda.tasklist.dto.AccessTokenRequest;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.json.JsonUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.*;

public class RestClientTest {
  final static String TASKLIST_BASE_URL="https://jfk-1.tasklist.camunda.io/bccd8692-5e2d-4599-b29e-0988bd5a14a4";
  final static String TASKS_SEARCH_ENDPOINT = TASKLIST_BASE_URL + "/v1/tasks/search";

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

  @Test
  public void propertiesTest() {
    assertTrue(true);
    Properties properties = loadProps("test.sm.properties");
    String clientId = properties.getProperty("clientId");
    assertEquals("tasklist", clientId);
  }

  @Test
  public void jsonTest() throws JsonProcessingException {
    AccessTokenRequest accessTokenRequest = new AccessTokenRequest("xxx", "xxx", "tasklist.camunda.io", "client_credentials");
    String json = JsonUtils.toJson(accessTokenRequest);
    assertNotNull(json);
    assertEquals("{\"client_id\":\"xxx\",\"client_secret\":\"xxx\",\"audience\":\"tasklist.camunda.io\",\"grant_type\":\"client_credentials\"}", json);

    JsonUtils<AccessTokenRequest> jsonUtils = new JsonUtils<>();
    AccessTokenRequest result = jsonUtils.fromJson(json, AccessTokenRequest.class);
    assertNotNull(result);
    assertEquals("xxx", result.getClient_id());
  }

  public void authTest(Properties props) throws TaskListException {
    JWTAuthentication saasAuthentication = new JWTAuthentication(
        props.getProperty("authorizationUrl"),
        props.getProperty("clientId"),
        props.getProperty("clientSecret"),
        props.getProperty("contentType")
    );
    TaskListRestClient taskListRestClient = new TaskListRestClient(saasAuthentication);
    taskListRestClient.authenticate();
    assertNotNull(taskListRestClient.getAccessTokenResponse());
    assertEquals(taskListRestClient.getAccessTokenResponse().getToken_type(), "Bearer");
  }

  @Test
  public void saasAuthTest() throws TaskListException {
    Properties props = loadProps("test.saas.properties");
    authTest(props);
  }

  @Test
  public void smAuthTest() throws TaskListException {
    Properties props = loadProps("test.sm.properties");
    authTest(props);
  }

}
