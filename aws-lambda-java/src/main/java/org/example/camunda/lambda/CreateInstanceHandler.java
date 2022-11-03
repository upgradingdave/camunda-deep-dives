package org.example.camunda.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateInstanceHandler implements RequestHandler {

  @Override
  public Object handleRequest(Object input, Context context) {

    ZeebeService zeebeService = new ZeebeService(ZeebeService.fromAppConstants());
    return zeebeService.createInstance();
  }
}
