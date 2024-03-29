package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V22_CreateAltDetectWhitelistTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_alt_detect_whitelist (  " +
            "player_uuid_1 VARCHAR(36) NOT NULL,  " +
            "player_uuid_2 VARCHAR(36) NOT NULL,  " +
            "PRIMARY KEY (player_uuid_1, player_uuid_2));";
    }

    @Override
    public int getVersion() {
        return 22;
    }
}
