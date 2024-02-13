package org.example.camunda.process.solution;

import org.junit.jupiter.api.Test;

public class EntraZeebeClientTest {
  @Test
  public void testZeebeClient() throws Exception {
    EntraZeebeClient zeebeClient = new EntraZeebeClient();
    zeebeClient.topologyRequest();
  }
}
