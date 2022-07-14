package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V40_AlterReportsTableAddLocationColumnMigration implements Migration {

    @Override
    public String getStatement(Connection connection) {
        return "ALTER TABLE sp_reports ADD COLUMN location_id INT null;";
    }

    @Override
    public int getVersion() {
        return 40;
    }
}
