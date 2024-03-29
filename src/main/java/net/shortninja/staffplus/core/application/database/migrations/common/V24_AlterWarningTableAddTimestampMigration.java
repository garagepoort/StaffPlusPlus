package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V24_AlterWarningTableAddTimestampMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "ALTER TABLE sp_warnings ADD COLUMN timestamp BIGINT NOT NULL DEFAULT 1602696006000;";
    }

    @Override
    public int getVersion() {
        return 24;
    }
}
