package net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.whitelist;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteAltDetectWhitelistRepository extends AbstractSqlAltDetectWhitelistRepository {

    public SqliteAltDetectWhitelistRepository(SqlConnectionProvider sqlConnectionProvider) {
        super(sqlConnectionProvider);
    }
}
