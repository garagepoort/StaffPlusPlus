package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V70_RemoveOldCommandsTableSqlMigration implements Migration {
    
    @Override
    public String getStatement() {
        return "DROP TABLE sp_commands";
    }

    @Override
    public int getVersion() {
        return 70;
    }
}
