package net.shortninja.staffplus.core.application.database.migrations.sqlite;

import be.garagepoort.mcsqlmigrations.Migration;

import java.util.Arrays;
import java.util.List;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
@IocMultiProvider(Migration.class)
public class V77_UpdateDeletedReportsTableMigration implements Migration {
    @Override
    public List<String> getStatements() {
        return Arrays.asList("UPDATE sp_reports set deleted=0 WHERE deleted='false';","UPDATE sp_reports set deleted=1 WHERE deleted='true';");
    }

    @Override
    public int getVersion() {
        return 77;
    }
}
