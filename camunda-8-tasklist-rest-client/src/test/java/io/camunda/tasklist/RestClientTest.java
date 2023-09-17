package io.camunda.tasklist;

import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static org.junit.Assert.assertTrue;

public class RestClientTest {

  final static String TASKLIST_BASE_URL="https://jfk-1.tasklist.camunda.io/bccd8692-5e2d-4599-b29e-0988bd5a14a4";
  final static String TASKS_SEARCH_ENDPOINT = TASKLIST_BASE_URL + "/v1/tasks/search";

  @Test
  public void sanity() {
    assertTrue(true);
  }

  @Test
  public void testConnection() {

  }
}
