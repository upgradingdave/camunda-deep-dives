package org.camunda.community.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.Topology;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import org.camunda.community.ZeebeClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

@Component
public class BPMNService {

  private ZeebeClient zeebeClient;

  @Autowired
  public BPMNService(ZeebeClientConfig zeebeClientConfig) {

    if(zeebeClientConfig.getMode().equals("saas")) {
      String oAuthAPI = "https://login.cloud.camunda.io/oauth/token";
      String zeebeGrpc = "grpcs://" + zeebeClientConfig.getClusterId() + "." + zeebeClientConfig.getRegion() + ".zeebe.camunda.io";
      String zeebeRest = "https://" + zeebeClientConfig.getRegion() + ".zeebe.camunda.io/" + zeebeClientConfig.getClusterId() + ".zeebe.camunda.io:443";
      String audience = "zeebe.camunda.io";

      OAuthCredentialsProvider credentialsProvider =
          new OAuthCredentialsProviderBuilder()
              .authorizationServerUrl(oAuthAPI)
              .audience(audience)
              .clientId(zeebeClientConfig.getClientId())
              .clientSecret(zeebeClientConfig.getClientSecret())
              .build();

      this.zeebeClient = ZeebeClient.newClientBuilder()
          .grpcAddress(URI.create(zeebeGrpc))
          .restAddress(URI.create(zeebeRest))
          .credentialsProvider(credentialsProvider)
          .build();

    }
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

  /*
  public List<ProcessInstance> getProcessInstance(String processInstanceKey) {
    SearchQueryResponse<ProcessInstance> result =
        zeebeClient.newProcessInstanceQuery().send().join();
    return result.items();
  }*/

}
