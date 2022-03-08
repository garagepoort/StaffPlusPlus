package net.shortninja.staffplus.core.application.database;

import net.shortninja.staffplus.core.common.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SqlQueryService {

    Connection getConnection();

    default <T> Optional<T> findOne(String query, SqlParameterSetter parameterSetter, RowMapper<T> rowMapper) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(query)) {
            parameterSetter.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(rowMapper.apply(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    default <T> T getOne(String query, SqlParameterSetter parameterSetter, RowMapper<T> rowMapper) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(query)) {
            parameterSetter.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rowMapper.apply(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    default <T> List<T> find(String query, RowMapper<T> rowMapper) {
        return find(query, (rs) -> {
        }, rowMapper);
    }

    default <T> List<T> find(String query, SqlParameterSetter parameterSetter, RowMapper<T> rowMapper) {
        List<T> results = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(query)
        ) {
            parameterSetter.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    T mappedRow = rowMapper.apply(rs);
                    results.add(mappedRow);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return results;
    }

    default int insertQuery(String query, SqlParameterSetter parameterSetter) {
        try (Connection sql = getConnection()) {
            sql.setAutoCommit(false);
            PreparedStatement insert = sql.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            parameterSetter.accept(insert);
            insert.executeUpdate();
            int id = getGeneratedId(sql, insert);
            sql.setAutoCommit(true);
            return id;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    default void updateQuery(String query, SqlParameterSetter parameterSetter) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement(query)) {
            parameterSetter.accept(insert);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    default void deleteQuery(String query, SqlParameterSetter parameterSetter) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement(query)) {
            parameterSetter.accept(insert);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    Integer getGeneratedId(Connection connection, PreparedStatement insert) throws SQLException;

    interface SqlParameterSetter {
        void accept(PreparedStatement preparedStatement) throws SQLException;
    }

    interface RowMapper<T> {
        T apply(ResultSet rs) throws SQLException;
    }
}
