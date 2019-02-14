package com.github.sskelkar.autofilldb;

import static com.github.sskelkar.autofilldb.Util.isNullOrEmpty;
import static java.lang.Integer.parseInt;
import static java.util.UUID.randomUUID;

final class VarcharHandler implements DataTypeHandler<String> {

  @Override
  public boolean canHandle(String dataType) {
    return dataType.toLowerCase().startsWith("varchar");
  }

  @Override
  public String uniqueValue(ColumnDefinition def) {
    String randomString = randomUUID().toString();
    int columnSize = parseInt(def.getDataType().replace("varchar(", "").replace(")", ""));
    String value = randomString.length() > columnSize ? randomString.substring(0, columnSize) : randomString;
    return enQuote(value);
  }

  @Override
  public String value(String defaultValue) {
    String value = isNullOrEmpty(defaultValue) ? "a" : defaultValue;
    return enQuote(value);
  }

  private String enQuote(String value) {
    return "'" + value + "'";
  }
}
