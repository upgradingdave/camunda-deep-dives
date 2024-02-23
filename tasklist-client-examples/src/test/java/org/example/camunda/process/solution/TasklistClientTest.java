package org.example.camunda.process.solution;

import io.camunda.tasklist.dto.TaskList;
import io.camunda.tasklist.dto.TaskSearch;
import io.camunda.tasklist.dto.TaskState;
import io.camunda.tasklist.exception.TaskListException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableConfigurationProperties
public class TasklistClientTest {

  @Autowired
  CustomTasklistClient customTasklistClient;

  @Test
  public void customTasklistClientTest() throws TaskListException {
    TaskSearch taskSearch = new TaskSearch();
    taskSearch.setState(TaskState.CREATED);
    TaskList taskList = customTasklistClient.getTaskListClient().getTasks(taskSearch);
    assertNotNull(taskList);
  }

}
