package net.shortninja.staffplus.util.database.migrations.common;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V10_AlterReportTableAddStaffNameMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports ADD COLUMN staff_name varchar(16);";
    }

    @Override
    public int getVersion() {
        return 10;
    }
}
