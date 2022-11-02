package org.example.camunda.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.camunda.zeebe.client.CredentialsProvider;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import java.util.HashMap;
import java.util.Map;

public class CreateInstanceHandler implements RequestHandler {

  @Override
  public Object handleRequest(Object input, Context context) {

    ZeebeClient zeebeClient =
        ZeebeClient.newCloudClientBuilder()
            .withClusterId(AppConstants.ZEEBE_CLUSTER_ID)
            .withClientId(AppConstants.ZEEBE_CLIENT_ID)
            .withClientSecret(AppConstants.ZEEBE_CLIENT_SECRET)
            .withRegion(AppConstants.ZEEBE_REGION)
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
