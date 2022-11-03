package org.example.camunda.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;

public class CreateInstanceHandler implements RequestHandler {

  @Override
  public Object handleRequest(Object input, Context context) {

    Map<String, Object> variables =
        (Map<String, Object>) JsonUtils.toObject((String) input, Map.class);
    ZeebeService zeebeService = new ZeebeService(ZeebeService.fromAppConstants());
    return zeebeService.createInstance(variables);
  }
}
