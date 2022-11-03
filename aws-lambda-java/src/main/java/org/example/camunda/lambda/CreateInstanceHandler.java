package org.example.camunda.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;

public class CreateInstanceHandler implements RequestHandler<Map<String, Object>, String> {

  @Override
  public String handleRequest(Map<String, Object> variables, Context context) {
    ZeebeService zeebeService = new ZeebeService(ZeebeService.fromAppConstants());
    return zeebeService.createInstance(variables);
  }
}
