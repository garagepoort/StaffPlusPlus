package net.shortninja.staffplus.application.database.migrations.mysql;

import net.shortninja.staffplus.application.database.migrations.Migration;

public class V5_CreateTicketsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_tickets ( UUID VARCHAR(36) NOT NULL, ID INT NOT NULL, Inquiry VARCHAR(255) NOT NULL, PRIMARY KEY (UUID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 5;
    }
}
