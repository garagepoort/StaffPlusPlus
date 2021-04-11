package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V49_AlterSessionTableAddStaffModeNameColumnMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_sessions ADD COLUMN staff_mode_name VARCHAR(128) NULL;";
    }

    @Override
    public int getVersion() {
        return 49;
    }
}
