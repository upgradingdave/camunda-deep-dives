package org.camunda.community.workers;

import io.camunda.zeebe.client.api.worker.JobWorker;
import org.camunda.community.services.BPMNService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UserTaskWorker {

  static Logger logger = LoggerFactory.getLogger(UserTaskWorker.class);

  private BPMNService bpmnService;

  @Autowired
  public UserTaskWorker(BPMNService bpmnService) {
    this.bpmnService = bpmnService;

    createUserTaskWorker();
  }

  private void createUserTaskWorker() {
    String userTaskJobType = "io.camunda.zeebe:userTask";
    final JobWorker workerRegistration =
        bpmnService.getZeebeClient()
            .newWorker()
            .jobType(userTaskJobType)
            .handler(new UserTaskWorkerHandler())
            .timeout(Duration.ofDays(90))
            .open();
    logger.info("User Task Job worker opened and receiving jobs.");
  }
}

  /*
  @JobWorker(
      type = "io.camunda.zeebe:userTask",
      autoComplete = false,
      timeout = 2592000000L) // set timeout to 30 days
  public void userTaskWorker(
      final JobClient client,
      final ActivatedJob job,
      @VariablesAsType Map<String, Object> variables,
      @CustomHeaders Map<String, String> headers) {

    try {


  }
  */


