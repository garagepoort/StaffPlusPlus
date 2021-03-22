package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

public class V5_CreateTicketsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_tickets ( UUID VARCHAR(36) PRIMARY KEY, ID INT NOT NULL, Inquiry VARCHAR(255) NOT NULL);";
    }

    @Override
    public int getVersion() {
        return 5;
    }
}
