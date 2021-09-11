package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V71_CreateCommandsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_commands (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +

            "actionable_id INT NULL,  " +
            "actionable_type VARCHAR(36) NULL,  " +
            "rollback_timestamp BIGINT NULL, " +
            "rollback_command_id INT NULL, " +

            "command VARCHAR(255) NOT NULL,  " +
            "executioner_uuid VARCHAR(36) NOT NULL, " +
            "target_uuid VARCHAR(36) NULL, " +
            "executioner_run_strategy VARCHAR(255) NOT NULL,  " +
            "target_run_strategy VARCHAR(255) NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "execution_timestamp BIGINT NULL, " +
            "server_name VARCHAR(255) NOT NULL, " +

            "is_delayed BOOLEAN default 0, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 71;
    }
}
