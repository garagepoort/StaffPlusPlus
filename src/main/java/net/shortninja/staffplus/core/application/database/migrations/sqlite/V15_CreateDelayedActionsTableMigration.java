package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

public class V15_CreateDelayedActionsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_delayed_actions (ID INTEGER PRIMARY KEY,  command VARCHAR(255) NOT NULL, Player_UUID VARCHAR(36) NOT NULL);";
    }

    @Override
    public int getVersion() {
        return 15;
    }
}
