package org.example.camunda.lambda;

public class AppConstants {

  static final String BASE_ADDRESS = "zeebe.camunda.io";
  static final String BASE_AUTH_URL = "https://login.cloud.camunda.io/oauth/token";
  static final String DEFAULT_REGION = "bru-2";
  static final String ZEEBE_CLUSTER_ID = "";
  static final String ZEEBE_CLIENT_ID = "";
  static final String ZEEBE_CLIENT_SECRET = "";

  static final String ZEEBE_ADDRESS =
      ZEEBE_CLUSTER_ID + "." + DEFAULT_REGION + "." + BASE_ADDRESS + "443";
}
