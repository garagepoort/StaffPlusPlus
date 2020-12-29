package net.shortninja.staffplus.util.database.migrations.sqlite;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V26_CreateMutedPlayersTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_muted_players (  " +
            "ID integer PRIMARY KEY,  " +
            "player_uuid VARCHAR(36) NOT NULL,  " +
            "issuer_uuid VARCHAR(36) NOT NULL,  " +
            "unmuted_by_uuid VARCHAR(36) NULL,  " +
            "reason TEXT NOT NULL,  " +
            "unmute_reason TEXT NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "end_timestamp BIGINT NULL" +
            ");";
    }

    @Override
    public int getVersion() {
        return 26;
    }
}
