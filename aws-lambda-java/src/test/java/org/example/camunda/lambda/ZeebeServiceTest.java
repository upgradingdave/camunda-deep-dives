package org.example.camunda.lambda;

import org.junit.jupiter.api.Test;

public class ZeebeServiceTest {

  @Test
  public void testCreateInstance() {
    ZeebeService zeebeService = new ZeebeService(ZeebeService.fromAppConstants());
    // ZeebeService zeebeService = new ZeebeService();
    zeebeService.createInstance();
  }
}
