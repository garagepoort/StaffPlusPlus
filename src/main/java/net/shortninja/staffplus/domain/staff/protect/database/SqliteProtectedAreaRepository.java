package net.shortninja.staffplus.domain.staff.protect.database;

import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.location.LocationRepository;
import net.shortninja.staffplus.application.database.migrations.sqlite.SqlLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteProtectedAreaRepository extends AbstractSqlProtectedAreaRepository {

    public SqliteProtectedAreaRepository(LocationRepository locationRepository, Options options) {
        super(locationRepository, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }
}
