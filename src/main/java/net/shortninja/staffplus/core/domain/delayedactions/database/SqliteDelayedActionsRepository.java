package net.shortninja.staffplus.core.domain.delayedactions.database;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.config.Options;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteDelayedActionsRepository extends AbstractSqlDelayedActionsRepository {

    public SqliteDelayedActionsRepository(Options options, SqlConnectionProvider sqlConnectionProvider) {
        super(options, sqlConnectionProvider);
    }
}
