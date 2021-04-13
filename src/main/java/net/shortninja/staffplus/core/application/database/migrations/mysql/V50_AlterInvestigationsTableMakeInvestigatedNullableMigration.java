package net.shortninja.staffplus.core.application.database.migrations.mysql;

import be.garagepoort.mcsqlmigrations.Migration;

public class V50_AlterInvestigationsTableMakeInvestigatedNullableMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_investigations MODIFY investigated_uuid VARCHAR(36);";
    }

    @Override
    public int getVersion() {
        return 50;
    }
}
