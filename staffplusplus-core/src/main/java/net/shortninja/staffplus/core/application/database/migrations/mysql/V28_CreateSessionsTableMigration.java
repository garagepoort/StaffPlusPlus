package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V28_CreateSessionsTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_sessions (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "player_uuid VARCHAR(36) NOT NULL,  " +
            "vanish_type VARCHAR(36) NOT NULL DEFAULT 'NONE',  " +
            "in_staff_mode boolean NOT NULL default 0,  " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 28;
    }
}
