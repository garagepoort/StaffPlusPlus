package net.shortninja.staffplus.application.database.migrations.common;

import net.shortninja.staffplus.application.database.migrations.Migration;

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
