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
public class V52_AddNamesToBanTableMigration implements Migration {

    private final PlayerManager playerManager;
    private final QueryBuilderFactory query;

    public V52_AddNamesToBanTableMigration(PlayerManager playerManager, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.query = query;
    }

    @Override
    public List<String> getStatements() {
        String addPlayerNameColumn = "ALTER TABLE sp_banned_players ADD COLUMN player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addIssuerNameColumn = "ALTER TABLE sp_banned_players ADD COLUMN issuer_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addPlayerNameColumn);
        statements.add(addIssuerNameColumn);

        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, query,"sp_banned_players", "player_name","player_uuid"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, query,"sp_banned_players", "issuer_name","issuer_uuid"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 52;
    }
}
