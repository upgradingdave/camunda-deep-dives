package io.camunda.tasklist;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.camunda.tasklist.auth.Authentication;
import io.camunda.tasklist.dto.AccessTokenResponse;
import io.camunda.tasklist.dto.ErrorResponse;
import io.camunda.tasklist.dto.TaskSearchRequest;
import io.camunda.tasklist.dto.TaskSearchResponse;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.exception.TaskListRestException;
import io.camunda.tasklist.json.JsonUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TaskListRestClient {

  final static String TASKS_SEARCH_ENDPOINT = "/v1/tasks/search";

  Authentication authentication;
  AccessTokenResponse accessTokenResponse;

  String taskListBaseUrl;

  private final HttpClient httpClient;

  public TaskListRestClient(Authentication authentication, String taskListBaseUrl) {
    this.authentication = authentication;
    httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    this.taskListBaseUrl = taskListBaseUrl;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public void setAuthentication(Authentication authentication) {
    this.authentication = authentication;
  }

  public AccessTokenResponse getAccessTokenResponse() {
    return accessTokenResponse;
  }

  public void setAccessTokenResponse(AccessTokenResponse accessTokenResponse) {
    this.accessTokenResponse = accessTokenResponse;
  }

  public void authenticate() throws TaskListException {
    this.authentication.authenticate(this);
  }

  public HttpResponse<String> post(String endPoint, String body, Class responseClass) throws TaskListException, TaskListRestException {

      if(accessTokenResponse == null) {
        this.authentication.authenticate(this);
      }

      HttpResponse<String> response = doPost(endPoint, body);

      if(response.statusCode() == 401) {
        this.authentication.authenticate(this);
        response = doPost(endPoint, body);
      }

      if(response.statusCode() == 200) {
        return response;
      } else if (response.statusCode() == 400) {

        JsonUtils<ErrorResponse> jsonUtils = new JsonUtils<>(ErrorResponse.class);
        try {
          ErrorResponse errorResponse = jsonUtils.fromJson(response.body());
          throw new TaskListRestException(errorResponse);
        } catch (JsonProcessingException e) {
          throw new TaskListException("Unable to parse error response", e);
        }

      } else {
        throw new TaskListException("Unexpected response from post");
      }
  }

  private HttpResponse<String> doPost(String endPoint, String body) throws TaskListException {
    try {

      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI(endPoint))
          .header("content-type", "application/json")
          .header("Authorization", "Bearer " + accessTokenResponse.getAccess_token() )
          .timeout(Duration.ofSeconds(10))
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    } catch (URISyntaxException e) {
      throw new TaskListException("Endpoint URL must be a valid URI", e);
    } catch (IOException | InterruptedException e) {
      throw new TaskListException("Unable to complete request", e);
    }

  }

  public List<TaskSearchResponse> search(TaskSearchRequest request) throws TaskListException, TaskListRestException {
    JsonUtils<TaskSearchRequest> jsonRequest = new JsonUtils<>(TaskSearchRequest.class);
    try {

      String body = jsonRequest.toJson(request);
      String endpoint = taskListBaseUrl + TASKS_SEARCH_ENDPOINT;
      HttpResponse<String> response = post(endpoint, body, TaskSearchResponse.class);
      JsonUtils<List> jsonResponse = new JsonUtils<>(List.class);
      return jsonResponse.fromJson(response.body());

    } catch (JsonProcessingException e) {
      throw new TaskListException("Unable to parse TaskSearchRequest to json", e);
    }
  }

}
