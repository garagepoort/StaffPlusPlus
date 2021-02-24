package net.shortninja.staffplus.util.database.migrations.common;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V41_AlterWarningTableAddExpiredMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_warnings ADD COLUMN is_expired boolean NOT NULL DEFAULT false;";
    }

    @Override
    public int getVersion() {
        return 41;
    }
}
