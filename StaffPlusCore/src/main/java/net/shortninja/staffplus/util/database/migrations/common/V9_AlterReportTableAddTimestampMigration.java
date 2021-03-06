package net.shortninja.staffplus.util.database.migrations.common;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V9_AlterReportTableAddTimestampMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports ADD COLUMN timestamp BIGINT;";
    }

    @Override
    public int getVersion() {
        return 9;
    }
}
