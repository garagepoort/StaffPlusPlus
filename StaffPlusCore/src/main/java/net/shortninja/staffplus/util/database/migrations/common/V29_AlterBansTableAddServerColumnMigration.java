package net.shortninja.staffplus.util.database.migrations.common;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V29_AlterBansTableAddServerColumnMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_banned_players ADD COLUMN server_name VARCHAR(255) null;";
    }

    @Override
    public int getVersion() {
        return 29;
    }
}
