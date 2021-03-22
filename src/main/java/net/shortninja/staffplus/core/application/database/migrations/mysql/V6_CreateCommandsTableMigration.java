package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V6_CreateCommandsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_commands (Command_Name VARCHAR(36) NOT NULL, Command VARCHAR(36) NOT NULL, PRIMARY KEY (Command_Name)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 6;
    }
}
