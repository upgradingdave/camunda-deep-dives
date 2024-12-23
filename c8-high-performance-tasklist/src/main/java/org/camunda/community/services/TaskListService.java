package org.camunda.community.services;


import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.camunda.community.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskListService {

  Logger logger = LoggerFactory.getLogger(TaskListService.class);

  private final ZeebeRestClient zeebeRestClient;
  private final TaskListRestClient taskListRestClient;
  private final TaskRepository taskRepository;
  private final TaskVariableRepository taskVariableRepository;

  @Autowired
  public TaskListService(
      TaskListRestClient taskListRestClient,
      ZeebeRestClient zeebeRestClient,
      TaskRepository taskRepository,
      TaskVariableRepository taskVariableRepository,
      MeterRegistry meterRegistry) {
    this.taskRepository = taskRepository;
    this.taskListRestClient = taskListRestClient;
    this.zeebeRestClient = zeebeRestClient;
    this.taskVariableRepository = taskVariableRepository;

    Gauge.builder("tasks_cache_count", taskRepository::count)
        .description("Number of cached tasks")
        .register(meterRegistry);

  }

  public List<Task> findTasksByBusinessKey(String businessKey) {

    List<Task> dbTasks = taskRepository.findTasksByBusinessKey(businessKey);
    List<Task> restTasks = taskListRestClient.findTasksByBusinessKey(businessKey);

    // merge the results
    for(Task task : dbTasks) {
      if(!restTasks.contains(task)) {
        logger.warn("Task {} not found", task.getId());
        restTasks.add(task);
      }
    }

    return restTasks;

  }

  public List<Task> findTasksByAssignee(String assignee) {

    List<Task> dbTasks = taskRepository.findTasksByAssignee(assignee);
    List<Task> restTasks = taskListRestClient.findTasksByAssignee(assignee);

    // merge the results
    for(Task task : dbTasks) {
      if(!restTasks.contains(task)) {
        logger.warn("Task {} not found", task.getId());
        restTasks.add(task);
      }
    }

    return restTasks;

  }

  public Task findTaskById(String taskId) {

    Task task = taskListRestClient.findTaskById(taskId);
    if(task == null) {
      task = taskRepository.findTaskById(taskId);
    }

    return task;

  }

  public List<TaskVariable> findTaskVariablesByTaskId(String taskId) {
    List<TaskVariable> variables = taskListRestClient.findTaskVariablesById(taskId);
    if(variables == null || variables.isEmpty()) {
      Task task = taskRepository.findTaskById(taskId);
      variables = task.getVariables();
    }

    return variables;
  }

  public Form findFormById(String formId) {
    return taskListRestClient.findFormById(formId);
  }

  public Task saveTask(Task task) {
    taskVariableRepository.saveAll(task.getVariables());
    return taskRepository.save(task);
  }

  public Boolean completeTask(String taskId, Map<String, Object> variables) {
    return zeebeRestClient.completeJob(taskId, variables);
  }

}
