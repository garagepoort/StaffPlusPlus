package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

import java.util.Arrays;
import java.util.List;

public class V58_AlterBansTableAddSilentMigration implements Migration {

    @Override
    public List<String> getStatements() {
        return Arrays.asList(
            "ALTER TABLE sp_banned_players ADD COLUMN silent_ban boolean default 0;",
            "ALTER TABLE sp_banned_players ADD COLUMN silent_unban boolean default 0;"
        );
    }

    @Override
    public int getVersion() {
        return 58;
    }
}
