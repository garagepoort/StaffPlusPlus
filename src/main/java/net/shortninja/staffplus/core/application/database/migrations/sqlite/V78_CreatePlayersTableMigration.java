package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import java.util.Arrays;
import java.util.List;

public class V78_CreatePlayersTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
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
