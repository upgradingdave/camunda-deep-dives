package org.camunda.community.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.Topology;
import io.camunda.zeebe.client.api.search.response.ProcessInstance;
import io.camunda.zeebe.client.api.search.response.SearchQueryResponse;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import lombok.Getter;
import org.camunda.community.CamundaConfig;
import org.camunda.community.model.InitialPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ZeebeService {

  @Getter
  private final ZeebeClient zeebeClient;

  private final ZeebeRestClient zeebeRestClient;
  private final CamundaConfig camundaConfig;

  @Autowired
  public ZeebeService(CamundaConfig camundaConfig, ZeebeRestClient zeebeRestClient) {

    OAuthCredentialsProvider credentialsProvider =
        new OAuthCredentialsProviderBuilder()
            .authorizationServerUrl(camundaConfig.getOAuthApi())
            .audience(camundaConfig.getZeebeAudience())
            .clientId(camundaConfig.getClientId())
            .clientSecret(camundaConfig.getClientSecret())
            .build();

    this.zeebeClient = ZeebeClient.newClientBuilder()
        .grpcAddress(URI.create(camundaConfig.getZeebeGrpc()))
        .restAddress(URI.create(camundaConfig.getZeebeRest()))
        .credentialsProvider(credentialsProvider)
        .build();
    this.zeebeRestClient = zeebeRestClient;
    this.camundaConfig = camundaConfig;
  }

  public Topology getTopology() {
    return zeebeClient.newTopologyRequest().send().join();
  }

  public InitialPayload startProcessInstance(InitialPayload initialPayload) {

    if(initialPayload.getBusinessKey() == null) {
      initialPayload.setBusinessKey(UUID.randomUUID().toString());
    }

    if(initialPayload.getCreatedByUserName() == null) {
      throw new RuntimeException("CreatedByUserName is required to start the process");
    }

/*    PublishMessageResponse result = zeebeClient.newPublishMessageCommand()
        .messageName(CamundaConfig.MESSAGE_START)
        .correlationKey(initialPayload.getCreatedByUserName())
        .variables(initialPayload)
        .send().join();*/

    ProcessInstanceEvent result = zeebeClient.newCreateInstanceCommand()
        .bpmnProcessId(camundaConfig.getProcessId())
        .latestVersion()
        .variables(initialPayload)
        .send().join();

    return initialPayload;
  }

  public List<ProcessInstance> getProcessInstance(String processInstanceKey) {
    SearchQueryResponse<ProcessInstance> result =
        zeebeClient.newProcessInstanceQuery().send().join();
    return result.items();
  }

  public Boolean completeJob(String jobId, Map<String, Object> variables) {
    CompleteJobResponse response = zeebeClient.newCompleteCommand(Long.parseLong(jobId)).variables(variables).send().join();
    return true;
    //return zeebeRestClient.completeJob(jobId, variables);
  }

}
