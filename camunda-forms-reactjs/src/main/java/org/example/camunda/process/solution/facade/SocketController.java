package org.example.camunda.process.solution.facade;

import java.util.Map;
import org.example.camunda.process.solution.model.ProcessSolutionRequest;
import org.example.camunda.process.solution.model.ProcessSolutionResponse;
import org.example.camunda.process.solution.service.ZeebeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SocketController {

  private static final Logger LOG = LoggerFactory.getLogger(SocketController.class);

  @Autowired private ZeebeService zeebeService;

  @MessageMapping("/startProcess")
  @SendTo("/topic/process")
  public ProcessSolutionResponse startProcessInstance(@RequestBody ProcessSolutionRequest request) {

    String processId = (String) request.getKey();
    Map<String, Object> variables = request.getVariables();

    LOG.info("Starting process `" + processId + "` with variables: " + variables);

    try {
      return zeebeService.startProcess(processId, variables);
    } catch (Exception e) {
      return ProcessSolutionResponse.withError("StartProcessError", e);
    }
  }

  @MessageMapping("/completeTask")
  @SendTo("/topic/process")
  public ProcessSolutionResponse completeTask(@RequestBody ProcessSolutionRequest request) {

    long jobKey = (long) request.getKey();
    Map<String, Object> variables = request.getVariables();

    LOG.info("Complete Task `" + jobKey + "` with variables: " + variables);

    try {
      return zeebeService.completeTask(jobKey, variables);
    } catch (Exception e) {
      return ProcessSolutionResponse.withError("CompleteTaskError", e);
    }
  }
}
