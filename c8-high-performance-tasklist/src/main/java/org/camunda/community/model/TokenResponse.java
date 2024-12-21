package org.camunda.community.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class TokenResponse {

  @JsonProperty("access_token")
  String accessToken;

  @JsonProperty("expires_in")
  Integer expiresIn;

  // This is null from SaaS
  @JsonProperty("refresh_expires_in")
  Integer refreshExpiresIn;

  @JsonProperty("token_type")
  String tokenType;

  // This is null from SaaS
  @JsonProperty("not-before-policy")
  Integer notBeforePolicy;

  String scope;

  Timestamp createTimestamp;

  public TokenResponse() {
    this.createTimestamp = new Timestamp(System.currentTimeMillis());
  }

  public boolean isExpired() {
    //TODO: this can be improved, but for now, just refresh the token every 5 mins
    Timestamp fiveMinsAgo = new Timestamp(System.currentTimeMillis() - (60 * 5 * 1000));
    return fiveMinsAgo.getTime() >= this.createTimestamp.getTime();
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  public Integer getRefreshExpiresIn() {
    return refreshExpiresIn;
  }

  public void setRefreshExpiresIn(Integer refreshExpiresIn) {
    this.refreshExpiresIn = refreshExpiresIn;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public Integer getNotBeforePolicy() {
    return notBeforePolicy;
  }

  public void setNotBeforePolicy(Integer notBeforePolicy) {
    this.notBeforePolicy = notBeforePolicy;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}
