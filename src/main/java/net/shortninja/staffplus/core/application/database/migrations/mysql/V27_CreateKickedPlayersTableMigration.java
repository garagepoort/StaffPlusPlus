package net.shortninja.staffplus.core.application.database.migrations.mysql;

import net.shortninja.staffplus.core.application.database.migrations.Migration;

public class V27_CreateKickedPlayersTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_kicked_players (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "player_uuid VARCHAR(36) NOT NULL,  " +
            "issuer_uuid VARCHAR(36) NOT NULL,  " +
            "reason TEXT NOT NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 27;
    }
}
