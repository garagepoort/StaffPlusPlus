package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean
@IocMultiProvider(Migration.class)
public class V74_AlterBansTableAddBanMessageMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_banned_players ADD COLUMN template VARCHAR(255);";
    }

    @Override
    public int getVersion() {
        return 74;
    }
}
