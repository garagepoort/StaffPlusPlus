package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.utils.DatabaseUtil;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class V56_UpdateNamesReportsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        List<String> statements = new ArrayList<>();
        PlayerManager playerManager = StaffPlus.get().getIocContainer().get(PlayerManager.class);
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, "sp_reports", "player_name","Player_UUID"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, "sp_reports", "reporter_name","Reporter_UUID"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 56;
    }
}
