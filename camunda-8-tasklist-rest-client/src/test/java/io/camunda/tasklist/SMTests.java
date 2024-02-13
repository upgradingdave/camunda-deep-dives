package io.camunda.tasklist;

import io.camunda.tasklist.auth.JWTAuthentication;
import io.camunda.tasklist.exception.TaskListException;
import io.camunda.tasklist.exception.TaskListRestException;
import io.camunda.zeebe.client.CredentialsProvider;
import io.camunda.zeebe.client.impl.ZeebeClientBuilderImpl;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import org.junit.Ignore;

@Ignore
public class SMTests extends TaskListRestApiTests {

  public SMTests() {

    this.props = loadProps("test.sm.properties");

    CredentialsProvider credentialsProvider = new OAuthCredentialsProviderBuilder()
        .authorizationServerUrl(props.getProperty("authorizationUrl"))
        .clientId(props.getProperty("zeebeClientId"))
        .clientSecret(props.getProperty("zeebeClientSecret"))
        .audience("zeebe-api")
        .build();

    this.zeebeClient = new ZeebeClientBuilderImpl()
        .gatewayAddress(props.getProperty("zeebeGatewayAddress"))
        .credentialsProvider(credentialsProvider)
        .build();

    JWTAuthentication jwtAuthentication = new JWTAuthentication(
        props.getProperty("authorizationUrl"),
        props.getProperty("clientId"),
        props.getProperty("clientSecret"),
        props.getProperty("contentType")
    );

    taskListRestClient =
        new TaskListRestClient(jwtAuthentication, props.getProperty("taskListBaseUrl"));

    zeebeStatus();
    deployProcess();
    try {
      setup();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
