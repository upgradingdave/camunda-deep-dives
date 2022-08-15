/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Zeebe Community License 1.1. You may not use this file
 * except in compliance with the Zeebe Community License 1.1.
 */
package org.example.camunda.process.solution.model;

import java.util.HashMap;
import java.util.Map;

public final class ProcessSolutionResponse {

  public String responseType;
  public Map<String, Object> variables;
  public Object result;
  public Object error;

  public void setResponseType(String responseType) {
    this.responseType = responseType;
  }

  public void setVariables(Map<String, Object> variables) {
    this.variables = variables;
  }

  public void setResult(final Object result) {
    this.result = result;
  }

  public void setError(final Object error) {
    this.error = error;
  }

  public static ProcessSolutionResponse withResult(
      String responseType, Map<String, Object> processVariables, Object result) {
    final var response = new ProcessSolutionResponse();
    response.setResponseType(responseType);
    response.setVariables(processVariables);
    response.setResult(result);
    return response;
  }

  public static ProcessSolutionResponse withError(String errorType, Exception error) {
    Map<String, Object> errorInfo = new HashMap<>();
    errorInfo.put("errorType", errorType);
    errorInfo.put("errorMessage", error.getMessage());
    final var response = new ProcessSolutionResponse();
    response.setError(errorInfo);
    return response;
  }

  @Override
  public String toString() {
    return "ProcessSolutionResponse{" + "result=" + result + ", error='" + error + '\'' + '}';
  }
}
