package autofilldb.datatype;

import autofilldb.ColumnDefinition;

import java.util.Random;

import static autofilldb.Util.isNullOrEmpty;
import static java.lang.Integer.parseInt;

public class IntegerHandler implements DataTypeHandler<Integer> {

  @Override
  public boolean canHandle(String dataType) {
    return dataType.toLowerCase().startsWith("int");
  }

  @Override
  public Integer value(ColumnDefinition def) {

    return isConstraintPresent(def.getConstraint()) ?
        generateValue() :
        fillerValue(def.getDefaultValue());
  }

  private Integer generateValue() {
      return new Random().nextInt();
  }

  private int fillerValue(String defaultValue) {
    return isNullOrEmpty(defaultValue) ? 1 : parseInt(defaultValue);
  }

  private boolean isConstraintPresent(String constraint) {
    return !isNullOrEmpty(constraint) && (constraint.equals("PRI") || constraint.equals("UNI"));
  }
}
