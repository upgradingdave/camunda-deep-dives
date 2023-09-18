package io.camunda.tasklist;

import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.exception.TaskListRestException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;


@Ignore
public class SaaSTests {

  TestUtils testUtils = new TestUtils();
  Properties props = testUtils.loadProps("test.saas.properties");

  @Test
  public void saasAuthTest() throws TaskListException {
    testUtils.authTest(props);
  }

  @Test
  public void searchTasks() throws TaskListException, TaskListRestException {
    testUtils.searchTest(props);
  }


}
