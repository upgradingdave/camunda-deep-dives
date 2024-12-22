package org.camunda.community.workers;

import io.camunda.zeebe.client.api.worker.JobWorker;
import org.camunda.community.services.ZeebeService;
import org.camunda.community.services.TaskListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UserTaskWorker {

  static Logger logger = LoggerFactory.getLogger(UserTaskWorker.class);

  private ZeebeService zeebeService;
  private TaskListService taskListService;

  @Autowired
  public UserTaskWorker(ZeebeService zeebeService, TaskListService taskListService) {
    this.zeebeService = zeebeService;
    this.taskListService = taskListService;
    createUserTaskWorker();
  }

  private void createUserTaskWorker() {
    String userTaskJobType = "io.camunda.zeebe:userTask";
    final JobWorker workerRegistration =
        zeebeService.getZeebeClient()
            .newWorker()
            .jobType(userTaskJobType)
            .handler(new UserTaskWorkerHandler(taskListService))
            .timeout(Duration.ofDays(90))
            .open();
    logger.info("User Task Job worker opened and receiving jobs.");
  }
}


