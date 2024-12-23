package org.camunda.community.simulation;

import org.camunda.community.CamundaConfig;
import org.camunda.community.model.InitialPayload;
import org.camunda.community.services.OperateRestClient;
import org.camunda.community.services.ZeebeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Simulator {

  private static Logger log = LoggerFactory.getLogger(Simulator.class);

  // Local State
  List<InitialPayload> activePis;
  private long simulatorStartTimeInMillis;
  private long lastPiStartTimeInMillis;
  private long piStartedThisFrame;

  private CamundaConfig camundaConfig;
  private OperateRestClient operateRestClient;
  private ZeebeService zeebeService;
  private SimulatorConfig simulatorConfig;

  @Autowired
  public Simulator(CamundaConfig camundaConfig,
                   SimulatorConfig simulatorConfig,
                   OperateRestClient operateRestClient,
                   ZeebeService zeebeService
  ) {
    this.camundaConfig = camundaConfig;
    this.operateRestClient = operateRestClient;
    this.zeebeService = zeebeService;
    this.simulatorConfig = simulatorConfig;

    // init state
    simulatorStartTimeInMillis = System.currentTimeMillis();
    lastPiStartTimeInMillis = System.currentTimeMillis();
    piStartedThisFrame = 0;
    activePis = new ArrayList<>();

  }

  @Scheduled(fixedRate = 300, initialDelay = 5000)
  public void simulateLoad() {

    long currentTime = System.currentTimeMillis();
    long totalPassedTime = currentTime - simulatorStartTimeInMillis;

    // Handle Starting Instances
    if(totalPassedTime <= simulatorConfig.getPiStartDurationInSeconds()*1000) {

      long piStartPassedTime = currentTime - lastPiStartTimeInMillis;
      long totalActivePis = activePis.size();
      long piToStart = simulatorConfig.getActivePiGoal() - totalActivePis;

      if (piStartPassedTime < simulatorConfig.getPiStartRateInSeconds() * 1000) {
        // we still have more time in this frame

        if (totalActivePis < simulatorConfig.getActivePiGoal()) {
          // we need to start more instances to maintain our goal

          long batchSize = simulatorConfig.getPiStartBatchSize();
          if (batchSize + totalActivePis > simulatorConfig.getActivePiGoal()) {
            batchSize = simulatorConfig.getActivePiGoal() - totalActivePis;
          }

          if (piStartedThisFrame < batchSize) {
            startProcessInstances(batchSize);
          }
        }

      } else {
        // time is up
        if (totalActivePis < simulatorConfig.getActivePiGoal() && piStartedThisFrame <= 0) {
          // we ran out of time!
          log.warn("Simulator has fallen behind by {}", piToStart);
        }

        // reset state for next frame
        lastPiStartTimeInMillis = System.currentTimeMillis();
        piStartedThisFrame = 0;
      }
    }

  }

  @Async
  protected void startProcessInstances(long batchSize) {
    log.info("Staring some process instances, {} started so far", batchSize);

    for (int i = 0; i < batchSize; i++) {
      InitialPayload payload = InitialPayload.builder()
          .createdByUserName("user"+piStartedThisFrame)
          .build();
      piStartedThisFrame++;
      payload = zeebeService.startProcessInstance(payload);
      activePis.add(payload);
    }
  }

}
