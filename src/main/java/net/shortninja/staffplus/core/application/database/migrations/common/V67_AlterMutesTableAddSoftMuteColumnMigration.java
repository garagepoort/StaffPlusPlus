package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V67_AlterMutesTableAddSoftMuteColumnMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_muted_players ADD COLUMN soft_mute boolean default 0;";
    }

    @Override
    public int getVersion() {
        return 67;
    }
}
