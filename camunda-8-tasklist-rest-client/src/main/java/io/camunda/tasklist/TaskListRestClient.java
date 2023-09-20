package io.camunda.tasklist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.tasklist.auth.Authentication;
import io.camunda.tasklist.dto.*;
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

  public HttpResponse<String> post(String endPoint, String body, Class responseClass)
      throws TaskListException, TaskListRestException {
    return postOrPatch("POST", endPoint, body, responseClass);
  }

  public HttpResponse<String> patch(String endPoint, String body, Class responseClass)
      throws TaskListException, TaskListRestException {
    return postOrPatch("PATCH", endPoint, body, responseClass);
  }

  private HttpResponse<String> postOrPatch(String method, String endPoint, String body, Class responseClass)
  throws TaskListException, TaskListRestException {

      if(accessTokenResponse == null) {
        this.authentication.authenticate(this);
      }

      HttpResponse<String> response = doPostOrPatch(method, endPoint, body);

      if(response.statusCode() == 401) {
        this.authentication.authenticate(this);
        response = doPostOrPatch(method, endPoint, body);
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

  private HttpResponse<String> doPostOrPatch(String method, String endPoint, String body) throws TaskListException {
    try {

      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI(endPoint))
          .header("content-type", "application/json")
          .header("Authorization", "Bearer " + accessTokenResponse.getAccess_token())
          .timeout(Duration.ofSeconds(10))
          .method(method, HttpRequest.BodyPublishers.ofString(body))
          .build();

      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    } catch (URISyntaxException e) {
      throw new TaskListException("Endpoint URL must be a valid URI", e);
    } catch (IOException | InterruptedException e) {
      throw new TaskListException("Unable to complete request", e);
    }

  }

  public List<TaskSearchResponse> searchTasks(TaskSearchRequest request) throws TaskListException, TaskListRestException {
    JsonUtils<TaskSearchRequest> jsonRequest = new JsonUtils<>(TaskSearchRequest.class);
    try {

      String body = jsonRequest.toJson(request);
      String endpoint = taskListBaseUrl +  "/v1/tasks/search";
      HttpResponse<String> response = post(endpoint, body, TaskSearchResponse.class);

      // TODO: update JsonUtils if this works
      JsonUtils<List> jsonResponse = new JsonUtils<>(List.class);
      ObjectMapper objectMapper = jsonRequest.getObjectMapper();
      JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, TaskSearchResponse.class);
      return objectMapper.readValue(response.body(), javaType);

      //return jsonResponse.fromJson(response.body());

    } catch (JsonProcessingException e) {
      throw new TaskListException("Unable to parse TaskSearchRequest to json", e);
    }
  }

  public TaskResponse assignTask(String taskId, TaskAssignRequest request) throws TaskListException, TaskListRestException {
    JsonUtils<TaskAssignRequest> jsonRequest = new JsonUtils<>(TaskAssignRequest.class);
    try {

      String body = jsonRequest.toJson(request);
      String endpoint = taskListBaseUrl + "/v1/tasks/"+taskId+"/assign";
      HttpResponse<String> response = patch(endpoint, body, TaskSearchResponse.class);
      JsonUtils<TaskResponse> jsonResponse = new JsonUtils<>(TaskResponse.class);
      return jsonResponse.fromJson(response.body());

    } catch (JsonProcessingException e) {
      throw new TaskListException("Unable to parse TaskSearchRequest to json", e);
    }
  }

}
