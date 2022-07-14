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
public class V76_AddNamesToInvestigationsTableMigration implements Migration {
    private final PlayerManager playerManager;
    private final QueryBuilderFactory query;

    public V76_AddNamesToInvestigationsTableMigration(PlayerManager playerManager, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.query = query;
    }

    @Override
    public List<String> getStatements(Connection connection) {
        String addInvestigatorName = "ALTER TABLE sp_investigations ADD COLUMN investigator_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addInvestigatedName = "ALTER TABLE sp_investigations ADD COLUMN investigated_name VARCHAR(32) NULL;";
        List<String> statements = new ArrayList<>();
        statements.add(addInvestigatedName);
        statements.add(addInvestigatorName);

        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_investigations", "investigator_name","investigator_uuid"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(connection, playerManager, query,"sp_investigations", "investigated_name","investigated_uuid"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 76;
    }
}
