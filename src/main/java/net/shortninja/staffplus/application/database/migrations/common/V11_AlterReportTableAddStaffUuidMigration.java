package net.shortninja.staffplus.application.database.migrations.common;

import net.shortninja.staffplus.application.database.migrations.Migration;

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
