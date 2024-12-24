package org.camunda.community.services.tasklist;

import org.camunda.community.simulation.SimulatorConfig;
import org.camunda.community.utils.DateUtils;
import org.camunda.community.utils.RandomNumberUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatorTest {

  @Test
  public void testRandomCompletTaskMillis() {
    int min = 3000;
    int max = 10000;
    for (int i = 0; i < 100; i++) {
      assertTrue(
          RandomNumberUtils.getRandom(min, max) > min
              && RandomNumberUtils.getRandom(min, max) < max);
    }
  }

  @Test
  public void testDateUtils() throws ParseException {

    //String original = "2024-12-23 12:17:53 -0500";
    String original = "2024-12-23T17:46:10.729+0000";
    String expected = "2024-12-23T12:46:10.729-0500";
    Date date = DateUtils.dateFromString(original);
    String dateStr = DateUtils.stringFromDate(date);
    assertEquals(expected, dateStr);

  }


}
