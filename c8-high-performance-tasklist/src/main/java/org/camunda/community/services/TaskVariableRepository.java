package org.camunda.community.services;

import org.camunda.community.model.TaskVariable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskVariableRepository
    extends CrudRepository<TaskVariable, String> {
}
