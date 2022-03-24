package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V38_AlterDelayedActionsTableAddRollbackMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_delayed_actions ADD COLUMN rollback boolean default 0;";
    }

    @Override
    public int getVersion() {
        return 38;
    }
}
