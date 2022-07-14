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
public class V54_AddNamesToWarningsTableMigration implements Migration {

    private final PlayerManager playerManager;
    private final QueryBuilderFactory query;

    public V54_AddNamesToWarningsTableMigration(PlayerManager playerManager, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.query = query;
    }

    @Override
    public List<String> getStatements(Connection connection) {
        String addPlayerNameColumn = "ALTER TABLE sp_warnings ADD COLUMN player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addIssuerNameColumn = "ALTER TABLE sp_warnings ADD COLUMN warner_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addPlayerNameColumn);
        statements.add(addIssuerNameColumn);

        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_warnings", "player_name","Player_UUID"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_warnings", "warner_name","Warner_UUID"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 54;
    }
}
