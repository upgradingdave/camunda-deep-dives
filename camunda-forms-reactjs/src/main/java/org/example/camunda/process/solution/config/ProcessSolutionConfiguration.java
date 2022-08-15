package org.example.camunda.process.solution.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties(prefix = "process.solution")
public class ProcessSolutionConfiguration {

  private String allowedOriginPatterns;

  public Resource getBpmnDefinitionResource() {
    return bpmnDefinitionResource;
  }

  public void setBpmnDefinitionResource(Resource bpmnDefinitionResource) {
    this.bpmnDefinitionResource = bpmnDefinitionResource;
  }

  private Resource bpmnDefinitionResource;

  public String getAllowedOriginPatterns() {
    return allowedOriginPatterns;
  }

  public void setAllowedOriginPatterns(String allowedOriginPatterns) {
    this.allowedOriginPatterns = allowedOriginPatterns;
  }
}
