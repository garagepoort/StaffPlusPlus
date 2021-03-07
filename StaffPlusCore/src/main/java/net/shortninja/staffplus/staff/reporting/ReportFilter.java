package net.shortninja.staffplus.staff.reporting;

public class ReportFilter<T> {

    private T value;
    private int sqlType;
    private String sqlColumn;

    public ReportFilter(T value, int sqlType, String sqlColumn) {
        this.value = value;
        this.sqlType = sqlType;
        this.sqlColumn = sqlColumn;
    }

    public T getValue() {
        return value;
    }

    public String getSqlColumn() {
        return sqlColumn;
    }

    public int getSqlType() {
        return sqlType;
    }
}
