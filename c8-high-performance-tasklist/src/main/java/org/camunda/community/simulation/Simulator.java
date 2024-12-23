package org.camunda.community.simulation;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Simulator {

  private long piStarted = 0;
  // How fast to start PI's
  private long piStartRatePerSecond = 5;
  // How many active PI's at any given time
  private long acitvePiGoal = 105;


  private long counter = 0;

  @Scheduled(fixedRate = 1000, initialDelay = 5000)
  public void startSomeProcessInstances() {

    counter++;
    long processInstancesToStart = 0;

    //if()
    //startProcessInstances( processInstancesToStart );

  }


}
