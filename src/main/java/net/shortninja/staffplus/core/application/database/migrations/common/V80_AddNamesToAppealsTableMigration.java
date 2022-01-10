package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.utils.DatabaseUtil;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class V80_AddNamesToAppealsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        String addAppealerName = "ALTER TABLE sp_appeals ADD COLUMN appealer_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addResolverName = "ALTER TABLE sp_appeals ADD COLUMN resolver_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        List<String> statements = new ArrayList<>();
        statements.add(addAppealerName);
        statements.add(addResolverName);


        PlayerManager playerManager = StaffPlus.get().getIocContainer().get(PlayerManager.class);
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, "sp_appeals", "appealer_name","appealer_uuid"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, "sp_appeals", "resolver_name","resolver_uuid"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 80;
    }
}
