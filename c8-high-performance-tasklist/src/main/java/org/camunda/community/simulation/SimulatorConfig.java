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

  // How fast to start PI's
  private Long piStartBatchSize;

  // Start rate in seconds
  private Long piStartRateInSeconds;

  // How many active PI's at any given time
  private Long activePiGoal;

  // How long to start instances
  private Long piStartDurationInSeconds;


}
