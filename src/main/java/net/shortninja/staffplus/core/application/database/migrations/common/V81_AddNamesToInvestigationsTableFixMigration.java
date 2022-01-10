package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.utils.DatabaseUtil;
import net.shortninja.staffplus.core.domain.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class V81_AddNamesToInvestigationsTableFixMigration implements Migration {
    @Override
    public List<String> getStatements() {
        String addInvestigatorName = "ALTER TABLE sp_investigations ADD COLUMN investigator_name VARCHAR(32) NOT NULL DEFAULT 'Unknown';";
        String addInvestigatedName = "ALTER TABLE sp_investigations ADD COLUMN investigated_name VARCHAR(32) NULL;";
        List<String> statements = new ArrayList<>();
        statements.add(addInvestigatedName);
        statements.add(addInvestigatorName);

        PlayerManager playerManager = StaffPlus.get().getIocContainer().get(PlayerManager.class);
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, "sp_investigations", "investigator_name","investigator_uuid"));
        statements.addAll(DatabaseUtil.createMigrateNameStatements(playerManager, "sp_investigations", "investigated_name","investigated_uuid"));
        return statements;
    }

    @Override
    public int getVersion() {
        return 81;
    }
}
