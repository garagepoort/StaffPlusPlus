package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import net.shortninja.staffplus.core.application.database.migrations.Migration;

public class V18_CreateLocationsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_locations (  ID integer PRIMARY KEY,  x INTEGER NOT NULL,  y INTEGER NOT NULL,  z INTEGER NOT NULL, world VARCHAR(128) NOT NULL)";
    }

    @Override
    public int getVersion() {
        return 18;
    }
}
