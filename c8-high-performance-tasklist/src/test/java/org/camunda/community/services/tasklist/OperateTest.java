package org.camunda.community.services.tasklist;

import org.camunda.community.CamundaConfig;
import org.camunda.community.services.OperateRestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OperateTest {

  private final CamundaConfig camundaConfig;
  private final OperateRestClient operateRestClient;

  @Autowired
  public OperateTest(CamundaConfig camundaConfig, OperateRestClient operateRestClient) {
    this.camundaConfig = camundaConfig;
    this.operateRestClient = operateRestClient;
  }

  @Test
  public void testCountActiveInstances() {

    Integer count = operateRestClient.countActiveInstancesByProcessId(camundaConfig.getProcessId());
    //assertEquals(4, count);
    assertTrue(count > 0);

  }

}
