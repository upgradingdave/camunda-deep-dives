package org.camunda.community;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "camunda.client")
public class CamundaConfig {

  public static final String MESSAGE_START = "Message_HighPerfUserTaskProcessStart";

  private String clientId;
  private String clientSecret;
  private String clusterId;
  private String region;
  private String mode;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getOAuthApi() {
    if(mode.equals("saas")) {
      return "https://login.cloud.camunda.io/oauth/token";
    } else {
      throw new RuntimeException("Unsupported mode: " + mode);
    }
  }

  public String getZeebeGrpc() {
    if(mode.equals("saas")) {
      return "grpcs://" + getClusterId() + "." + getRegion() + ".zeebe.camunda.io";
    } else {
      throw new RuntimeException("Unsupported mode: " + mode);
    }
  }

  public String getZeebeRest() {
    if(mode.equals("saas")) {
      return "https://" + getRegion() + ".zeebe.camunda.io/" + getClusterId() + ".zeebe.camunda.io:443";
    } else {
      throw new RuntimeException("Unsupported mode: " + mode);
    }
  }

  public String getTasklistUrl() {
    if(mode.equals("saas")) {
      return "https://" + getRegion() + ".tasklist.camunda.io/" + getClusterId();
    } else {
      throw new RuntimeException("Unsupported mode: " + mode);
    }
  }

  public String getZeebeAudience() {
    return "zeebe.camunda.io";
  }

  public String getTaskListAudience() {
    return "tasklist.camunda.io";
  }

  public String getProcessId() {
    return "Process_1bzkc1f";
  }

}
