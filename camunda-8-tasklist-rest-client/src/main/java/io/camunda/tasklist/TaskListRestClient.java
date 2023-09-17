package io.camunda.tasklist;

import io.camunda.tasklist.auth.Authentication;
import io.camunda.tasklist.dto.AccessTokenResponse;
import io.camunda.tasklist.exception.TaskListException;

public class TaskListRestClient {

  Authentication authentication;
  AccessTokenResponse accessTokenResponse;

  public TaskListRestClient(Authentication authentication) {
    this.authentication = authentication;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public void setAuthentication(Authentication authentication) {
    this.authentication = authentication;
  }

  public AccessTokenResponse getAccessTokenResponse() {
    return accessTokenResponse;
  }

  public void setAccessTokenResponse(AccessTokenResponse accessTokenResponse) {
    this.accessTokenResponse = accessTokenResponse;
  }

  public void authenticate() throws TaskListException {
    this.authentication.authenticate(this);
  }
}
