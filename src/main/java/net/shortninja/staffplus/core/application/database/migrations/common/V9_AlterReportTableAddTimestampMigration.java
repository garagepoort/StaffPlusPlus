package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

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
