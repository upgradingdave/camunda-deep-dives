package org.camunda.community.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

  public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

  public static String stringFromDate() {
    return stringFromDate(new Date());
  }

  public static String stringFromDate(Date date) {
    return sdf.format(date == null ? new Date() : date);
  }

  public static Date dateFromString(String date) throws ParseException {
    return sdf.parse(date);
  }

  public static long toMillis(Date date) {
    return date.getTime();
  }

  public static long toMillis(String date) throws ParseException {
    return toMillis(dateFromString(date));
  }
}
