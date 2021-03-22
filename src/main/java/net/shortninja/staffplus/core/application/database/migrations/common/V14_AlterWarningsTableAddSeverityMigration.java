package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

public class V14_AlterWarningsTableAddSeverityMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_warnings ADD COLUMN severity VARCHAR(36);";
    }

    @Override
    public int getVersion() {
        return 14;
    }
}
