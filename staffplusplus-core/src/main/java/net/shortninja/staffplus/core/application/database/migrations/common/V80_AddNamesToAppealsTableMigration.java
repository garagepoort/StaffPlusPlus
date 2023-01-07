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
public class V80_AddNamesToAppealsTableMigration implements Migration {

    private final PlayerManager playerManager;
    private final QueryBuilderFactory query;

    public V80_AddNamesToAppealsTableMigration(PlayerManager playerManager, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.query = query;
    }

    @Override
    public List<String> getStatements(Connection connection) {
        String addAppealerName = "ALTER TABLE sp_appeals ADD COLUMN appealer_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addResolverName = "ALTER TABLE sp_appeals ADD COLUMN resolver_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addAppealerName);
        statements.add(addResolverName);


        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_appeals", "appealer_name","appealer_uuid"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_appeals", "resolver_name","resolver_uuid"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 80;
    }
}
