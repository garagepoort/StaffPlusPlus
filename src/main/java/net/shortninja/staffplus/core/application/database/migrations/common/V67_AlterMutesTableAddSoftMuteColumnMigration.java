package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean
@IocMultiProvider(Migration.class)
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
