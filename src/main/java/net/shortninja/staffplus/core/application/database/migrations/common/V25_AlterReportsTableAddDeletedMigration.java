package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V25_AlterReportsTableAddDeletedMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports ADD COLUMN deleted boolean not null default false;";
    }

    @Override
    public int getVersion() {
        return 25;
    }
}
