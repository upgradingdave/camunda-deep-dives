package org.camunda.community.services.tasklist;

import org.camunda.community.model.Task;
import org.camunda.community.model.TokenResponse;
import org.camunda.community.services.TaskListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
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
  public void testTaskList() throws Exception {
    TokenResponse tokenResponse = taskListService.getBearerToken();
    assertNotNull(tokenResponse);

    String token = tokenResponse.getAccessToken();
    assertNotNull(token);

    assertFalse(tokenResponse.isExpired());

  }

  @Test
  public void testTaskSearch() throws Exception {
    List<Task> results = taskListService.findTasksByBusinessKey("123");
    assertTrue(results.size() > 0);
  }

}
