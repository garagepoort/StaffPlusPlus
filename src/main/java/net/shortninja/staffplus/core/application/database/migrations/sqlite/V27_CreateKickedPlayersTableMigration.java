package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

public class V27_CreateKickedPlayersTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_kicked_players (  " +
            "ID integer PRIMARY KEY,  " +
            "player_uuid VARCHAR(36) NOT NULL,  " +
            "issuer_uuid VARCHAR(36) NOT NULL,  " +
            "reason TEXT NOT NULL,  " +
            "creation_timestamp BIGINT NOT NULL " +
            ");";
    }

    @Override
    public int getVersion() {
        return 27;
    }
}
