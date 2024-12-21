package org.camunda.community.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

  private String businessKey;

  private Boolean cache = false;

  @Id
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

  @OneToMany
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

}
