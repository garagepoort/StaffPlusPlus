package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V5_CreateTicketsTableMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "CREATE TABLE IF NOT EXISTS sp_tickets ( UUID VARCHAR(36) NOT NULL, ID INT NOT NULL, Inquiry VARCHAR(255) NOT NULL, PRIMARY KEY (UUID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 5;
    }
}
