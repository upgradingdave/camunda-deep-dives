package io.camunda.tasklist.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.camunda.tasklist.TaskListRestClient;
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

public class SaasAuthentication implements Authentication {

  String authorizationServerUrl;
  String audience = "tasklist.camunda.io";
  String clientId;
  String clientSecret;

  public SaasAuthentication(String authorizationServerUrl, String clientId, String clientSecret) {
    this.authorizationServerUrl = authorizationServerUrl;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  @Override
  public void authenticate(TaskListRestClient taskListClient) throws TaskListException {

    HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    try {
      AccessTokenRequest accessTokenRequest = new AccessTokenRequest(clientId, clientSecret, audience, "client_credentials");
      String body = JsonUtils.toJson(accessTokenRequest);
      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI(authorizationServerUrl))
          .header("content-type", "application/json")
          .timeout(Duration.ofSeconds(10))
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      JsonUtils<AccessTokenResponse> jsonUtils = new JsonUtils<>();
      AccessTokenResponse accessTokenResponse = jsonUtils.fromJson(response.body(), AccessTokenResponse.class);
      taskListClient.setAccessTokenResponse(accessTokenResponse);

    } catch (URISyntaxException e) {
      throw new TaskListException("Authorization Server URL must be a valid URI", e);
    } catch (JsonProcessingException e) {
      throw new TaskListException("Unable to serialize AccessTokenRequest to json", e);
    } catch (IOException | InterruptedException e) {
      throw new TaskListException("Unable to send Access Token Request", e);
    }
  }
}
