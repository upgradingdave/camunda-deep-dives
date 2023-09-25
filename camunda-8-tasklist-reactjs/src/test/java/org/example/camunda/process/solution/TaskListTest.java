package org.example.camunda.process.solution;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.camunda.tasklist.TaskListRestClient;
import io.camunda.tasklist.dto.TaskSearchResponse;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.exception.TaskListRestException;
import java.util.List;
import org.example.camunda.process.solution.service.TaskListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskListTest {

  @Autowired TaskListService taskListService;

  @Test
  public void testTaskListQuery() throws TaskListException, TaskListRestException {

    TaskListRestClient client = taskListService.getClient();
    List<TaskSearchResponse> tasks = taskListService.getAssigneeTasks("junit");

    assertNotNull(tasks);
  }
}
