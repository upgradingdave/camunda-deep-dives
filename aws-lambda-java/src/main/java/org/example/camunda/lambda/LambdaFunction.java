package org.example.camunda.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;

public class LambdaFunction implements RequestHandler<Map<String, Object>, Map<String, Object>> {

  private Map<String, Object> bookHotel() {
    Map<String, Object> result = new HashMap<>();
    result.put("bookHotelResult", true);
    return result;
  }

  @Override
  public Map<String, Object> handleRequest(Map<String, Object> variables, Context context) {

    String eventType = (String) variables.get("event");

    if (eventType != null) {
      if (eventType.equals("createInstance")) {

        ZeebeService zeebeService = new ZeebeService(ZeebeService.fromAppConstants());
        long processInstanceKey = zeebeService.createInstance(variables);

        // NOTE: the result isn't used by the process
        Map<String, Object> result = new HashMap<>();
        result.put("lambdaFunctionResult", processInstanceKey);
        return result;

      } else if (eventType.equals("bookHotel")) {

        return bookHotel();
      }
    }

    return null;
  }
}
