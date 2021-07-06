package net.shortninja.staffplus.core.domain.staff.altaccountdetect.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlAltDetectWhitelistRepository extends AbstractSqlAltDetectWhitelistRepository {

    public MysqlAltDetectWhitelistRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }
}
