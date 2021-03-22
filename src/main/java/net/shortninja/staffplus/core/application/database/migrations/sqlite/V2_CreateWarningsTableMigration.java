package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import net.shortninja.staffplus.core.application.database.migrations.Migration;

public class V2_CreateWarningsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_warnings (ID INTEGER PRIMARY KEY,  Reason VARCHAR(255) NULL,  Warner_UUID VARCHAR(36) NULL,  Player_UUID VARCHAR(36) NOT NULL);";
    }

    @Override
    public int getVersion() {
        return 2;
    }
}
