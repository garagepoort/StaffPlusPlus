package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V21_CreatePlayerIpsTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_player_ips (  " +
            "player_uuid VARCHAR(36) NOT NULL,  " +
            "ip VARCHAR(15) NOT NULL,  " +
            "PRIMARY KEY (player_uuid, ip));";
    }

    @Override
    public int getVersion() {
        return 21;
    }
}
