package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V24_AlterWarningTableAddTimestampMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_warnings ADD COLUMN timestamp BIGINT NOT NULL DEFAULT 1602696006000;";
    }

    @Override
    public int getVersion() {
        return 24;
    }
}
