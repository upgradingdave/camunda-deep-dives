package org.example.camunda.process.solution;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProcessVariables {

  private String businessKey;
  private Boolean result;
  private String greeting;

  public String getBusinessKey() {
    return businessKey;
  }

  public ProcessVariables setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
    return this;
  }

  public Boolean getResult() {
    return result;
  }

  public ProcessVariables setResult(Boolean result) {
    this.result = result;
    return this;
  }

  public String getGreeting() {
    return greeting;
  }

  public void setGreeting(String greeting) {
    this.greeting = greeting;
  }
}
