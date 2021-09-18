package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V75_AlterIpBansTableAddBanMessageMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_banned_ips ADD COLUMN template VARCHAR(255);";
    }

    @Override
    public int getVersion() {
        return 75;
    }
}
