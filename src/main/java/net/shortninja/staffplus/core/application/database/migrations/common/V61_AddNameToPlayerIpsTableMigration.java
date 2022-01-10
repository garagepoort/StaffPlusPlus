package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.utils.DatabaseUtil;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class V61_AddNameToPlayerIpsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        String addPlayerNameColumn = "ALTER TABLE sp_player_ips ADD COLUMN player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addPlayerNameColumn);


        PlayerManager playerManager = StaffPlus.get().getIocContainer().get(PlayerManager.class);
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, "sp_player_ips", "player_name","player_uuid"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 61;
    }
}
