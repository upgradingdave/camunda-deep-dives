package io.camunda.tasklist;

import io.camunda.tasklist.auth.JWTAuthentication;
import io.camunda.zeebe.client.ZeebeClient;
import org.junit.Ignore;


@Ignore
public class SaaSTests extends TaskListRestApiTests {

  public SaaSTests() {
    super();
    this.props = loadProps("test.saas.properties");

    this.zeebeClient = ZeebeClient.newCloudClientBuilder()
        .withClusterId(props.getProperty("zeebeClusterId"))
        .withClientId(props.getProperty("zeebeClientId"))
        .withClientSecret(props.getProperty("zeebeClientSecret"))
        .withRegion(props.getProperty("zeebeRegion"))
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
