package autofilldb.datatype;

import autofilldb.ColumnDefinition;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static autofilldb.Util.isNullOrEmpty;

public class DatetimeHandler implements DataTypeHandler<String> {
  private static final LocalDateTime REFERENCE_DATE = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public boolean canHandle(String dataType) {
    return dataType.toLowerCase().equals("datetime");
  }

  @Override
  public String value(ColumnDefinition def) {

    String value = isConstraintPresent(def.getConstraint()) ?
        generateValue() :
        fillerValue(def.getDefaultValue());

    return "'" + value + "'";
  }

  private String generateValue() {
    int offset = new Random().nextInt();
    return REFERENCE_DATE.plusSeconds(offset).format(FORMATTER);
  }

  private String fillerValue(String defaultValue) {
    return isNullOrEmpty(defaultValue) ? "2001-01-19 03:14:07" : defaultValue;
  }

  private boolean isConstraintPresent(String constraint) {
    return !isNullOrEmpty(constraint) && (constraint.equals("PRI") || constraint.equals("UNI"));
  }
}
