package io.camunda.tasklist.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils<T> {

  public static String toJson(Object object) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(object);
  }

  public T fromJson(String json, Class clazz) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
    return objectMapper.readValue(json, javaType);
  }

}
