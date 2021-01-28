package net.shortninja.staffplus.util.database.migrations.common;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V35_AlterWarningsTableAddServerColumnMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_warnings ADD COLUMN server_name VARCHAR(255) null;";
    }

    @Override
    public int getVersion() {
        return 35;
    }
}
