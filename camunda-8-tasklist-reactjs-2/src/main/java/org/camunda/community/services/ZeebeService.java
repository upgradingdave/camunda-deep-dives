package org.camunda.community.services;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZeebeService {

  ZeebeClient zeebeClient;

  public ZeebeService(@Autowired ZeebeClient zeebeClient) {
    this.zeebeClient = zeebeClient;
  }

  public Topology getTopology() {
    Topology topology = zeebeClient.newTopologyRequest().send().join();
    return topology;
  }

}
