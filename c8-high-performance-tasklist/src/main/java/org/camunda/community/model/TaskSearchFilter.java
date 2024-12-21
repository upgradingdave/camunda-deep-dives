package org.camunda.community.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSearchFilter {

  String state;
  String processDefinitionKey;
  Boolean assigned;
  String assignee;
  List<TaskVariableFilter> taskVariables;

}
