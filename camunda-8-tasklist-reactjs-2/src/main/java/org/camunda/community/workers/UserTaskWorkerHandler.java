package org.camunda.community.workers;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserTaskWorkerHandler implements JobHandler {

  static Logger logger = LoggerFactory.getLogger(UserTaskWorker.class);

  @Override
  public void handle(JobClient client, ActivatedJob job) throws Exception {

    Map<String, Object> variables = job.getVariablesAsMap();

    logger.info("User Task Worker Handler triggered with variables: " + variables);

    /*
    Task task = new Task();

    task.setVariables(variables);
    task.setProcessInstanceKey(String.valueOf(job.getProcessInstanceKey()));
    String processDefinitionKey = Long.toString(job.getProcessDefinitionKey());
    task.setProcessDefinitionKey(processDefinitionKey);

    String formKey = headers.get("io.camunda.zeebe:formKey");
    task.setFormKey(formKey);
    if (headers.containsKey("io.camunda.zeebe:dueDate")) {
      task.setDueDate(
          OffsetDateTime.parse(
              headers.get("io.camunda.zeebe:dueDate"), DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    String creationTime = sdf.format(new Date());
    task.setCreationDate(creationTime);

    // since 8.1.5, the taskId and jobKey are the same...
    String jobKey = Long.toString(job.getKey());
    task.setId(jobKey);
    task.setJobKey(jobKey);

    // TODO: is this used anymore?
    // String taskId = Long.toString(job.getElementInstanceKey());

    task.setTaskDefinitionId(job.getElementId());
    String bpmnProcessId = job.getBpmnProcessId();
    task.setProcessName(bpmnService.getProcessInstance(bpmnProcessId).get(0).getProcessName());

    String taskActivityId = job.getElementId();

    // !!! The name of the bpmn file in the "src/main/resources/models" directory must match the
    // process id in order for this to work!
    //String taskName = bpmnService.getUserTask(processDefinitionKey, taskActivityId);
    //task.setName(taskName);

    if (!job.getCustomHeaders().isEmpty()) {
      if (job.getCustomHeaders().containsKey("io.camunda.zeebe:assignee")) {
        task.setAssignee(job.getCustomHeaders().get("io.camunda.zeebe:assignee"));
      }
      if (job.getCustomHeaders().containsKey("io.camunda.zeebe:candidateGroups")) {
        String groups = job.getCustomHeaders().get("io.camunda.zeebe:candidateGroups");
        task.setCandidateGroups(
            JSONUtils.toParameterizedObject(groups, new TypeReference<List<String>>() {}));
      }
    }

    String taskState = TaskState.TASK_CREATED;
    task.setTaskState(taskState);
    //SseEmitterManager.broadcast(task);

  } catch (Exception e) {
    logger.error("Exception occured in UserTaskWorker", e);
    client
        .newFailCommand(job.getKey())
        .retries(0)
        .errorMessage("Exception occured in UserTaskWorker - " + e.getMessage())
        .send();
  }
     */

  }
}
