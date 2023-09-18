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
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JWTAuthentication implements Authentication {

  final public static String CLIENT_CREDENTIALS = "client_credentials";

  String authorizationServerUrl;
  String audience = "tasklist.camunda.io";
  String clientId;
  String clientSecret;

  String contentType;

  public JWTAuthentication(String authorizationServerUrl, String clientId, String clientSecret, String contentType) {
    this.authorizationServerUrl = authorizationServerUrl;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.contentType = contentType;
  }

  @Override
  public void authenticate(TaskListRestClient taskListClient) throws TaskListException {

    HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    try {

      String body = null;
      if(contentType.equals("application/json")) {

        AccessTokenRequest accessTokenRequest =
            new AccessTokenRequest(clientId, clientSecret, audience, CLIENT_CREDENTIALS);
        body = JsonUtils.toJson(accessTokenRequest);

      } else if(contentType.equals("application/x-www-form-urlencoded")) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientId);
        parameters.put("client_secret", clientSecret);
        parameters.put("audience", audience);
        parameters.put("grant_type", CLIENT_CREDENTIALS);

        body = parameters.entrySet()
            .stream()
            .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));

      } else {
        throw new TaskListException("Content type must either be `json` or `x-www-form-urlencoded`");
      }

      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI(authorizationServerUrl))
          .header("content-type", contentType)
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
