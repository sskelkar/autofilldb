package autofilldb;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static autofilldb.Util.isNullOrEmpty;

public class DatetimeValue {
  private static final LocalDateTime REFERENCE_DATE = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final String type;
  private final String key;
  private final String defaultValue;

  public DatetimeValue(String type, String key, String defaultValue) {

    this.type = type;
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public String value() {
    String value = isNullOrEmpty(key) ? fillerValue() : generateKeyForConstraints(key);
    return "'" + value + "'";
  }

  private String generateKeyForConstraints(String key) {
    if (key.equals("PRI") || key.equals("UNI")) {
      int offset = new Random().nextInt();
      return REFERENCE_DATE.plusSeconds(offset).format(FORMATTER);
    }

    return fillerValue();
  }

  private String fillerValue() {
    return isNullOrEmpty(defaultValue) ? "2001-01-19 03:14:07" : defaultValue;
  }
}
