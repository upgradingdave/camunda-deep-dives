package org.example.camunda.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;

public class BookHotelHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

  @Override
  public Map<String, Object> handleRequest(Map<String, Object> payload, Context context) {
    Map<String, Object> result = new HashMap<>();
    result.put("bookHotelResult", true);
    return result;
  }
}
