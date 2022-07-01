package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.common.utils.DatabaseUtil;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

@IocBean
@IocMultiProvider(Migration.class)
public class V61_AddNameToPlayerIpsTableMigration implements Migration {

    private final PlayerManager playerManager;
    private final QueryBuilderFactory query;

    public V61_AddNameToPlayerIpsTableMigration(PlayerManager playerManager, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.query = query;
    }

    @Override
    public List<String> getStatements() {
        String addPlayerNameColumn = "ALTER TABLE sp_player_ips ADD COLUMN player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addPlayerNameColumn);


        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, query,"sp_player_ips", "player_name","player_uuid"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 61;
    }
}
