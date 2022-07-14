package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V59_AlterPlayerIpTableAddTimestampMigration implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        return Arrays.asList(
            "ALTER TABLE sp_player_ips ADD COLUMN timestamp BIGINT;",
            "UPDATE sp_player_ips SET timestamp = CURRENT_TIMESTAMP;");
    }

    @Override
    public int getVersion() {
        return 59;
    }
}
