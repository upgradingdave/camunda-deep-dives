package io.camunda.tasklist.auth;

import io.camunda.tasklist.TaskListRestClient;
import io.camunda.tasklist.exception.TaskListException;

public interface Authentication {

  public void authenticate(TaskListRestClient taskListClient) throws TaskListException;

}
