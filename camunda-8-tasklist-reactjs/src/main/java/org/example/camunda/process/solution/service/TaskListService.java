package org.example.camunda.process.solution.service;

import io.camunda.tasklist.dto.Form;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.exception.TaskListException;
import java.util.List;
import java.util.Map;

public interface TaskListService {

  List<Task> getAssigneeTasks(String userId) throws TaskListException;

  Task getTask(String taskId) throws TaskListException;

  Form getFormById(String formId, String processDefinitionId) throws TaskListException;

  Form getFormByKey(String formKey, String processDefinitionId) throws TaskListException;

  Task completeTask(String taskId, Map<String, Object> variablesMap) throws TaskListException;
}
