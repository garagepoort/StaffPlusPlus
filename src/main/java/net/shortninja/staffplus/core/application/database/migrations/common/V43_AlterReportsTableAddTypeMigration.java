package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V43_AlterReportsTableAddTypeMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports ADD COLUMN type VARCHAR(36) NULL;";
    }

    @Override
    public int getVersion() {
        return 43;
    }
}
