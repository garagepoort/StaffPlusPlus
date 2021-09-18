package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V73_RemoveOldActionsTableSqlMigration implements Migration {
    
    @Override
    public String getStatement() {
        return "DROP TABLE IF EXISTS sp_actionable_actions";
    }

    @Override
    public int getVersion() {
        return 73;
    }
}
