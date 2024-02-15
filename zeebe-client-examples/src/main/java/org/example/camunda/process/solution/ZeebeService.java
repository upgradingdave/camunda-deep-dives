package org.example.camunda.process.solution;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZeebeService {

  private ZeebeClient zeebeClient;

  public ZeebeService(@Autowired ZeebeClient zeebeClient) {
    this.zeebeClient = zeebeClient;
  }

  public void topologyRequest() {
      Topology join = zeebeClient.newTopologyRequest().send().join();
      System.out.println(join);
  }

}
