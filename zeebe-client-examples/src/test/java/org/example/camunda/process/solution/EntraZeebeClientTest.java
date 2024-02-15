package org.example.camunda.process.solution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties
public class EntraZeebeClientTest {

  @Autowired ZeebeService zeebeService;

  @Autowired CustomEntraZeebeClient customEntraZeebeClient;

  @Test
  public void testZeebeClient() throws Exception {
    zeebeService.topologyRequest();
  }

  @Test
  public void customEntraZeebeClientTest() {
    customEntraZeebeClient.getZeebeClient().newTopologyRequest().send().join();
  }
}
