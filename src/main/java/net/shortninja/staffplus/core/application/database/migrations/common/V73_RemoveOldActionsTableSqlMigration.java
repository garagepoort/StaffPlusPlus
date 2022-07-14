package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V73_RemoveOldActionsTableSqlMigration implements Migration {
    
    @Override
    public String getStatement(Connection connection) {
        return "DROP TABLE IF EXISTS sp_actionable_actions";
    }

    @Override
    public int getVersion() {
        return 73;
    }
}
