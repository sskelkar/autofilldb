package autofilldb;

import java.util.Random;

import static autofilldb.Util.isNullOrEmpty;
import static java.lang.Integer.parseInt;

public class IntValue {
  private final String type;
  private final String key;
  private final String defaultValue;

  public IntValue(String type, String key, String defaultValue) {
    this.type = type;
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public Integer value() {
   return defaultValue == null ? generateValue(key) : parseInt(defaultValue);
  }

  private Integer generateValue(String key) {
   return isNullOrEmpty(key) ? 1 : generateKeyForConstraints(key);
  }

  private Integer generateKeyForConstraints(String key) {
    if (key.equals("PRI") || key.equals("UNI")) {
      return new Random().nextInt();
    }

    return 1;
  }
}
