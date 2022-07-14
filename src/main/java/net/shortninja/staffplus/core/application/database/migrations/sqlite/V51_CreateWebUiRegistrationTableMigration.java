package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V51_CreateWebUiRegistrationTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_web_ui_registration (  " +
            "ID integer PRIMARY KEY,  " +
            "authentication_key VARCHAR(32) NOT NULL,  " +
            "player_uuid VARCHAR(36) NOT NULL, " +
            "player_name VARCHAR(36) NOT NULL, " +
            "role VARCHAR(36) NOT NULL, " +
            "timestamp BIGINT NOT NULL " +
            ");";
    }

    @Override
    public int getVersion() {
        return 51;
    }
}
