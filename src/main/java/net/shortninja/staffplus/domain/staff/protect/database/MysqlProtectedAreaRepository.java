package net.shortninja.staffplus.domain.staff.protect.database;

import net.shortninja.staffplus.application.database.migrations.mysql.MySQLConnection;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.domain.location.LocationRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlProtectedAreaRepository extends AbstractSqlProtectedAreaRepository {

    public MysqlProtectedAreaRepository(LocationRepository locationRepository, Options options) {
        super(locationRepository, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}
