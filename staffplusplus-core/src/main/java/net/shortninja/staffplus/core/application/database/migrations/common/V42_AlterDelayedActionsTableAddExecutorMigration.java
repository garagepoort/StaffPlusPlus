package net.shortninja.staffplus.core.application.database.migrations.common;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcsqlmigrations.Migration;

import java.sql.Connection;

@IocBean
@IocMultiProvider(Migration.class)
public class V42_AlterDelayedActionsTableAddExecutorMigration implements Migration {
    @Override
    public String getStatement(Connection connection) {
        return "ALTER TABLE sp_delayed_actions ADD COLUMN executor VARCHAR(12) NOT NULL DEFAULT 'CONSOLE';";
    }

    @Override
    public int getVersion() {
        return 42;
    }
}
