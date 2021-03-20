package net.shortninja.staffplus.application.database.migrations.common;

import net.shortninja.staffplus.application.database.migrations.Migration;

public class V40_AlterReportsTableAddLocationColumnMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports ADD COLUMN location_id INT null;";
    }

    @Override
    public int getVersion() {
        return 40;
    }
}
