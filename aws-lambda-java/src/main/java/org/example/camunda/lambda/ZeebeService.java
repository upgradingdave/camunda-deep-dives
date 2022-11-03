package org.example.camunda.lambda;

import io.camunda.zeebe.client.CredentialsProvider;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import java.util.HashMap;
import java.util.Map;

public class ZeebeService {

  public String createInstance() {
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
            .credentialsCachePath("/tmp/zeebeCache")
            .build();

    ZeebeClient zeebeClient =
        ZeebeClient.newClientBuilder()
            .credentialsProvider(cp)
            .gatewayAddress(AppConstants.ZEEBE_ADDRESS)
            .build();

    String bpmnProcessId = "Process_tripBooking";
    Map<String, String> variables = new HashMap<>();
    variables.put("name", "dave");

    final ProcessInstanceEvent event =
        zeebeClient
            .newCreateInstanceCommand()
            .bpmnProcessId(bpmnProcessId)
            .latestVersion()
            .variables(variables)
            .send()
            .join();

    return "Process Instance Key: " + event.getProcessDefinitionKey();
  }
}
