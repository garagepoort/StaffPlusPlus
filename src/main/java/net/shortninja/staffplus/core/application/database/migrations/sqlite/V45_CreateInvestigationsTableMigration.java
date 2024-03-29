package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V45_CreateInvestigationsTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_investigations (  " +
            "ID integer PRIMARY KEY,  " +
            "investigator_uuid VARCHAR(36) NOT NULL,  " +
            "investigated_uuid VARCHAR(36) NOT NULL,  " +
            "status VARCHAR(36) NOT NULL DEFAULT 'OPEN',  " +
            "server_name VARCHAR(255) NULL,  " +
            "creation_timestamp BIGINT NOT NULL, " +
            "conclusion_timestamp BIGINT NULL);";
    }

    @Override
    public int getVersion() {
        return 45;
    }
}
