package org.example.camunda.lambda;

public class AppConstants {
  final static String ZEEBE_CLUSTER_ID = "";
  final static String ZEEBE_CLIENT_ID = "";
  final static String ZEEBE_CLIENT_SECRET = "";
  final static String ZEEBE_REGION = "bru-2";
  final static String ZEEBE_AUTHORIZATION_SERVER_URL = "https://login.cloud.camunda.io/oauth/token";
  final static String ZEEBE_ADDRESS = ZEEBE_CLUSTER_ID + "." + ZEEBE_REGION + ".zeebe.camunda.io:443";
}
