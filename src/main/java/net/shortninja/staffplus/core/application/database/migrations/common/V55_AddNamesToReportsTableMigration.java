package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.common.utils.DatabaseUtil;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

@IocBean
@IocMultiProvider(Migration.class)
public class V55_AddNamesToReportsTableMigration implements Migration {

    private final PlayerManager playerManager;
    private final QueryBuilderFactory query;

    public V55_AddNamesToReportsTableMigration(PlayerManager playerManager, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.query = query;
    }

    @Override
    public List<String> getStatements(Connection connection) {
        String addPlayerNameColumn = "ALTER TABLE sp_reports ADD COLUMN player_name VARCHAR(32) NULL;";
        String addIssuerNameColumn = "ALTER TABLE sp_reports ADD COLUMN reporter_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addPlayerNameColumn);
        statements.add(addIssuerNameColumn);

        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_reports", "player_name","Player_UUID"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_reports", "reporter_name","Reporter_UUID"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 55;
    }
}
