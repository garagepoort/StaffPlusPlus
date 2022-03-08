package net.shortninja.staffplus.core.application.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * The reason for this implementation is to make sure we do not access the sqlite file concurrently.
 */
@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteQueryService implements SqlQueryService {

    private final SqlConnectionProvider sqlConnectionProvider;

    public SqliteQueryService(SqlConnectionProvider sqlConnectionProvider) {
        this.sqlConnectionProvider = sqlConnectionProvider;
    }

    @Override
    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public synchronized int insertQuery(String query, SqlParameterSetter parameterSetter) {
        return SqlQueryService.super.insertQuery(query, parameterSetter);
    }

    @Override
    public synchronized void updateQuery(String query, SqlParameterSetter parameterSetter) {
        SqlQueryService.super.updateQuery(query, parameterSetter);
    }

    @Override
    public synchronized void deleteQuery(String query, SqlParameterSetter parameterSetter) {
        SqlQueryService.super.deleteQuery(query, parameterSetter);
    }

    @Override
    public synchronized <T> Optional<T> findOne(String query, SqlParameterSetter parameterSetter, RowMapper<T> rowMapper) {
        return SqlQueryService.super.findOne(query, parameterSetter, rowMapper);
    }

    @Override
    public synchronized <T> T getOne(String query, SqlParameterSetter parameterSetter, RowMapper<T> rowMapper) {
        return SqlQueryService.super.getOne(query, parameterSetter, rowMapper);
    }

    @Override
    public synchronized <T> List<T> find(String query, RowMapper<T> rowMapper) {
        return SqlQueryService.super.find(query, rowMapper);
    }

    @Override
    public synchronized <T> List<T> find(String query, SqlParameterSetter parameterSetter, RowMapper<T> rowMapper) {
        return SqlQueryService.super.find(query, parameterSetter, rowMapper);
    }

    @Override
    public synchronized Integer getGeneratedId(Connection connection, PreparedStatement insert) throws SQLException {
        ResultSet generatedKeys;
        Statement statement = connection.createStatement();
        generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
        int generatedKey = -1;
        if (generatedKeys.next()) {
            generatedKey = generatedKeys.getInt(1);
        }
        return generatedKey;
    }
}
