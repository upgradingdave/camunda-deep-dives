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
  //final static String TASKLIST_BASE_URL="https://jfk-1.tasklist.camunda.io/bccd8692-5e2d-4599-b29e-0988bd5a14a4";
  //final static String TASKS_SEARCH_ENDPOINT = TASKLIST_BASE_URL + "/v1/tasks/search";

  TestUtils testUtils = new TestUtils();
  @Test
  public void propertiesTest() {
    assertTrue(true);
    Properties properties = testUtils.loadProps("test.sm.properties");
    String clientId = properties.getProperty("clientId");
    assertEquals("tasklist", clientId);
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

}
