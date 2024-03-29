package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V78_CreatePlayersTableMigration implements Migration {
    @Override
    public List<String> getStatements(Connection connection) {
        return Arrays.asList("CREATE TABLE IF NOT EXISTS sp_players (  " +
            "ID integer PRIMARY KEY,  " +
            "uuid VARCHAR(36) NOT NULL, " +
            "name VARCHAR(36) NOT NULL, " +
            "servers TEXT NOT NULL);",
            "CREATE INDEX sp_players_uuid ON sp_players(uuid);");
    }

    @Override
    public int getVersion() {
        return 78;
    }
}
