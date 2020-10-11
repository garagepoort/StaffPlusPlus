package net.shortninja.staffplus.staff.altaccountdetect.database.ipcheck;

import net.shortninja.staffplus.IocContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractSqlPlayerIpRepository implements PlayerIpRepository, IocContainer.Repository {

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public void save(UUID playerUuid, String ip) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_player_ips(player_uuid, ip) " +
                 "SELECT * FROM (SELECT ?, ?) AS tmp  WHERE NOT EXISTS (SELECT 1 FROM sp_player_ips WHERE player_uuid=? AND ip=?);")) {
            insert.setString(1, playerUuid.toString());
            insert.setString(2, ip);
            insert.setString(3, playerUuid.toString());
            insert.setString(4, ip);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return ips;
    }

}
