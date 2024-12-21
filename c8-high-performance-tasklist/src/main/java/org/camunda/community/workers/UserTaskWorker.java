package org.camunda.community.workers;

import io.camunda.zeebe.client.api.worker.JobWorker;
import org.camunda.community.services.BPMNService;
import org.camunda.community.services.TaskListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UserTaskWorker {

  static Logger logger = LoggerFactory.getLogger(UserTaskWorker.class);

  private BPMNService bpmnService;
  private TaskListService taskListService;

  @Autowired
  public UserTaskWorker(BPMNService bpmnService, TaskListService taskListService) {
    this.bpmnService = bpmnService;
    this.taskListService = taskListService;
    createUserTaskWorker();
  }

  private void createUserTaskWorker() {
    String userTaskJobType = "io.camunda.zeebe:userTask";
    final JobWorker workerRegistration =
        bpmnService.getZeebeClient()
            .newWorker()
            .jobType(userTaskJobType)
            .handler(new UserTaskWorkerHandler(taskListService))
            .timeout(Duration.ofDays(90))
            .open();
    logger.info("User Task Job worker opened and receiving jobs.");
  }
}


