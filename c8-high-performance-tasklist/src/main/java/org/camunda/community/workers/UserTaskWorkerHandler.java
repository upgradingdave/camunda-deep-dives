package org.camunda.community.workers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import org.camunda.community.model.Task;
import org.camunda.community.model.TaskState;
import org.camunda.community.model.TaskVariable;
import org.camunda.community.services.TaskListService;
import org.camunda.community.utils.DateUtils;
import org.camunda.community.utils.JSONUtils;
import org.camunda.community.utils.RandomNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserTaskWorkerHandler implements JobHandler {

  static Logger logger = LoggerFactory.getLogger(UserTaskWorkerHandler.class);

  private final TaskListService taskListService;

  public UserTaskWorkerHandler(TaskListService taskListService) {
    this.taskListService = taskListService;
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) throws Exception {

    logger.debug("User Task Worker Handler started");

    Task task = new Task();
    task.setJobKey(job.getKey());
    // Prior to 8.1.5, taskId was ElementInstanceKey
    // String taskId = Long.toString(job.getElementInstanceKey());
    // since 8.1.5, the taskId and jobKey were the same, but not sure in 8.6?
    task.setId(Long.valueOf(job.getKey()).toString());
    task.setProcessInstanceKey(Long.valueOf(job.getProcessInstanceKey()).toString());
    task.setProcessId(job.getBpmnProcessId());
    // task.setProcessName(bpmnService.getProcessInstance(bpmnProcessId).get(0).getProcessName());
    task.setProcessDefinitionKey(Long.valueOf(job.getProcessDefinitionKey()).toString());
    task.setTaskDefinitionId(job.getElementId());
    task.setTenantId(job.getTenantId());

    task.setTaskState(TaskState.TASK_CREATED);
    task.setImplementation("JOB_WORKER");

    if (!job.getCustomHeaders().isEmpty()) {

      if (job.getCustomHeaders().containsKey("io.camunda.zeebe:assignee")) {
        task.setAssignee(job.getCustomHeaders().get("io.camunda.zeebe:assignee"));
      }

      if (job.getCustomHeaders().containsKey("io.camunda.zeebe:candidateGroups")) {
        String groups = job.getCustomHeaders().get("io.camunda.zeebe:candidateGroups");
        task.setCandidateGroups(
            JSONUtils.toParameterizedObject(groups, new TypeReference<>() {}));
      }

      if(job.getCustomHeaders().containsKey("io.camunda.zeebe:formKey")) {
        task.setFormKey(job.getCustomHeaders().get("io.camunda.zeebe:formKey"));
      }

      if (job.getCustomHeaders().containsKey("io.camunda.zeebe:dueDate")) {
        task.setDueDate(
            OffsetDateTime.parse(
                job.getCustomHeaders().get("io.camunda.zeebe:dueDate"), DateTimeFormatter.ISO_ZONED_DATE_TIME));
      }
    }

    Date now = new Date();
    long creationDateMillies = now.getTime();
    String creationDate = DateUtils.sdf.format(now);
    task.setCreationDate(creationDate);

    //TODO: needs some work for more complicated variable structures
    List<TaskVariable> variables = new ArrayList<>();
    for(Map.Entry<String, Object> variable : job.getVariablesAsMap().entrySet()) {
      if (variable.getValue() instanceof String) {
        if(variable.getKey().equals("businessKey")) {
          task.setBusinessKey(variable.getValue().toString());
        }
        variables.add(
            TaskVariable.builder()
                .id(job.getProcessInstanceKey() + "-" + variable.getKey())
                .name(variable.getKey())
                .value(variable.getValue().toString())
                .build());
      }
    }

    task.setVariables(variables);
    task.setCache(true);

    Integer completeTaskMillis = RandomNumberUtils.getRandom(
        taskListService.getMinCompleteTaskMillis(),
        taskListService.getMaxCompleteTaskMillis()
    );
    task.setSimulatedCompletionDateTime(completeTaskMillis+creationDateMillies);

    taskListService.saveTask(task);

    // !!! The name of the bpmn file in the "src/main/resources/models" directory must match the
    // process id in order for this to work!
    //String taskName = bpmnService.getUserTask(processDefinitionKey, taskActivityId);
    //task.setName(taskName);

    //SseEmitterManager.broadcast(task);

    logger.debug("User Task Worker Handler complete");

  }

}
