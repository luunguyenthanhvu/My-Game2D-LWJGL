package vuluu.nlu.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Time {

  static float timeStarted = System.nanoTime();

  public static float getTime() {
    return (float) ((System.nanoTime() - timeStarted) * 1E-9);
  }
}
