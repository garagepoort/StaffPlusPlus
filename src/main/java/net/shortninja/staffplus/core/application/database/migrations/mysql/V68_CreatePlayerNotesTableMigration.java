package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V68_CreatePlayerNotesTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_player_notes (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "note TEXT NOT NULL,  " +
            "noted_by_uuid VARCHAR(36) NOT NULL,  " +
            "noted_by_name VARCHAR(36) NOT NULL,  " +
            "target_uuid VARCHAR(36) NOT NULL,  " +
            "target_name VARCHAR(36) NOT NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "server_name VARCHAR(255) NOT NULL, " +
            "is_private_note BOOLEAN NOT NULL DEFAULT 0, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 68;
    }
}
