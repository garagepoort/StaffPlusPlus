package net.shortninja.staffplus.core.application.session.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplusplus.vanish.VanishType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@IocBean
public class PlayerSettingsSqlRepositoryImpl implements PlayerSettingsSqlRepository {
    private final QueryBuilderFactory query;

    public PlayerSettingsSqlRepositoryImpl(QueryBuilderFactory query) {
        this.query = query;
    }

    @Override
    public int saveSessions(PlayerSettingsEntity playerSettingsEntity) {
        return query.create().insertQuery("INSERT INTO sp_sessions(player_uuid, vanish_type, in_staff_mode, muted_staff_chat_channels, sound_disabled_staff_chat_channels, staff_mode_name) " +
            "VALUES(?, ?, ?, ?, ?, ?);", (insert) -> {
            insert.setString(1, playerSettingsEntity.getPlayerUuid().toString());
            insert.setString(2, playerSettingsEntity.getVanishType().toString());
            insert.setBoolean(3, playerSettingsEntity.getStaffMode());
            insert.setString(4, String.join(";", playerSettingsEntity.getMutedStaffChatChannels()));
            insert.setString(5, String.join(";", playerSettingsEntity.getSoundDisabledStaffChatChannels()));
            if (playerSettingsEntity.getStaffModeName() == null) {
                insert.setNull(6, Types.VARCHAR);
            } else {
                insert.setString(6, playerSettingsEntity.getStaffModeName());
            }
        });
    }

    @Override
    public void update(PlayerSettingsEntity playerSettingsEntity) {
        query.create().updateQuery("UPDATE sp_sessions set vanish_type=?, in_staff_mode=?, muted_staff_chat_channels=? , sound_disabled_staff_chat_channels=? , staff_mode_name=? WHERE ID=?",
            (insert) -> {
                insert.setString(1, playerSettingsEntity.getVanishType().toString());
                insert.setBoolean(2, playerSettingsEntity.getStaffMode());
                insert.setString(3, String.join(";", playerSettingsEntity.getMutedStaffChatChannels()));
                insert.setString(4, String.join(";", playerSettingsEntity.getSoundDisabledStaffChatChannels()));

                if (playerSettingsEntity.getStaffModeName() == null) {
                    insert.setNull(5, Types.VARCHAR);
                } else {
                    insert.setString(5, playerSettingsEntity.getStaffModeName());
                }
                insert.setInt(6, playerSettingsEntity.getId());
            });
    }

    @Override
    public Optional<PlayerSettingsEntity> findSettings(UUID uuid) {
        return query.create().findOne("SELECT * FROM sp_sessions WHERE player_uuid = ?", (ps) -> {
            ps.setString(1, uuid.toString());
        }, this::buildSessionEntity);
    }

    private PlayerSettingsEntity buildSessionEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        VanishType vanishType = VanishType.valueOf(rs.getString("vanish_type"));
        boolean inStaffMode = rs.getBoolean("in_staff_mode");
        Set<String> staffChatMuted = rs.getString("muted_staff_chat_channels") == null ? new HashSet<>() : Arrays.stream(rs.getString("muted_staff_chat_channels").split(";")).collect(Collectors.toSet());
        Set<String> soundDisabledStaffChatChannels = rs.getString("sound_disabled_staff_chat_channels") == null ? new HashSet<>() : Arrays.stream(rs.getString("sound_disabled_staff_chat_channels").split(";")).collect(Collectors.toSet());
        String staffModeName = rs.getString("staff_mode_name");
        return new PlayerSettingsEntity(
            id,
            playerUuid,
            vanishType,
            inStaffMode,
            staffChatMuted,
            soundDisabledStaffChatChannels,
            staffModeName);
    }
}
