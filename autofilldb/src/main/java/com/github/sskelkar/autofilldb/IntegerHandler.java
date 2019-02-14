package com.github.sskelkar.autofilldb;

import java.util.Random;

import static com.github.sskelkar.autofilldb.Util.isNullOrEmpty;
import static java.lang.Integer.parseInt;

final class IntegerHandler implements DataTypeHandler<Integer> {

  @Override
  public boolean canHandle(String dataType) {
    return dataType.toLowerCase().startsWith("int");
  }

  @Override
  public Integer uniqueValue(ColumnDefinition def) {
    return new Random().nextInt();
  }

  @Override
  public Integer value(String defaultValue) {
    return isNullOrEmpty(defaultValue) ? 1 : parseInt(defaultValue);
  }
}
