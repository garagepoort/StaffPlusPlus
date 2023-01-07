package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.common.utils.DatabaseUtil;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@IocBean
@IocMultiProvider(Migration.class)
public class V56_UpdateNamesReportsTableMigration implements Migration {
    private final PlayerManager playerManager;
    private final QueryBuilderFactory query;

    public V56_UpdateNamesReportsTableMigration(PlayerManager playerManager, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.query = query;
    }

    @Override
    public List<String> getStatements(Connection connection) {
        List<String> statements = new ArrayList<>();
        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_reports", "player_name","Player_UUID"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_reports", "reporter_name","Reporter_UUID"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 56;
    }
}
