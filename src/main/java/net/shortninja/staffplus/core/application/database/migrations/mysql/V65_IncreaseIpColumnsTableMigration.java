package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

@IocBean(conditionalOnProperty = "storage.type=mysql")
@IocMultiProvider(Migration.class)
public class V65_IncreaseIpColumnsTableMigration implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        return Arrays.asList("ALTER TABLE sp_player_ips MODIFY ip VARCHAR(40) NOT NULL;",
            "ALTER TABLE sp_banned_ips MODIFY ip VARCHAR(40) NOT NULL;");
    }

    @Override
    public int getVersion() {
        return 65;
    }
}
