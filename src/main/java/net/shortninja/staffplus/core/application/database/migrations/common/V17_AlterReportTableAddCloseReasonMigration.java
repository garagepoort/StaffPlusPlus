package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V17_AlterReportTableAddCloseReasonMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports ADD COLUMN close_reason text;";
    }

    @Override
    public int getVersion() {
        return 17;
    }
}
