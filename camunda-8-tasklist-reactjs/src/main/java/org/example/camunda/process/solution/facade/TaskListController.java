package org.example.camunda.process.solution.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.tasklist.dto.FormResponse;
import io.camunda.tasklist.dto.TaskResponse;
import io.camunda.tasklist.dto.TaskSearchResponse;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.exception.TaskListRestException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.example.camunda.process.solution.service.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
public class TaskListController {

  private final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private final TaskListService taskListService;

  @Autowired
  public TaskListController(TaskListService taskListService) {
    this.taskListService = taskListService;
  }

  @RequestMapping(value = "/tasklist/getAssigneeTasks", method = RequestMethod.GET)
  public ResponseEntity<?> getAssigneeTasks(@RequestParam String userId)
      throws TaskListException, TaskListRestException {

    LOGGER.info("getTasks");

    List<TaskSearchResponse> tasks = taskListService.getAssigneeTasks(userId);
    return new ResponseEntity<>(tasks, HttpStatus.OK);
  }

  @RequestMapping(value = "/tasklist/getTask", method = RequestMethod.GET)
  public ResponseEntity<?> getTask(@RequestParam String taskId)
      throws TaskListException, TaskListRestException {

    LOGGER.info("getTask");

    TaskResponse task = taskListService.getTask(taskId);
    return new ResponseEntity<>(task, HttpStatus.OK);
  }

  @RequestMapping(value = "/tasklist/getForm", method = RequestMethod.GET)
  public ResponseEntity<?> getFormByKey(
      @RequestParam String formId, @RequestParam String processDefinitionKey)
      throws TaskListException, TaskListRestException {

    LOGGER.info("getForm");

    FormResponse form = taskListService.getForm(formId, processDefinitionKey);
    return new ResponseEntity<>(form, HttpStatus.OK);
  }

  @RequestMapping(
      value = "/tasklist/completeTask",
      method = RequestMethod.POST,
      consumes = {"application/json"})
  public ResponseEntity<?> completeTask(@RequestBody(required = true) String data)
      throws TaskListException {

    LOGGER.info("completeTask");

    throw new IllegalStateException("Not implemented");

    /*
    JSONObject obj = (JSONObject) JSONValue.parse(data);
    String taskId = obj.getAsString("taskId");

    JSONObject variables = (JSONObject) obj.get("variables");

    Task task = taskListService.completeTask(taskId, variables);

    return new ResponseEntity<>("", HttpStatus.OK);
     */

  }

  public Map jsonToMap(String json) {

    ObjectMapper mapper = new ObjectMapper();
    try {
      // convert JSON string to Map
      Map<String, String> map = mapper.readValue(json, Map.class);

      return map;
    } catch (IOException e) {
      throw new IllegalStateException("Unable to parse json");
    }
  }
}
