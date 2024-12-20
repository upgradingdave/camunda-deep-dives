package org.camunda.community.rest;

import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.Topology;
import io.camunda.zeebe.client.api.search.response.ProcessInstance;
import org.camunda.community.services.BPMNService;
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
  private BPMNService bpmnService;

  public ProcessController(@Autowired BPMNService zeebeService) {
    this.bpmnService = zeebeService;
  }

  @GetMapping(value = "/topology")
  public Topology getTopology() {
    LOGGER.info("getTopology");
    return bpmnService.getTopology();
  }

  @GetMapping(value = "/instance/{instanceId}")
  public ProcessInstance getInstance(@PathVariable String instanceId) {
    LOGGER.info("getInstance");
    return null;
    //return bpmnService.getProcessInstance(instanceId).get(0);
  }

  @PostMapping(value = "/start/{processId}")
  public ProcessInstanceEvent startInstance(@PathVariable String processId,
                                            @RequestBody Map<Object, Object> variables) {
    LOGGER.info("startInstance");
    return bpmnService.startProcessInstance(processId, variables);
  }

}
