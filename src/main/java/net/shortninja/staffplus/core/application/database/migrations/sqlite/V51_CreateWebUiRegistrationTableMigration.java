package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

public class V51_CreateWebUiRegistrationTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_web_ui_registration (  " +
            "ID integer PRIMARY KEY,  " +
            "authentication_key VARCHAR(32) NOT NULL,  " +
            "player_uuid VARCHAR(36) NOT NULL, " +
            "timestamp BIGINT NOT NULL " +
            ");";
    }

    @Override
    public int getVersion() {
        return 51;
    }
}
