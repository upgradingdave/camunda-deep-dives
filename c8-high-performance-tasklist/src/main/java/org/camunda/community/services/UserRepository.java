package org.camunda.community.services;

import org.camunda.community.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
    extends CrudRepository<Person, Long> {
  Person findUserByUsername(String username);
}
