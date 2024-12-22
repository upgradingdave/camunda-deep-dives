package org.camunda.community.rest;

import io.camunda.zeebe.client.api.response.Topology;
import io.camunda.zeebe.client.api.search.response.ProcessInstance;
import org.camunda.community.model.InitialPayload;
import org.camunda.community.services.ZeebeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/process")
public class ProcessController {

  private final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);
  private final ZeebeService zeebeService;

  public ProcessController(@Autowired ZeebeService zeebeService) {
    this.zeebeService = zeebeService;
  }

  @GetMapping(value = "/topology")
  public Topology getTopology() {
    LOGGER.info("getTopology");
    return zeebeService.getTopology();
  }

  @GetMapping(value = "/instance/{instanceId}")
  public ProcessInstance getInstance(@PathVariable String instanceId) {
    LOGGER.info("getInstance");
    return zeebeService.getProcessInstance(instanceId).get(0);
  }

  @PostMapping(value = "/start")
  public InitialPayload startInstance(@RequestBody InitialPayload variables) {
    LOGGER.info("startInstance");
    return zeebeService.startProcessInstance(variables);
  }

  @PostMapping(value = "/jobs/{jobKey}/completion")
  public Boolean completeJob(@PathVariable String jobKey,
                             @RequestBody Map<String, Object> variables) {
    LOGGER.info("completeJob");
    return zeebeService.completeJob(jobKey, variables);
  }

}
