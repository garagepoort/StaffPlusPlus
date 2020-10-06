package net.shortninja.staffplus.staff.protect.database;

import net.shortninja.staffplus.staff.location.LocationRepository;
import net.shortninja.staffplus.util.database.migrations.mysql.MySQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlProtectedAreaRepository extends AbstractSqlProtectedAreaRepository {

    public MysqlProtectedAreaRepository(LocationRepository locationRepository) {
        super(locationRepository);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }
}
