package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V45_CreateInvestigationsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_investigations (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "investigator_uuid VARCHAR(36) NOT NULL,  " +
            "investigated_uuid VARCHAR(36) NOT NULL,  " +
            "status VARCHAR(36) NOT NULL DEFAULT 'OPEN',  " +
            "server_name VARCHAR(255) NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "conclusion_timestamp BIGINT NULL, " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 45;
    }
}
