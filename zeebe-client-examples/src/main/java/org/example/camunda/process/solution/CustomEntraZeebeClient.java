package org.example.camunda.process.solution;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomEntraZeebeClient {

  private OAuthCredentialsProvider credentialsProvider;
  private ZeebeClient zeebeClient;

  public CustomEntraZeebeClient(
      @Value("${zeebe.client.broker.gatewayAddress:notProvided}") String gatewayAddress,
      @Value("${zeebe.client.cloud.auth-url:notProvided}") String authTokenUrl,
      @Value("${zeebe.client.cloud.client-id:notProvided}") String clientId,
      @Value("${zeebe.client.cloud.client-secret:notProvided}") String clientSecret,
      @Value("${zeebe.client.cloud.scope:notProvided}") String scope) {

    this.credentialsProvider =
        new OAuthCredentialsProviderBuilder()
            .authorizationServerUrl(authTokenUrl)
            .audience(clientId)
            .scope(scope)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();

    this.zeebeClient =
        ZeebeClient.newClientBuilder()
            .gatewayAddress(gatewayAddress)
            .credentialsProvider(credentialsProvider)
            .build();
  }

  public ZeebeClient getZeebeClient() {
    return zeebeClient;
  }
}
