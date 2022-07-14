package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V4_CreatePlayerDataTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_playerdata ( GlassColor INTEGER NOT NULL DEFAULT 0, Password VARCHAR(255) NOT NULL DEFAULT '', Player_UUID VARCHAR(36) PRIMARY KEY, Name VARCHAR(18) NOT NULL);";
    }

    @Override
    public int getVersion() {
        return 4;
    }
}
