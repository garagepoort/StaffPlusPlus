package net.shortninja.staffplus.core.domain.delayedactions.database;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.config.Options;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlDelayedActionsRepository extends AbstractSqlDelayedActionsRepository {

    public MysqlDelayedActionsRepository(Options options, SqlConnectionProvider sqlConnectionProvider) {
        super(options, sqlConnectionProvider);
    }
}
