package org.camunda.community.model;

import java.util.ArrayList;
import java.util.List;

public class TaskSearchFilter {

  String state;
  String processDefinitionKey;
  Boolean assigned;
  String assignee;
  List<TaskVariableFilter> taskVariables;

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public TaskSearchFilter state(String state) {
    this.state = state;
    return this;
  }

  public Boolean getAssigned() {
    return assigned;
  }

  public void setAssigned(Boolean assigned) {
    this.assigned = assigned;
  }

  public TaskSearchFilter assigned(Boolean assigned) {
    this.assigned = assigned;
    return this;
  }

  public String getAssignee() {
    return assignee;
  }

  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  public TaskSearchFilter assignee(String assignee) {
    this.assignee = assignee;
    return this;
  }

  public List<TaskVariableFilter> getTaskVariables() {
    return taskVariables;
  }

  public void setTaskVariables(List<TaskVariableFilter> variables) {
    this.taskVariables = variables;
  }

  public TaskSearchFilter taskVariableFilter(String name, String value) {
    if(this.taskVariables == null) {
      this.taskVariables = new ArrayList<>();
    }
    this.taskVariables.add(new TaskVariableFilter(name, value));
    return this;
  }

  public String getProcessDefinitionKey() {
    return processDefinitionKey;
  }

  public void setProcessDefinitionKey(String processDefinitionKey) {
    this.processDefinitionKey = processDefinitionKey;
  }

  public TaskSearchFilter processDefinitionKey(String processDefinitionKey) {
    this.processDefinitionKey = processDefinitionKey;
    return this;
  }
}
