package org.camunda.community.simulation;

import lombok.AllArgsConstructor;;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "camunda.simulator")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulatorConfig {

  // Number of instances to start during time defined in piStartRateInSeconds
  private Long piStartBatchSize;

  // Simulator will attempt to start piStartBatchSize instances in this amount of time
  private Long piStartRateInSeconds;

  // Start instances until we reach this goal. As instances complete, more instances will be created
  private Long activePiGoal;

  // After this time, no new instances will be created, but instances will still be completed
  private Long piStartDurationInSeconds;

  // How often to run complete user task logic
  private long completeTaskRateInMillis;

  //Min time a simulated user would complete a user task
  private Integer minCompleteTaskMillis;

  //Max time a simulated user completes a user task
  private Integer maxCompleteTaskMillis;


}
