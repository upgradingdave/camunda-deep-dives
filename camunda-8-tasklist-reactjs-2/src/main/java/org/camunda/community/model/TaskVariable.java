package org.camunda.community.model;

public class TaskVariable {

  private String id;
  private String name;
  private String value;
  private Boolean isValueTruncated;
  private String previewValue;
  private TaskVariable draft;
  private String tenantId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TaskVariable id(String id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TaskVariable name(String name) {
    this.name = name;
    return this;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public TaskVariable value(String value) {
    this.value = value;
    return this;
  }

  public Boolean getValueTruncated() {
    return isValueTruncated;
  }

  public void setValueTruncated(Boolean valueTruncated) {
    isValueTruncated = valueTruncated;
  }

  public TaskVariable isValueTruncated(Boolean isValueTruncated) {
    this.isValueTruncated = isValueTruncated;
    return this;
  }

  public String getPreviewValue() {
    return previewValue;
  }

  public void setPreviewValue(String previewValue) {
    this.previewValue = previewValue;
  }

  public TaskVariable previewValue(String previewValue) {
    this.previewValue = previewValue;
    return this;
  }

  public TaskVariable getDraft() {
    return draft;
  }

  public void setDraft(TaskVariable draft) {
    this.draft = draft;
  }

  public TaskVariable draft(TaskVariable draft) {
    this.draft = draft;
    return this;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public TaskVariable tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }
}
