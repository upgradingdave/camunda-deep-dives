package org.camunda.community.rest;

import org.camunda.community.model.*;
import org.camunda.community.services.TaskListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/tasklist")
public class TaskListController {

  private final Logger LOGGER = LoggerFactory.getLogger(TaskListController.class);
  private final TaskListService tasklist;

  public TaskListController(@Autowired TaskListService tasklist) {
    this.tasklist = tasklist;
  }

  @GetMapping(value = "/findTasksByBusinessKey/{businessKey}")
  public List<Task> findTasksByBusinessKey(@PathVariable String businessKey) {
    LOGGER.info("findTasksByBusinessKey");
    return tasklist.findTasksByBusinessKey(businessKey);
  }

  @GetMapping(value = "/findTasksByAssignee/{assignee}")
  public List<Task> findTasksByAssignee(@PathVariable String assignee) {
    LOGGER.info("findTasksByAssignee");
    return tasklist.findTasksByAssignee(assignee);
  }

  @GetMapping(value = "/findTaskById/{taskId}")
  public Task findTaskById(@PathVariable String taskId) {
    LOGGER.info("findTaskById");
    return tasklist.findTaskById(taskId);
  }

  @GetMapping(value = "/findTaskVariablesById/{taskId}")
  public List<TaskVariable> findTaskVariablesById(@PathVariable String taskId) {
    LOGGER.info("findTaskVariablesById");
    return tasklist.findTaskVariablesByTaskId(taskId);
  }

  @GetMapping(value = "/findFormById/{formId}")
  public Form findFormById(@PathVariable String formId) {
    LOGGER.info("findFormById");
    return tasklist.findFormById(formId);
  }

  @PostMapping(value = "/complete/{taskId}")
  public Boolean completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
    LOGGER.info("completeTask");
    return tasklist.completeTask(taskId, variables);
  }

}
