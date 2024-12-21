package org.camunda.community.services;

import org.camunda.community.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository
    extends CrudRepository<Task, Long> {
  List<Task> getTasksByBusinessKey(String businessKey);
  void deleteTaskByBusinessKey(String businessKey);
}
