package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V20_CreateBannedPlayersTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_banned_players (  " +
            "ID integer PRIMARY KEY,  " +
            "player_uuid VARCHAR(36) NOT NULL,  " +
            "issuer_uuid VARCHAR(36) NOT NULL,  " +
            "unbanned_by_uuid VARCHAR(36) NULL,  " +
            "reason TEXT NOT NULL,  " +
            "unban_reason TEXT NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "end_timestamp BIGINT NULL" +
            ");";
    }

    @Override
    public int getVersion() {
        return 20;
    }
}
