package org.camunda.community.services.tasklist;

import com.google.rpc.context.AttributeContext;
import org.camunda.community.CamundaConfig;
import org.camunda.community.model.TokenResponse;
import org.camunda.community.services.AuthService;
import org.camunda.community.services.TaskListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AuthServiceTest {

  private AuthService authService;
  private CamundaConfig camundaConfig;

  @Autowired
  public AuthServiceTest(CamundaConfig camundaConfig, AuthService authService) {
    this.camundaConfig = camundaConfig;
    this.authService = authService;
  }

  @Test
  public void getBearerToken() throws Exception {
    TokenResponse tokenResponse = authService.getBearerToken(
        camundaConfig.getClientId(), camundaConfig.getClientSecret(), camundaConfig.getZeebeAudience()
    );
    assertNotNull(tokenResponse);

    String token = tokenResponse.getAccessToken();
    assertNotNull(token);

    assertFalse(tokenResponse.isExpired());
  }

}
