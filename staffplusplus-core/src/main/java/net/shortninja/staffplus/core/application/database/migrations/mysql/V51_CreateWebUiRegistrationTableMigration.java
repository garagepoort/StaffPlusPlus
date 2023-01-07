package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V51_CreateWebUiRegistrationTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_web_ui_registration (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "authentication_key VARCHAR(32) NOT NULL,  " +
            "player_uuid VARCHAR(36) NOT NULL, " +
            "player_name VARCHAR(36) NOT NULL, " +
            "role VARCHAR(36) NOT NULL, " +
            "timestamp BIGINT NOT NULL, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 51;
    }
}
