package io.camunda.tasklist;

import io.camunda.tasklist.exception.TaskListException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

@Ignore
public class SMTests {

  TestUtils testUtils = new TestUtils();

  @Test
  public void smAuthTest() throws TaskListException {
    Properties props = testUtils.loadProps("test.sm.properties");
    testUtils.authTest(props);
  }
}
