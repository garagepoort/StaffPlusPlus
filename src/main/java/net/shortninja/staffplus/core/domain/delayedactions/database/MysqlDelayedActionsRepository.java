package net.shortninja.staffplus.core.domain.delayedactions.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlDelayedActionsRepository extends AbstractSqlDelayedActionsRepository {

    public MysqlDelayedActionsRepository(Options options, SqlConnectionProvider sqlConnectionProvider) {
        super(options, sqlConnectionProvider);
    }
}
