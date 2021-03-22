package net.shortninja.staffplus.core.domain.staff.protect.database;

import net.shortninja.staffplus.core.application.IocBean;
import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.location.LocationRepository;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteProtectedAreaRepository extends AbstractSqlProtectedAreaRepository {

    public SqliteProtectedAreaRepository(LocationRepository locationRepository, SqlConnectionProvider sqlConnectionProvider, Options options) {
        super(locationRepository, sqlConnectionProvider, options);
    }
}
