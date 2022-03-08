package net.shortninja.staffplus.core.application.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class MysqlQueryService implements SqlQueryService {

    private final SqlConnectionProvider sqlConnectionProvider;

    public MysqlQueryService(SqlConnectionProvider sqlConnectionProvider) {
        this.sqlConnectionProvider = sqlConnectionProvider;
    }

    @Override
    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public Integer getGeneratedId(Connection connection, PreparedStatement insert) throws SQLException {
        ResultSet generatedKeys;
        generatedKeys = insert.getGeneratedKeys();
        int generatedKey = -1;
        if (generatedKeys.next()) {
            generatedKey = generatedKeys.getInt(1);
        }
        return generatedKey;
    }
}
