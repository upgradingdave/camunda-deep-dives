package org.example.camunda.lambda;

import io.camunda.zeebe.client.CredentialsProvider;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import java.util.Map;

public class ZeebeService {

  public static ZeebeClient fromAppConstants() {
    CredentialsProvider cp =
        CredentialsProvider.newCredentialsProviderBuilder()
            .audience(
                String.format(
                    "%s.%s.%s",
                    AppConstants.ZEEBE_CLUSTER_ID,
                    AppConstants.DEFAULT_REGION,
                    AppConstants.BASE_ADDRESS))
            .clientId(AppConstants.ZEEBE_CLIENT_ID)
            .clientSecret(AppConstants.ZEEBE_CLIENT_SECRET)
            .authorizationServerUrl(AppConstants.BASE_AUTH_URL)
            .credentialsCachePath("/tmp/zeebe/cache")
            .build();

    return ZeebeClient.newClientBuilder()
        .credentialsProvider(cp)
        .gatewayAddress(AppConstants.ZEEBE_ADDRESS)
        .build();
  }

  ZeebeClient zeebeClient;

  public ZeebeService(ZeebeClient zeebeClient) {
    this.zeebeClient = zeebeClient;
  }

  public long createInstance(Map<String, Object> variables) {

    String bpmnProcessId = "Process_tripBooking";

    final ProcessInstanceEvent event =
        zeebeClient
            .newCreateInstanceCommand()
            .bpmnProcessId(bpmnProcessId)
            .latestVersion()
            .variables(variables)
            .send()
            .join();

    return event.getProcessDefinitionKey();
  }
}
