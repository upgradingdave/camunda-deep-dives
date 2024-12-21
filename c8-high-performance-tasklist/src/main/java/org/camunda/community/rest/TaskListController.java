package org.camunda.community.rest;

import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.Topology;
import io.camunda.zeebe.client.api.search.response.ProcessInstance;
import org.camunda.community.model.Task;
import org.camunda.community.model.TokenResponse;
import org.camunda.community.services.BPMNService;
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
  private TaskListService tasklist;

  public TaskListController(@Autowired TaskListService tasklist) {
    this.tasklist = tasklist;
  }

  @PostMapping(value = "/auth")
  public TokenResponse getBearerToken() {
    LOGGER.info("getBearerToken");
    return tasklist.getBearerToken();
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

}
