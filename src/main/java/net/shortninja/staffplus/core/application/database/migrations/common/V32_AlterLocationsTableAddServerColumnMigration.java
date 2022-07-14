package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V32_AlterLocationsTableAddServerColumnMigration implements Migration {

    @Override
    public String getStatement(Connection connection) {
        return "ALTER TABLE sp_locations ADD COLUMN server_name VARCHAR(255) null;";
    }

    @Override
    public int getVersion() {
        return 32;
    }
}
