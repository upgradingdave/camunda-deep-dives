package org.example.camunda.process.solution.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.tasklist.dto.Form;
import io.camunda.tasklist.dto.Task;
import io.camunda.tasklist.exception.TaskListException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.example.camunda.process.solution.service.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Profile("tasklist")
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
  public ResponseEntity<?> getAssigneeTasks(@RequestParam String userId) throws TaskListException {

    LOGGER.info("getTasks");

    List<Task> tasks = taskListService.getAssigneeTasks(userId);
    return new ResponseEntity<>(tasks, HttpStatus.OK);
  }

  @RequestMapping(value = "/tasklist/getTask", method = RequestMethod.GET)
  public ResponseEntity<?> getTask(@RequestParam String taskId) throws TaskListException {

    LOGGER.info("getTask");

    Task task = taskListService.getTask(taskId);
    return new ResponseEntity<>(task, HttpStatus.OK);
  }

  @RequestMapping(value = "/tasklist/getFormByKey", method = RequestMethod.GET)
  public ResponseEntity<?> getFormByKey(
      @RequestParam String formKey, @RequestParam String processDefinitionId)
      throws TaskListException {

    LOGGER.info("getFormByKey");

    Form form = taskListService.getFormByKey(formKey, processDefinitionId);
    return new ResponseEntity<>(form, HttpStatus.OK);
  }

  @RequestMapping(value = "/tasklist/getFormById", method = RequestMethod.GET)
  public ResponseEntity<?> getFormById(
      @RequestParam String formId, @RequestParam String processDefinitionId)
      throws TaskListException {

    LOGGER.info("getFormById");

    Form form = taskListService.getFormById(formId, processDefinitionId);
    return new ResponseEntity<>(form, HttpStatus.OK);
  }

  @RequestMapping(
      value = "/tasklist/completeTask",
      method = RequestMethod.POST,
      consumes = {"application/json"})
  public ResponseEntity<?> completeTask(@RequestBody(required = true) String data)
      throws TaskListException {

    LOGGER.info("completeTask");

    JSONObject obj = (JSONObject) JSONValue.parse(data);
    String taskId = obj.getAsString("taskId");

    JSONObject variables = (JSONObject) obj.get("variables");

    Task task = taskListService.completeTask(taskId, variables);
    return new ResponseEntity<>(task, HttpStatus.OK);
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
