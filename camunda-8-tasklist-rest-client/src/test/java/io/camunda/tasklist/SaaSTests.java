package io.camunda.tasklist;

import io.camunda.tasklist.dto.TaskResponse;
import io.camunda.tasklist.dto.TaskSearchResponse;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.exception.TaskListRestException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;


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
    testUtils.findCreatedUnAssignedTasks(props);
  }

  @Test
  public void assignTask() throws TaskListException, TaskListRestException {
    List<TaskSearchResponse> tasks = testUtils.findCreatedUnAssignedTasks(props);
    assertTrue(tasks.size() > 0);
    TaskSearchResponse taskSearchResponse = tasks.get(0);
    assertEquals("CREATED", taskSearchResponse.getTaskState());
    assertEquals("2251799813954673", taskSearchResponse.getProcessDefinitionKey());
    assertEquals("User Task Unit Test", taskSearchResponse.getProcessName());

    TaskResponse taskResponse= testUtils.assignTask(
        props, taskSearchResponse.getId(), "dave");

    assertNotNull(taskResponse);
    assertEquals("dave", taskResponse.getAssignee());
  }


}
