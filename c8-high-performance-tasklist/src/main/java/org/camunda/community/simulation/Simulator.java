package org.camunda.community.simulation;

import org.camunda.community.model.*;
import org.camunda.community.services.TaskListRestClient;
import org.camunda.community.services.TaskRepository;
import org.camunda.community.services.ZeebeService;
import org.camunda.community.utils.DateUtils;
import org.camunda.community.utils.RandomNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Component
public class Simulator {

  private static final Logger log = LoggerFactory.getLogger(Simulator.class);

  // Local State
  Map<String, ProcessInstance> activePis;
  private final long simulatorStartTimeInMillis;
  private long lastPiStartTimeInMillis;
  private long lastCompleteTaskTimeInMillis;
  private long piStartedThisFrame;
  private boolean tasksCompletedThisFrame;

  // TODO implement these as micrometer timers
  private long totalIndexed;
  private Long avgMillisBeforeIndexed;

  private final ZeebeService zeebeService;
  private final TaskListRestClient taskListRestClient;
  private final TaskRepository taskRepository;
  private final SimulatorConfig simulatorConfig;

  @Autowired
  public Simulator(SimulatorConfig simulatorConfig,
                   ZeebeService zeebeService,
                   TaskListRestClient taskListRestClient,
                   TaskRepository taskRepository
  ) {
    this.zeebeService = zeebeService;
    this.taskListRestClient = taskListRestClient;
    this.taskRepository = taskRepository;

    this.simulatorConfig = simulatorConfig;

    // init state
    simulatorStartTimeInMillis = System.currentTimeMillis();
    lastPiStartTimeInMillis = System.currentTimeMillis();
    lastCompleteTaskTimeInMillis = System.currentTimeMillis();
    piStartedThisFrame = 0;
    tasksCompletedThisFrame = false;
    activePis = new HashMap<>();
    totalIndexed = 0;
    avgMillisBeforeIndexed = 0L;

    init();

  }

  private void init() {

    // Load any active simulated user tasks into the db
    List<Task> restTasks = taskListRestClient.findCreatedTasks();
    for (Task restTask : restTasks) {
      List<TaskVariable> variables = taskListRestClient.findTaskVariablesById(restTask.getId());
      for (TaskVariable variable : variables) {
        if(variable.getName().equals("simulated")) {
          log.info("Found existing simulated task {}", restTask.getId());
          taskRepository.save(restTask);
        }
      }
    }
  }

  @Scheduled(fixedRate = 300, initialDelay = 5000)
  public void simulateLoad() throws ParseException {

    long currentTime = System.currentTimeMillis();
    long totalPassedTime = currentTime - simulatorStartTimeInMillis;

    // Simulate starting instances
    if(totalPassedTime <= simulatorConfig.getPiStartDurationInSeconds()*1000) {
      simulateStartInstances(currentTime);
    }

    // Simulate completing User Tasks
    simulateCompleteUserTasks(currentTime);
  }

  protected void simulateStartInstances(long currentTime) {
    long piStartPassedTime = currentTime - lastPiStartTimeInMillis;
    long totalActivePis = activePis.size();
    long piToStart = simulatorConfig.getActivePiGoal() - totalActivePis;

    if (piStartPassedTime < simulatorConfig.getPiStartRateInSeconds() * 1000) {
      // we still have more time in this frame

      if (totalActivePis < simulatorConfig.getActivePiGoal()) {
        // we need to start more instances to maintain our goal

        //TODO: This could be improved by dividing the batches based on the start rate
        // defined in the config relative to the frequency that this spring method is scheduled
        // For example, if start rate is 2 per 5 seconds, and the spring method runs every 300 millis
        // then we only need to start 1 every time the spring method runs to achieve our goal of 2 per 5 seconds
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

  @Async
  protected void startProcessInstances(long batchSize) {
    log.info("Staring {} process instances, {} total active instances", batchSize, activePis.size());

    for (int i = 0; i < batchSize; i++) {
      InitialPayload payload = InitialPayload.builder()
          .createdByUserName("user"+piStartedThisFrame)
          .simulated(true)
          .build();
      piStartedThisFrame++;
      payload = zeebeService.startProcessInstance(payload);

      String creationTime = DateUtils.stringFromDate();

      ProcessInstance instance = ProcessInstance.builder()
          .creationDate(creationTime)
          .businessKey(payload.getBusinessKey())
          .build();

      activePis.put(payload.getBusinessKey(), instance);
    }
  }

  private void updateAvgMillisBeforeIndexed(long millisBeforeIndexed) {
    // Reset average every 50 tasks
    if(totalIndexed >= 30) {
      totalIndexed = 0;
      avgMillisBeforeIndexed = 0L;
    }
    totalIndexed = totalIndexed + 1;
    avgMillisBeforeIndexed = (avgMillisBeforeIndexed * (totalIndexed-1) + millisBeforeIndexed) / totalIndexed;
  }

  @Async
  protected void simulateCompleteUserTasks(long currentTime) throws ParseException {

    long completeTaskPassedTime = currentTime - lastCompleteTaskTimeInMillis;

    if (completeTaskPassedTime < simulatorConfig.getCompleteTaskRateInMillis()) {

      if (!tasksCompletedThisFrame) {
        // complete as many tasks as possible based on min/max complete times
        tasksCompletedThisFrame = true;

        // Get a list of active user tasks from TaskList
        List<Task> restTasks = taskListRestClient.findCreatedTasks();

        // Get a list of active user tasks from the DB
        List<Task> dbTasks = taskRepository.findTasksByTaskState(TaskState.TASK_CREATED);

        for (Task dbTask : dbTasks) {

          Integer completeTaskMillis = RandomNumberUtils.getRandom(
              simulatorConfig.getMinCompleteTaskMillis(),
              simulatorConfig.getMaxCompleteTaskMillis()
          );

          long createDateMillis = DateUtils.toMillis(dbTask.getCreationDate());
          if (createDateMillis + completeTaskMillis < currentTime) {

            // Simulate a user completing this task

            // Can we find the corresponding task in the rest results?
            if(restTasks.contains(dbTask)) {

              // Found it, complete the task via Zeebe
              log.debug("Completing task {}", dbTask.getId());
              Boolean success = zeebeService.completeJob(dbTask.getId(), new HashMap<>());

              long millisBeforeIndexed = currentTime - completeTaskMillis - createDateMillis;
              updateAvgMillisBeforeIndexed(millisBeforeIndexed);

              // Update the db
              dbTask.setCompletionDate(DateUtils.stringFromDate());
              dbTask.setTaskState(TaskState.TASK_COMPLETED);
              taskRepository.save(dbTask);

              // If this is the last User task, remove it from the activeInstances
              log.debug("Task Definition Id {} just completed", dbTask.getTaskDefinitionId());
              if(dbTask.getTaskDefinitionId().equals("Activity_lastUserTask")) {
                activePis.remove(dbTask.getBusinessKey());
                log.debug("Instance complete {}", dbTask.getBusinessKey());
                log.debug("Total Active PIs: {}", activePis.size());
              }

            } else {

              log.debug("Unable to find task {} in TaskList", dbTask.getId());

            }

          }
        }
      }

    } else {
      // time frame is up, let's do it again
      //log.info("Attempting to simulate users completing tasks ...");
      log.info("Average Time before indexing tasks is {}", avgMillisBeforeIndexed);
      lastCompleteTaskTimeInMillis = System.currentTimeMillis();
      tasksCompletedThisFrame = false;
    }
  }

}
