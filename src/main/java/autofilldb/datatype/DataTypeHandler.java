package autofilldb.datatype;

import autofilldb.ColumnDefinition;

public interface DataTypeHandler<T> {

  boolean canHandle(String dataType);

  T value(ColumnDefinition columnDefinition);
}
