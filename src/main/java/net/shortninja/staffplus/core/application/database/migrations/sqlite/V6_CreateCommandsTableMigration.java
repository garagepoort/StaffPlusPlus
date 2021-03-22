package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import net.shortninja.staffplus.core.application.database.migrations.Migration;

public class V6_CreateCommandsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_commands (Command_Name VARCHAR(36) PRIMARY KEY, Command VARCHAR(36) NOT NULL);";
    }

    @Override
    public int getVersion() {
        return 6;
    }
}
