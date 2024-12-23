package org.camunda.community.services;

import org.camunda.community.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CacheManager {

  private static Logger log = LoggerFactory.getLogger(CacheManager.class);

  private final TaskRepository taskRepository;
  private final TaskListRestClient taskListRestClient;

  public CacheManager(TaskRepository taskRepository, TaskListRestClient taskListRestClient) {
    this.taskRepository = taskRepository;
    this.taskListRestClient = taskListRestClient;
  }

  @Scheduled(fixedDelay = 30000, initialDelay = 30000)
  public void clearCache() {

    List<Task> restTasks = taskListRestClient.findCreatedTasks();
    for (Task task : restTasks) {
      taskRepository.deleteById(task.getId());
    }

  }

}
