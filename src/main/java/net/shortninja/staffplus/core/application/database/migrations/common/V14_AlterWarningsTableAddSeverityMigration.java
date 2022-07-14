package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V14_AlterWarningsTableAddSeverityMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "ALTER TABLE sp_warnings ADD COLUMN severity VARCHAR(36);";
    }

    @Override
    public int getVersion() {
        return 14;
    }
}
