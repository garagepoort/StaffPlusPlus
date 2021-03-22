package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V37_AlterDelayedActionsTableAddExecutableActionIdMigration implements Migration {

    @Override
    public String getStatement() {
        return "ALTER TABLE sp_delayed_actions ADD COLUMN executable_action_id INT null;";
    }

    @Override
    public int getVersion() {
        return 37;
    }
}
