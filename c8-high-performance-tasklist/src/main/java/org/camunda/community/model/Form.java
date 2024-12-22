package org.camunda.community.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Form {

  private String id;
  private String processDefinitionKey;
  private String title;
  private Integer version;
  private String tenantId;
  private Boolean isDeleted;

}
