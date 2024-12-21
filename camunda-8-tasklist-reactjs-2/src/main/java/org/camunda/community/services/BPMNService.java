package org.camunda.community.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.Topology;
import io.camunda.zeebe.client.api.search.response.ProcessInstance;
import io.camunda.zeebe.client.api.search.response.SearchQueryResponse;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import org.camunda.community.CamundaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class BPMNService {

  private ZeebeClient zeebeClient;

  @Autowired
  public BPMNService(CamundaConfig camundaConfig) {

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
  }

  public ZeebeClient getZeebeClient() {
    return zeebeClient;
  }

  public Topology getTopology() {
    Topology topology = zeebeClient.newTopologyRequest().send().join();
    return topology;
  }

  public ProcessInstanceEvent startProcessInstance(String processDefinitionId, Map<Object, Object> variables) {
    ProcessInstanceEvent result = zeebeClient.newCreateInstanceCommand()
        .bpmnProcessId(processDefinitionId)
        .latestVersion()
        .variables(variables)
        .send()
        .join();

    return result;
  }

  public List<ProcessInstance> getProcessInstance(String processInstanceKey) {
    SearchQueryResponse<ProcessInstance> result =
        zeebeClient.newProcessInstanceQuery().send().join();
    return result.items();
  }

}
