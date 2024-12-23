package org.camunda.community.services.tasklist;

import org.camunda.community.model.Task;
import org.camunda.community.model.TaskVariable;
import org.camunda.community.model.TokenResponse;
import org.camunda.community.services.TaskListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskListTest {

  private TaskListService taskListService;

  @Autowired
  public TaskListTest(TaskListService taskListService) {
    this.taskListService = taskListService;
  }

  @Test
  public void testTaskSearch() throws Exception {
    List<Task> results = taskListService.findTasksByBusinessKey("123");
    assertTrue(results.size() > 0);
  }

  @Test
  public void saveTask() throws Exception {

    List<TaskVariable> variables = new ArrayList<>();
    variables.add(TaskVariable.builder()
        .id("1")
        .build());

    Task task = Task.builder()
        .id("1")
        .variables(variables)
        .build();

    taskListService.saveTask(task);

    task = Task.builder()
        .id("2")
        .variables(variables)
        .build();

    taskListService.saveTask(task);

    task = taskListService.findTaskById("1");
    assertNotNull(task);

  }

}
