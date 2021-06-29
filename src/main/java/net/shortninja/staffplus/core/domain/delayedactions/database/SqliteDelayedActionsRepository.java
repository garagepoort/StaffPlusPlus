package net.shortninja.staffplus.core.domain.delayedactions.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteDelayedActionsRepository extends AbstractSqlDelayedActionsRepository {

    public SqliteDelayedActionsRepository(Options options, SqlConnectionProvider sqlConnectionProvider) {
        super(options, sqlConnectionProvider);
    }
}
