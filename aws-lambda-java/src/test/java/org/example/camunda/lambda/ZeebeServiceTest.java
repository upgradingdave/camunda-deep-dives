package org.example.camunda.lambda;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ZeebeServiceTest {

  @Test
  public void testCreateInstance() {
    ZeebeService zeebeService = new ZeebeService(ZeebeService.fromAppConstants());
    // ZeebeService zeebeService = new ZeebeService();
    Map<String, Object> variables = new HashMap<>();
    variables.put("bookHotelResult", false);
    zeebeService.createInstance(variables);
  }
}
