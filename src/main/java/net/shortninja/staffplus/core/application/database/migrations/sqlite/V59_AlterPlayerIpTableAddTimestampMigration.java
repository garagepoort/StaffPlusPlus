package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import java.util.Arrays;
import java.util.List;

public class V59_AlterPlayerIpTableAddTimestampMigration implements Migration {
    @Override
    public List<String> getStatements() {
        return Arrays.asList(
            "ALTER TABLE sp_player_ips ADD COLUMN timestamp BIGINT;",
            "UPDATE sp_player_ips SET timestamp = CURRENT_TIMESTAMP;");
    }

    @Override
    public int getVersion() {
        return 59;
    }
}
