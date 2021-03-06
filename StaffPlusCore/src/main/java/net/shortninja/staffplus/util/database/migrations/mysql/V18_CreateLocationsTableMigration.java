package net.shortninja.staffplus.util.database.migrations.mysql;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V18_CreateLocationsTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_locations (  ID INT NOT NULL AUTO_INCREMENT,  x INTEGER NOT NULL,  y INTEGER NOT NULL,  z INTEGER NOT NULL, world VARCHAR(128) NOT NULL,  PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 18;
    }
}
