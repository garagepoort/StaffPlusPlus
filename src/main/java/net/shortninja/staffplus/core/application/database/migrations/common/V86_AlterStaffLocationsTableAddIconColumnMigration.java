package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V86_AlterStaffLocationsTableAddIconColumnMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_staff_locations ADD COLUMN icon VARCHAR(255) not null default 'PAPER';";
    }

    @Override
    public int getVersion() {
        return 86;
    }
}