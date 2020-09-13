package net.shortninja.staffplus.util.database.migrations.common;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V11_AlterReportTableAddStaffUuidMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports ADD COLUMN staff_uuid varchar(36);";
    }

    @Override
    public int getVersion() {
        return 11;
    }
}
