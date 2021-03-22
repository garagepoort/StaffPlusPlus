package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

public class V1_CreateReportsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_reports (  ID integer PRIMARY KEY,  Reason VARCHAR(255) NULL,  Reporter_UUID VARCHAR(36) NULL,  Player_UUID VARCHAR(36) NULL)";
    }

    @Override
    public int getVersion() {
        return 1;
    }
}
