package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V70_RemoveOldCommandsTableSqlMigration implements Migration {
    
    @Override
    public String getStatement(Connection connection) {
        return "DROP TABLE IF EXISTS sp_commands";
    }

    @Override
    public int getVersion() {
        return 70;
    }
}
