package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V59_AlterPlayerIpTableAddTimestampMigration implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        return Arrays.asList(
            "ALTER TABLE sp_player_ips ADD COLUMN timestamp BIGINT;",
            "UPDATE sp_player_ips SET timestamp = UNIX_TIMESTAMP() * 1000;");
    }

    @Override
    public int getVersion() {
        return 59;
    }
}
