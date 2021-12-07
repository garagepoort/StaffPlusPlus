package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V78_CreatePlayersTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_players (  " +
            "ID INT NOT NULL AUTO_INCREMENT,  " +
            "uuid VARCHAR(36) NOT NULL, " +
            "name VARCHAR(36) NOT NULL, " +
            "servers TEXT NOT NULL, " +
            "INDEX (uuid), " +
            "PRIMARY KEY (ID)) ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 78;
    }
}
