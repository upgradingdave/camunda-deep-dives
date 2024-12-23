package org.camunda.community.services;

import org.camunda.community.model.Task;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository
    extends CrudRepository<Task, String> {

  List<Task> findTasksByBusinessKey(String businessKey);

  void deleteTaskByBusinessKey(String businessKey);

  List<Task> findTasksByAssignee(String assignee);

  Task findTaskById(String id);
}
