package net.shortninja.staffplus.core.application.database.migrations.mysql;

import net.shortninja.staffplus.core.application.database.migrations.Migration;

public class V21_CreatePlayerIpsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_player_ips (  " +
            "player_uuid VARCHAR(36) NOT NULL,  " +
            "ip VARCHAR(15) NOT NULL,  " +
            "PRIMARY KEY (player_uuid, ip)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 21;
    }
}
