package org.example.camunda.process.solution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties
public class ZeebeClientTest {

  @Autowired ZeebeService zeebeService;

  @Test
  public void testZeebeClient() throws Exception {
    zeebeService.topologyRequest();
  }
}
