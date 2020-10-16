package net.shortninja.staffplus.util.database.migrations.common;

import net.shortninja.staffplus.util.database.migrations.Migration;

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
