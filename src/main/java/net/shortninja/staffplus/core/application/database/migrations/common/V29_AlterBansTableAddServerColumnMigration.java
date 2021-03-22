package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V29_AlterBansTableAddServerColumnMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_banned_players ADD COLUMN server_name VARCHAR(255) null;";
    }

    @Override
    public int getVersion() {
        return 29;
    }
}
