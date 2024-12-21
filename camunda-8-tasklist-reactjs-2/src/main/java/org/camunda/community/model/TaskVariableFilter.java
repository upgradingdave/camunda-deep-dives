package org.camunda.community.model;

public class TaskVariableFilter {

  String name;
  String value;
  String operator;

  public TaskVariableFilter(String name, String value) {
    this.name = name;
    this.value = value;
    this.operator = "eq";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }
}
