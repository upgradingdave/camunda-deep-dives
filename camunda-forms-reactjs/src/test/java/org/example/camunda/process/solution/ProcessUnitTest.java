package org.example.camunda.process.solution;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.camunda.process.solution.facade.ProcessController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ProcessApplication.class) // will deploy BPMN & DMN models
public class ProcessUnitTest {

  @Autowired private ProcessController processController;

  @Test
  public void sanityTest() throws Exception {
    assertTrue(true);
  }
}
