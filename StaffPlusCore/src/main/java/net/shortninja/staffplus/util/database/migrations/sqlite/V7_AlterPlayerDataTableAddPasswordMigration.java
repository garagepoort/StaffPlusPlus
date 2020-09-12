package net.shortninja.staffplus.util.database.migrations.sqlite;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V7_AlterPlayerDataTableAddPasswordMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_playerdata CHANGE Password Password VARCHAR(255) NOT NULL DEFAULT '';";
    }

    @Override
    public int getVersion() {
        return 7;
    }
}
