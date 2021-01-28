package net.shortninja.staffplus.staff.protect.database;

import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.location.LocationRepository;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

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
