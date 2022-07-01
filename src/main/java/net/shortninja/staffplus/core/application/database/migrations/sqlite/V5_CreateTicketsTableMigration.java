package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
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
