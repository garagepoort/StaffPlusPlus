package net.shortninja.staffplus.session.database;

import net.shortninja.staffplus.unordered.VanishType;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractSqlSessionsRepository implements SessionsRepository {


    protected abstract Connection getConnection() throws SQLException;


    @Override
    public void update(SessionEntity sessionEntity) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_sessions set vanish_type=?, in_staff_mode=? WHERE ID=?")) {
            insert.setString(1, sessionEntity.getVanishType().toString());
            insert.setBoolean(2, sessionEntity.getStaffMode());
            insert.setInt(3, sessionEntity.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SessionEntity> findSession(UUID uuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_sessions WHERE player_uuid = ?")) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildSessionEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private SessionEntity buildSessionEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        VanishType vanishType = VanishType.valueOf(rs.getString("vanish_type"));
        boolean inStaffMode = rs.getBoolean("in_staff_mode");
        return new SessionEntity(
            id,
            playerUuid,
            vanishType,
            inStaffMode
        );
    }

}
