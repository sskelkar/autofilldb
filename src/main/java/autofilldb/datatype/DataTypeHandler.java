package autofilldb.datatype;

import autofilldb.ColumnDefinition;

public interface DataTypeHandler<T> {

  boolean canHandle(String dataType);

  T uniqueValue(ColumnDefinition columnDefinition);

  T value(String defaultValue);
}
