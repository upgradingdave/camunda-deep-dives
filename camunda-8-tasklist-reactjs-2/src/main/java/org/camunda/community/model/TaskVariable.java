package org.camunda.community.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskVariable {

  @Id
  private String id;
  private String name;
  private String value;
  private Boolean isValueTruncated;
  private String previewValue;
  @OneToOne
  private TaskVariable draft;
  private String tenantId;

}
