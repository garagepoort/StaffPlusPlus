package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V22_CreateAltDetectWhitelistTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_alt_detect_whitelist (  " +
            "player_uuid_1 VARCHAR(36) NOT NULL,  " +
            "player_uuid_2 VARCHAR(36) NOT NULL,  " +
            "PRIMARY KEY (player_uuid_1, player_uuid_2)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 22;
    }
}
