package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V4_CreatePlayerDataTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_playerdata ( GlassColor INT NOT NULL DEFAULT 0, Password VARCHAR(255) NOT NULL DEFAULT '', Player_UUID VARCHAR(36) NOT NULL, Name VARCHAR(18) NOT NULL, PRIMARY KEY (Player_UUID))  ENGINE = InnoDB;";
    }

    @Override
    public int getVersion() {
        return 4;
    }
}
