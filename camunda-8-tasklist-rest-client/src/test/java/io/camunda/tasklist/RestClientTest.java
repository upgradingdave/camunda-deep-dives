package io.camunda.tasklist;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.camunda.tasklist.auth.SaasAuthentication;
import io.camunda.tasklist.dto.AccessTokenRequest;
import io.camunda.tasklist.dto.AccessTokenResponse;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.json.JsonUtils;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static org.junit.Assert.*;

public class RestClientTest {

  final static String TASKLIST_BASE_URL="https://jfk-1.tasklist.camunda.io/bccd8692-5e2d-4599-b29e-0988bd5a14a4";
  final static String TASKS_SEARCH_ENDPOINT = TASKLIST_BASE_URL + "/v1/tasks/search";

  @Test
  public void sanity() {
    assertTrue(true);
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

  @Test
  public void authTokenTest() throws TaskListException {
    SaasAuthentication saasAuthentication = new SaasAuthentication(
        "https://login.cloud.camunda.io/oauth/token",
        "xxx",
        "xxx"
    );
    TaskListRestClient taskListRestClient = new TaskListRestClient(saasAuthentication);
    taskListRestClient.authenticate();
    assertNotNull(taskListRestClient.getAccessTokenResponse());
    assertEquals(taskListRestClient.getAccessTokenResponse().getToken_type(), "Bearer");
  }
}
