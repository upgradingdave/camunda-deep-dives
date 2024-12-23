package org.camunda.community.utils;

import java.util.Random;

public class RandomNumberUtils {

  public static Integer getRandom(Integer min, Integer max) {
    Random random = new Random();

    // get rand number between 0 and max - min + 1
    int randomNumber = random.nextInt(max - min + 1);

    // shift the range
    randomNumber += min;
    return randomNumber;
  }

}
