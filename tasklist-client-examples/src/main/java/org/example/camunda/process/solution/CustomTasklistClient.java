package org.example.camunda.process.solution;

import io.camunda.common.auth.*;
import io.camunda.tasklist.CamundaTaskListClient;
import io.camunda.tasklist.exception.TaskListException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class CustomTasklistClient {

  private CamundaTaskListClient taskListClient;

  public CustomTasklistClient(
      @Value("${tasklist.client.url:notProvided}") String tasklistUrl,
      @Value("${tasklist.client.username:notProvided}") String username,
      @Value("${tasklist.client.password:notProvided}") String password) throws TaskListException {

    SimpleConfig simpleConf = new SimpleConfig();
    simpleConf.addProduct(Product.TASKLIST, new SimpleCredential(username, password));
    Authentication auth =
        SimpleAuthentication.builder()
            .simpleConfig(simpleConf)
            .simpleUrl(tasklistUrl)
            .build();

    taskListClient =
        CamundaTaskListClient.builder()
            .taskListUrl(tasklistUrl)
            .authentication(auth)
            .build();
  }

  public CamundaTaskListClient getTaskListClient() {
    return taskListClient;
  }
}
