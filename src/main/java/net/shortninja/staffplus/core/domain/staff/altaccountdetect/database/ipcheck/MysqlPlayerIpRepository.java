package net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.ipcheck;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlPlayerIpRepository extends AbstractSqlPlayerIpRepository {

    public MysqlPlayerIpRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }
}
