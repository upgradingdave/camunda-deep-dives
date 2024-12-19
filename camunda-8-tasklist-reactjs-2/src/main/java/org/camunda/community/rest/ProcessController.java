package org.camunda.community.rest;

import io.camunda.zeebe.client.api.response.Topology;
import org.camunda.community.services.ZeebeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Proxy;

@CrossOrigin
@RestController
@RequestMapping("/process")
public class ProcessController {

  private final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);
  private ZeebeService zeebeService;

  public ProcessController(@Autowired ZeebeService zeebeService) {
    this.zeebeService = zeebeService;
  }

  @GetMapping(value = "/topology")
  public Topology getTopology() {

    LOGGER.info("getTopology");
    return zeebeService.getTopology();
  }

}
