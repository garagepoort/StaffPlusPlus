package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean
@IocMultiProvider(Migration.class)
public class V8_AlterReportTableAddStatusMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_reports ADD COLUMN status VARCHAR(16) NOT NULL DEFAULT 'OPEN';";
    }

    @Override
    public int getVersion() {
        return 8;
    }
}
