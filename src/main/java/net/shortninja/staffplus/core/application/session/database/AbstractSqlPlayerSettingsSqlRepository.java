package net.shortninja.staffplus.core.application.session.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplusplus.vanish.VanishType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractSqlPlayerSettingsSqlRepository implements PlayerSettingsSqlRepository {

    private final SqlConnectionProvider sqlConnectionProvider;

    public AbstractSqlPlayerSettingsSqlRepository(SqlConnectionProvider sqlConnectionProvider) {
        this.sqlConnectionProvider = sqlConnectionProvider;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public void update(PlayerSettingsEntity playerSettingsEntity) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_sessions set vanish_type=?, in_staff_mode=?, muted_staff_chat_channels=? , staff_mode_name=? WHERE ID=?")) {
            insert.setString(1, playerSettingsEntity.getVanishType().toString());
            insert.setBoolean(2, playerSettingsEntity.getStaffMode());
            insert.setString(3, String.join(";", playerSettingsEntity.getMutedStaffChatChannels()));
            if (playerSettingsEntity.getStaffModeName() == null) {
                insert.setNull(4, Types.VARCHAR);
            } else {
                insert.setString(4, playerSettingsEntity.getStaffModeName());
            }
            insert.setInt(5, playerSettingsEntity.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Optional<PlayerSettingsEntity> findSettings(UUID uuid) {
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
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    private PlayerSettingsEntity buildSessionEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        VanishType vanishType = VanishType.valueOf(rs.getString("vanish_type"));
        boolean inStaffMode = rs.getBoolean("in_staff_mode");
        Set<String> staffChatMuted = rs.getString("muted_staff_chat_channels") == null ? new HashSet<>() : Arrays.stream(rs.getString("muted_staff_chat_channels").split(";")).collect(Collectors.toSet());
        String staffModeName = rs.getString("staff_mode_name");
        return new PlayerSettingsEntity(
            id,
            playerUuid,
            vanishType,
            inStaffMode,
            staffChatMuted, staffModeName);
    }

}
