package net.shortninja.staffplus.core.application.database;

import be.garagepoort.mcioc.configuration.ConfigProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public abstract class SqlRepository {

    @ConfigProperty("storage.type")
    private String storageType;

    protected Integer getGeneratedId(PreparedStatement insert) throws SQLException {
        ResultSet generatedKeys;
        if (storageType.equalsIgnoreCase("mysql")) {
            generatedKeys = insert.getGeneratedKeys();
        } else {
            generatedKeys = insert.executeQuery("SELECT last_insert_rowid()");
        }
        int generatedKey = -1;
        if (generatedKeys.next()) {
            generatedKey = generatedKeys.getInt(1);
        }
        return generatedKey;
    }

    protected void insertIfPresent(PreparedStatement insert, int i, Optional optional, int sqlType) throws SQLException {
        if (optional.isPresent()) {
            insert.setObject(i, optional.get());
        } else {
            insert.setNull(i, sqlType);
        }
    }
}
