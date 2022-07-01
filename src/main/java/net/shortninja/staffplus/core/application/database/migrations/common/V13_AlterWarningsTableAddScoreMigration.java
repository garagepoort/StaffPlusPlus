package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

@IocBean
@IocMultiProvider(Migration.class)
public class V13_AlterWarningsTableAddScoreMigration implements Migration {
    @Override
    public String getStatement() {
        return "ALTER TABLE sp_warnings ADD COLUMN score SMALLINT NOT NULL DEFAULT 0;";
    }

    @Override
    public int getVersion() {
        return 13;
    }
}
