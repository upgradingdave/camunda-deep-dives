package org.example.camunda.process.solution.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeCustomHeaders;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariablesAsType;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.example.camunda.process.solution.model.ProcessSolutionResponse;
import org.example.camunda.process.solution.service.ZeebeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserTaskWorker {

  private static final Logger LOG = LoggerFactory.getLogger(UserTaskWorker.class);

  @Autowired SimpMessagingTemplate simpMessagingTemplate;

  @Autowired ZeebeService zeebeService;

  @ZeebeWorker(type = "io.camunda.zeebe:userTask")
  public void completeUserTask(
      final JobClient client,
      final ActivatedJob job,
      @ZeebeVariablesAsType Map<String, Object> variables,
      @ZeebeCustomHeaders Map<String, String> headers) {

    LOG.info("User Task Worker triggered with variables: " + variables);

    Map<String, Object> controlVariables = new HashMap<>();
    controlVariables.put("jobKey", job.getKey());

    String formKey = headers.get("io.camunda.zeebe:formKey");
    controlVariables.put("formKey", formKey);

    String formId = zeebeService.parseFormIdFromKey(formKey);
    controlVariables.put("formId", formId);
    String assignee = headers.get("io.camunda.zeebe:assignee");
    controlVariables.put("assignee", assignee);
    String schema = null;

    try {
      schema = zeebeService.getFormSchemaFromBpmn(formId);
      controlVariables.put("formSchema", schema);

      simpMessagingTemplate.convertAndSend(
          "/topic/forms/" + assignee,
          ProcessSolutionResponse.withResult("formReady", variables, controlVariables));

      zeebeService.requestTimeout(job.getKey());
      // client.newCompleteCommand(job.getKey()).send();
      // client.newFailCommand(job.getKey()).retries(1).requestTimeout(Duration.ofMinutes(30)).send();

    } catch (Exception e) {
      client.newFailCommand(job.getKey()).retries(0).send();
    }
  }
}
