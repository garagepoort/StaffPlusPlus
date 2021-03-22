package net.shortninja.staffplus.core.application.database.migrations.common;

import net.shortninja.staffplus.core.application.database.migrations.Migration;

public class V8_AlterReportTableAddStatusMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports ADD COLUMN status VARCHAR(16) NOT NULL DEFAULT 'OPEN';";
    }

    @Override
    public int getVersion() {
        return 8;
    }
}
