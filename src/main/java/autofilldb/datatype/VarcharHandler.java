package autofilldb.datatype;

import autofilldb.ColumnDefinition;

import java.util.Random;

import static autofilldb.Util.isNullOrEmpty;
import static java.lang.Integer.parseInt;
import static java.util.UUID.randomUUID;

public class VarcharHandler implements DataTypeHandler<String> {

  @Override
  public boolean canHandle(String dataType) {
    return dataType.toLowerCase().startsWith("varchar");
  }

  @Override
  public String value(ColumnDefinition def) {
    String value = isConstraintPresent(def.getConstraint()) ?
        generateValue(def) :
        fillerValue(def.getDefaultValue());
    return "'" + value + "'";
  }

  private String generateValue(ColumnDefinition def) {
    String randomString = randomUUID().toString();
    int columnSize = parseInt(def.getDataType().replace("varchar(", "").replace(")", ""));
    return randomString.length() > columnSize ? randomString.substring(0, columnSize) : randomString;
  }

  private String fillerValue(String defaultValue) {
    return isNullOrEmpty(defaultValue) ? "a" : defaultValue;
  }

  private boolean isConstraintPresent(String constraint) {
    return !isNullOrEmpty(constraint) && (constraint.equals("PRI") || constraint.equals("UNI"));
  }
}
