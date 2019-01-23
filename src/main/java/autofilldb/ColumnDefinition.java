package autofilldb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnDefinition {
    private static final int FIELD = 1;
    private static final int TYPE = 2;
    private static final int NULL = 3;
    private static final int KEY = 4;
    private static final int DEFAULT = 5;

    private String dataType;
    private String constraint;
    private String defaultValue;
    private String name;
    private boolean isNullable;

    public ColumnDefinition(ResultSet column) throws SQLException {
        this.dataType = column.getString(TYPE);
        this.constraint = column.getString(KEY);
        this.defaultValue = column.getString(DEFAULT);
        this.name = column.getString(FIELD);
        this.isNullable = column.getString(NULL).equals("YES");
    }

    public String getDataType() {
        return dataType;
    }

    public String getConstraint() {
        return constraint;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getName() {
        return name;
    }

    public boolean isNullable() {
        return isNullable;
    }
}
