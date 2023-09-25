package org.example.camunda.process.solution;

import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import org.example.camunda.process.solution.service.TaskListService;
import org.example.camunda.process.solution.service.TaskListServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskListTest {

  @Autowired TaskListService taskListService;

  @Test
  public void testTaskListQuery() throws TaskListException {

    CamundaTaskListClient client = ((TaskListServiceImpl) taskListService).getClient();
    client.getTasks(false, TaskState.CREATED, 50);
  }
}
