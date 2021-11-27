package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V77_CreateQueueTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_queue (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "type VARCHAR(255) NOT NULL,  " +
            "data TEXT NOT NULL,  " +
            "timestamp BIGINT NOT NULL, " +
            "process_id VARCHAR(36) NULL, " +
            "status VARCHAR(28) NOT NULL,  " +
            "status_message VARCHAR(255) NULL,  " +
            "server_name VARCHAR(255) NULL, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 77;
    }
}
