package org.example.camunda.process.solution.service;

import io.camunda.tasklist.entities.TaskEntity;
import io.camunda.tasklist.schema.templates.TaskTemplate;
import io.camunda.tasklist.store.TaskStore;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(TasklistExtraProperties.class)
public class TasklistExtraService {

  private final TasklistExtraProperties properties;

  @Autowired private TaskStore taskStore;

  public TasklistExtraService(TasklistExtraProperties properties) {
    this.properties = properties;
  }

  public TaskEntity getTask(String taskId) {
    return taskStore.getTask(taskId);
  }

  public TaskEntity setDueDate(String taskId, OffsetDateTime dueDate) {
    final TaskEntity taskBefore = getTask(taskId);

    Map<String, Object> fields = new HashMap<>();
    fields.put(TaskTemplate.DUE_DATE, dueDate);

    taskStore.updateTask(taskBefore.getId(), fields);

    return getTask(taskId);
  }

  public TaskEntity setFollowUpDate(String taskId, OffsetDateTime followUpDate) {
    final TaskEntity taskBefore = getTask(taskId);

    Map<String, Object> fields = new HashMap<>();
    fields.put(TaskTemplate.FOLLOW_UP_DATE, followUpDate);

    taskStore.updateTask(taskBefore.getId(), fields);

    return getTask(taskId);
  }
}
