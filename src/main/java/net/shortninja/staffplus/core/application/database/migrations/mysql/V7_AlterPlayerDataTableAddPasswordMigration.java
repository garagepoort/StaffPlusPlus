package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V7_AlterPlayerDataTableAddPasswordMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_playerdata CHANGE Password Password VARCHAR(255) NOT NULL DEFAULT '';";
    }

    @Override
    public int getVersion() {
        return 7;
    }
}
