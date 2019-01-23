package autofilldb;

import java.util.Random;
import java.util.UUID;

import static autofilldb.Util.isNullOrEmpty;
import static java.lang.Integer.parseInt;
import static java.util.UUID.randomUUID;

public class VarcharValue {
  private int size;
  private final String key;
  private final String defaultValue;

  public VarcharValue(String type, String key, String defaultValue) {
    this.size = parseInt(type.replace("varchar(", "").replace(")", ""));
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public String value() {
    String value = isNullOrEmpty(key) ? fillerValue() : generateKeyForConstraints(key);
    return "'" + value + "'";
  }

  private String generateKeyForConstraints(String key) {
    if (key.equals("PRI") || key.equals("UNI")) {
      String randomString = randomUUID().toString();
      return randomString.length() > size ? randomString.substring(0, size) : randomString;
    }

    return fillerValue();
  }

  private String fillerValue() {
    return isNullOrEmpty(defaultValue) ? "a" : defaultValue;
  }
}
