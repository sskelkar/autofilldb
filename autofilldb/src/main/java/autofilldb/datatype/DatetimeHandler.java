package autofilldb.datatype;

import autofilldb.ColumnDefinition;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static autofilldb.Util.isNullOrEmpty;

public final class DatetimeHandler implements DataTypeHandler<String> {
  private static final LocalDateTime REFERENCE_DATE = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public boolean canHandle(String dataType) {
    return dataType.toLowerCase().equals("datetime");
  }

  @Override
  public String uniqueValue(ColumnDefinition def) {
    int offset = new Random().nextInt();
    String value = REFERENCE_DATE.plusSeconds(offset).format(FORMATTER);

    return enQuote(value);
  }

  @Override
  public String value(String defaultValue) {
    String value = isNullOrEmpty(defaultValue) ? "2001-01-19 03:14:07" : defaultValue;
    return enQuote(value);
  }

  private String enQuote(String value) {
    return "'" + value + "'";
  }
}
