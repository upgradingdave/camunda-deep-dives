package org.example.camunda.process.solution;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.Topology;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;

public class EntraZeebeClient {
  private static final String gatewayUrl = "<my.domain.name.com>:443";
  private static final String tenantId = "<my-entra-tenant-id>";
  private static final String clientId = "<my-entra-client-id>";
  private static final String authTokenUrl =
      "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";
  private static final String audience = clientId;
  private static final String scope = clientId + "/.default";
  private static final String clientSecret = "<my-entra-client-secret>";

  private OAuthCredentialsProvider credentialsProvider;

  public EntraZeebeClient() {
    this.credentialsProvider =
        new OAuthCredentialsProviderBuilder()
            .authorizationServerUrl(authTokenUrl)
            .audience(audience)
            .scope(scope)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
  }

  public void topologyRequest() {
    try (ZeebeClient client =
        ZeebeClient.newClientBuilder()
            .gatewayAddress(gatewayUrl)
            .credentialsProvider(credentialsProvider)
            .build()) {
      Topology join = client.newTopologyRequest().send().join();
      System.out.println(join);
    }
  }
}
