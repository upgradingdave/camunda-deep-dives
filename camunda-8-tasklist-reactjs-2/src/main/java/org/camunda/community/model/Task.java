package org.camunda.community.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public class Task {

  private String id;

  private Long jobKey;

  private String name;

  private String processId;

  private String processInstanceKey;

  private String processName;

  private String tenantId;

  private String assignee;

  private String creationDate;

  private String completionDate;

  private String taskState;

  private List<String> candidateGroups;

  private List<String> candidateUsers;

  private List<String> sortValues;

  private Boolean isFirst;

  private List<TaskVariable> variables;

  private String formKey;

  private String formId;

  private Long formVersion;

  private Boolean isFormEmbedded;

  private String processDefinitionKey;

  private String taskDefinitionId;

  private OffsetDateTime dueDate;

  private OffsetDateTime followUpDate;

  private String implementation;

  private Integer priority;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProcessInstanceKey() {
    return processInstanceKey;
  }

  public void setProcessInstanceKey(String processInstanceKey) {
    this.processInstanceKey = processInstanceKey;
  }

  public String getProcessName() {
    return processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  public String getAssignee() {
    return assignee;
  }

  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getTaskState() {
    return taskState;
  }

  public void setTaskState(String taskState) {
    this.taskState = taskState;
  }

  public List<String> getSortValues() {
    return sortValues;
  }

  public void setSortValues(List<String> sortValues) {
    this.sortValues = sortValues;
  }

  public Boolean getIsFirst() {
    return isFirst;
  }

  public void setIsFirst(Boolean isFirst) {
    this.isFirst = isFirst;
  }

  public List<TaskVariable> getVariables() {
    return variables;
  }

  public void setVariables(List<TaskVariable> variables) {
    this.variables = variables;
  }

  public List<String> getCandidateGroups() {
    return candidateGroups;
  }

  public void setCandidateGroups(List<String> candidateGroups) {
    this.candidateGroups = candidateGroups;
  }

  public String getFormKey() {
    return formKey;
  }

  public void setFormKey(String formKey) {
    this.formKey = formKey;
  }

  public String getFormId() {
    return formId;
  }

  public void setFormId(String formId) {
    this.formId = formId;
  }

  public Long getFormVersion() {
    return formVersion;
  }

  public void setFormVersion(Long formVersion) {
    this.formVersion = formVersion;
  }

  public Boolean getIsFormEmbedded() {
    return isFormEmbedded;
  }

  public void setIsFormEmbedded(Boolean isFormEmbedded) {
    this.isFormEmbedded = isFormEmbedded;
  }

  public String getProcessDefinitionKey() {
    return processDefinitionKey;
  }

  public void setProcessDefinitionKey(String processDefinitionKey) {
    this.processDefinitionKey = processDefinitionKey;
  }

  public Long getJobKey() {
    return jobKey;
  }

  public void setJobKey(Long jobKey) {
    this.jobKey = jobKey;
  }

  public String getTaskDefinitionId() {
    return taskDefinitionId;
  }

  public void setTaskDefinitionId(String taskDefinitionId) {
    this.taskDefinitionId = taskDefinitionId;
  }

  public OffsetDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(OffsetDateTime dueDate) {
    this.dueDate = dueDate;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getCompletionDate() {
    return completionDate;
  }

  public void setCompletionDate(String completionDate) {
    this.completionDate = completionDate;
  }

  public List<String> getCandidateUsers() {
    return candidateUsers;
  }

  public void setCandidateUsers(List<String> candidateUsers) {
    this.candidateUsers = candidateUsers;
  }

  public Boolean getFirst() {
    return isFirst;
  }

  public void setFirst(Boolean first) {
    isFirst = first;
  }

  public Boolean getFormEmbedded() {
    return isFormEmbedded;
  }

  public void setFormEmbedded(Boolean formEmbedded) {
    isFormEmbedded = formEmbedded;
  }

  public OffsetDateTime getFollowUpDate() {
    return followUpDate;
  }

  public void setFollowUpDate(OffsetDateTime followUpDate) {
    this.followUpDate = followUpDate;
  }

  public String getImplementation() {
    return implementation;
  }

  public void setImplementation(String implementation) {
    this.implementation = implementation;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public String getProcessId() {
    return processId;
  }

  public void setProcessId(String processId) {
    this.processId = processId;
  }
}
