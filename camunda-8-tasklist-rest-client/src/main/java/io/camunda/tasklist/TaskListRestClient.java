package io.camunda.tasklist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.tasklist.auth.Authentication;
import io.camunda.tasklist.dto.AccessTokenRequest;
import io.camunda.tasklist.dto.AccessTokenResponse;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.json.JsonUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TaskListRestClient {

  Authentication authentication;
  AccessTokenResponse accessTokenResponse;

  private HttpClient httpClient;

  public TaskListRestClient(Authentication authentication) {
    this.authentication = authentication;
    httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();
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

  public Object post(String endPoint, String body, Class responseClass) throws TaskListException {

    try {

      if(accessTokenResponse == null) {
        this.authentication.authenticate(this);
      }

      HttpResponse<String> response = doPost(endPoint, body);

      if(response.statusCode() == 401) {
        this.authentication.authenticate(this);
        response = doPost(endPoint, body);
      }

      JsonUtils<Object> jsonUtils = new JsonUtils<>();
      return jsonUtils.fromJson(response.body(), responseClass);

    } catch (JsonProcessingException e) {
      throw new TaskListException("Unable to serialize response body to json", e);
    }
  }

  private HttpResponse<String> doPost(String endPoint, String body) throws TaskListException {
    try {

      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI(endPoint))
          .header("content-type", "application/json")
          .header("Authorization", "Bearer" + accessTokenResponse.getAccess_token() )
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
}
