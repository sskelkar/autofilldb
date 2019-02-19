package com.github.sskelkar.autofilldb;

import static com.github.sskelkar.autofilldb.Util.isNullOrEmpty;
import static java.lang.Integer.parseInt;

final class BitHandler implements DataTypeHandler<Integer> {

  @Override
  public boolean canHandle(String dataType) {
    return dataType.toLowerCase().startsWith("bit");
  }

  @Override
  public Integer uniqueValue(ColumnDefinition def) {
    throw new UnsupportedOperationException("Bit type cannot have unique value");
  }

  @Override
  public Integer value(String defaultValue) {
    return isNullOrEmpty(defaultValue) ? 0 : parseInt(defaultValue.substring(2,3));
  }
}
