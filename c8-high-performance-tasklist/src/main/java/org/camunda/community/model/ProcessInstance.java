package org.camunda.community.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProcessInstance {

  private String businessKey;
  private String creationDate;

}
