package net.shortninja.staffplus.core.domain.staff.altaccountdetect.database.whitelist;

import net.shortninja.staffplus.core.application.database.migrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.staff.altaccountdetect.AltDetectWhitelistedItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractSqlAltDetectWhitelistRepository implements AltDetectWhitelistRepository {

    protected final SqlConnectionProvider sqlConnectionProvider;

    public AbstractSqlAltDetectWhitelistRepository(SqlConnectionProvider sqlConnectionProvider) {
        this.sqlConnectionProvider = sqlConnectionProvider;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public void addWhitelistedItem(UUID playerUuid1, UUID playerUuid2) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_alt_detect_whitelist(player_uuid_1, player_uuid_2) " +
                 "SELECT * FROM (SELECT ?, ?) AS tmp  WHERE NOT EXISTS (SELECT 1 FROM sp_alt_detect_whitelist WHERE (player_uuid_1=? OR player_uuid_2=?) AND (player_uuid_1=? OR player_uuid_2=?));")) {
            insert.setString(1, playerUuid1.toString());
            insert.setString(2, playerUuid2.toString());
            insert.setString(3, playerUuid1.toString());
            insert.setString(4, playerUuid1.toString());
            insert.setString(5, playerUuid2.toString());
            insert.setString(6, playerUuid2.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeWhitelistedItem(UUID playerUuid1, UUID playerUuid2) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_alt_detect_whitelist WHERE (player_uuid_1=? AND player_uuid_2=?) OR (player_uuid_1=? AND player_uuid_2=?)")) {
            insert.setString(1, playerUuid1.toString());
            insert.setString(2, playerUuid2.toString());
            insert.setString(3, playerUuid2.toString());
            insert.setString(4, playerUuid1.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<AltDetectWhitelistedItem> getWhitelistedItems(UUID playerUuid) {
        List<AltDetectWhitelistedItem> whitelistedItems = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_alt_detect_whitelist WHERE player_uuid_1 = ? OR player_uuid_2 = ?")) {
            ps.setString(1, playerUuid.toString());
            ps.setString(2, playerUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AltDetectWhitelistedItem altDetectWhitelistedItem = new AltDetectWhitelistedItem(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(2)));
                    whitelistedItems.add(altDetectWhitelistedItem);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return whitelistedItems;
    }

    @Override
    public List<AltDetectWhitelistedItem> getAllPAgedWhitelistedItems(int offset, int amount) {
        List<AltDetectWhitelistedItem> whitelistedItems = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_alt_detect_whitelist LIMIT ?,?")) {
            ps.setInt(1, offset);
            ps.setInt(2, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AltDetectWhitelistedItem altDetectWhitelistedItem = new AltDetectWhitelistedItem(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(2)));
                    whitelistedItems.add(altDetectWhitelistedItem);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return whitelistedItems;
    }

}
