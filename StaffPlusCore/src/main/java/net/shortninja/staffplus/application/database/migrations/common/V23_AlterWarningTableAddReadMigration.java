package net.shortninja.staffplus.application.database.migrations.common;

import net.shortninja.staffplus.application.database.migrations.Migration;

public class V23_AlterWarningTableAddReadMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_warnings ADD COLUMN is_read boolean NOT NULL DEFAULT false;";
    }

    @Override
    public int getVersion() {
        return 23;
    }
}
