package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V39_CreateActionableActionsTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_actionable_actions (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "actionable_id INT NOT NULL,  " +
            "actionable_type VARCHAR(36) NOT NULL,  " +
            "command VARCHAR(255) NOT NULL,  " +
            "rollback_command VARCHAR(255) NULL,  " +
            "run_strategy VARCHAR(255) NOT NULL, " +
            "rollback_run_strategy VARCHAR(255) NULL,  " +
            "execution_timestamp BIGINT NULL," +
            "rollback_timestamp BIGINT NULL," +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 39;
    }
}
