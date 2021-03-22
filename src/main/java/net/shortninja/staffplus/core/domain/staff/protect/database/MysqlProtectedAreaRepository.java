package net.shortninja.staffplus.core.domain.staff.protect.database;

import be.garagepoort.mcioc.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.location.LocationRepository;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlProtectedAreaRepository extends AbstractSqlProtectedAreaRepository {

    public MysqlProtectedAreaRepository(LocationRepository locationRepository, SqlConnectionProvider sqlConnectionProvider, Options options) {
        super(locationRepository, sqlConnectionProvider, options);
    }
}
