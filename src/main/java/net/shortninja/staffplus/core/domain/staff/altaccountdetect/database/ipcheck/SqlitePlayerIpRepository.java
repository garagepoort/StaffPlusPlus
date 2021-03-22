package net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.ipcheck;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqlitePlayerIpRepository extends AbstractSqlPlayerIpRepository {

    public SqlitePlayerIpRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }
}
