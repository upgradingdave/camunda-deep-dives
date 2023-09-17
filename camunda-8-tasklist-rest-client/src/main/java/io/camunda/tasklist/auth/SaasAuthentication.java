package io.camunda.tasklist.auth;

import io.camunda.tasklist.exception.TaskListException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

public class SaasAuthentication implements Authentication {

  String authorizationServerUrl;
  String audience;
  String clusterId;
  String clientId;
  String clientSecret;
  String region;

  public SaasAuthentication(String authorizationServerUrl, String audience, String clusterId, String clientId, String clientSecret, String region) {
    this.authorizationServerUrl = authorizationServerUrl;
    this.audience = audience;
    this.clusterId = clusterId;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.region = region;
  }

  @Override
  public void authenticate() throws TaskListException {

    HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI(authorizationServerUrl))
          .header("content-type", "application/json")
          .timeout(Duration.ofSeconds(10))
          .POST(HttpRequest.BodyPublishers.ofString(""))
          .build();
    } catch (URISyntaxException e) {
      throw new TaskListException("Authorization Server URL must be a valid URI", e);
    }
  }
}
