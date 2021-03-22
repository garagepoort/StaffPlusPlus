package net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.whitelist;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlAltDetectWhitelistRepository extends AbstractSqlAltDetectWhitelistRepository {

    public MysqlAltDetectWhitelistRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }
}
