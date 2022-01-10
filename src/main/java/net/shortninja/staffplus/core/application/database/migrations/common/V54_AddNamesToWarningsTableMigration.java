package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.utils.DatabaseUtil;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class V54_AddNamesToWarningsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        String addPlayerNameColumn = "ALTER TABLE sp_warnings ADD COLUMN player_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addIssuerNameColumn = "ALTER TABLE sp_warnings ADD COLUMN warner_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addPlayerNameColumn);
        statements.add(addIssuerNameColumn);

        PlayerManager playerManager = StaffPlus.get().getIocContainer().get(PlayerManager.class);
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, "sp_warnings", "player_name","Player_UUID"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, "sp_warnings", "warner_name","Warner_UUID"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 54;
    }
}
