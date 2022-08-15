package org.example.camunda.process.solution.model;

import java.util.Map;

public class ProcessSolutionRequest {

  public Object key;
  public Map<String, Object> processVariables;

  @Override
  public String toString() {
    return "ProcessSolutionRequest{"
        + "='"
        + '\''
        + ", processId or jobKey="
        + key
        + ", variables="
        + processVariables
        + '}';
  }

  public Object getKey() {
    return key;
  }

  public Map<String, Object> getVariables() {
    return processVariables;
  }
}
