package net.shortninja.staffplus.core.domain.player.ip.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpRecord;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractSqlPlayerIpRepository implements PlayerIpRepository {

    private final SqlConnectionProvider sqlConnectionProvider;

    public AbstractSqlPlayerIpRepository(SqlConnectionProvider sqlConnectionProvider) {
        this.sqlConnectionProvider = sqlConnectionProvider;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public void save(UUID playerUuid, String playerName, String ip) {
        try (Connection sql = getConnection();
             PreparedStatement delete = sql.prepareStatement("DELETE FROM sp_player_ips WHERE player_uuid = ? AND ip=?;");
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_player_ips(player_uuid, player_name, ip, ip_numeric, timestamp) VALUES (?,?,?,?,?);");
             ) {
            sql.setAutoCommit(false);
            delete.setString(1, playerUuid.toString());
            delete.setString(2, ip);
            delete.executeUpdate();

            insert.setString(1, playerUuid.toString());
            insert.setString(2, playerName);
            insert.setString(3, ip);
            insert.setLong(4, JavaUtils.convertIp(ip));
            insert.setLong(5, System.currentTimeMillis());
            insert.executeUpdate();
            sql.commit();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<String> getIps(UUID playerUuid) {
        List<String> ips = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT ip FROM sp_player_ips WHERE player_uuid = ?")) {
            ps.setString(1, playerUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ips.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return ips;
    }

    @Override
    public List<PlayerIpRecord> getAllIpRecords() {
        List<PlayerIpRecord> ips = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT ip, player_uuid, player_name FROM sp_player_ips;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ips.add(buildPlayerIpRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return ips;
    }

    @Override
    public Optional<String> getLastIp(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT ip FROM sp_player_ips WHERE player_uuid = ? ORDER BY timestamp DESC LIMIT 1")) {
            ps.setString(1, playerUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<PlayerIpRecord> findInSubnet(long lower, long upper) {
        List<PlayerIpRecord> ips = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT ip, player_uuid, player_name FROM sp_player_ips s1 WHERE ip_numeric BETWEEN ? AND ? AND timestamp = (SELECT MAX(timestamp) FROM sp_player_ips s2 WHERE s1.player_uuid = s2.player_uuid);")) {
            ps.setLong(1, lower);
            ps.setLong(2, upper);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ips.add(buildPlayerIpRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return ips;
    }

    @Override
    public List<PlayerIpRecord> findWithIp(long ip) {
        List<PlayerIpRecord> ips = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT ip, player_uuid, player_name FROM sp_player_ips s1 WHERE ip_numeric = ? AND timestamp = (SELECT MAX(timestamp) FROM sp_player_ips s2 WHERE s1.player_uuid = s2.player_uuid);")) {
            ps.setLong(1, ip);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ips.add(buildPlayerIpRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return ips;
    }

    @NotNull
    private PlayerIpRecord buildPlayerIpRecord(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString(2));
        return new PlayerIpRecord(rs.getString(1), playerUuid, rs.getString(3));
    }

}
