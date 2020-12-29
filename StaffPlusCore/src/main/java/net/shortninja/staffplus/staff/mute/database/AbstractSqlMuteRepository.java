package net.shortninja.staffplus.staff.mute.database;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.staff.mute.Mute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class AbstractSqlMuteRepository implements MuteRepository {

    private final PlayerManager playerManager;

    protected AbstractSqlMuteRepository(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public List<Mute> getActiveMutes(int offset, int amount) {
        List<Mute> mutes = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE end_timestamp IS NULL OR end_timestamp > ? ORDER BY creation_timestamp DESC LIMIT ?,?")) {
            ps.setLong(1, System.currentTimeMillis());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mutes.add(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mutes;
    }

    @Override
    public List<Mute> getAllActiveMutes(List<String> playerUuids) {
        List<Mute> mutes = new ArrayList<>();
        List<String> questionMarks = playerUuids.stream().map(p -> "?").collect(Collectors.toList());

        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(String.format("SELECT * FROM sp_muted_players WHERE end_timestamp IS NULL OR end_timestamp > ? AND player_uuid IN (%s)", String.join(", ", questionMarks)))) {
            ps.setLong(1, System.currentTimeMillis());
            int index =2;
            for( String uuid : playerUuids ) {
                ps.setString(  index, uuid);
                index++;
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mutes.add(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mutes;
    }

    @Override
    public Optional<Mute> findActiveMute(int muteId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE id = ? AND (end_timestamp IS NULL OR end_timestamp > ?)")) {
            ps.setInt(1, muteId);
            ps.setLong(2, System.currentTimeMillis());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Mute> findActiveMute(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_muted_players WHERE player_uuid = ? AND (end_timestamp IS NULL OR end_timestamp > ?)")) {
            ps.setString(1, playerUuid.toString());
            ps.setLong(2, System.currentTimeMillis());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildMute(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void update(Mute mute) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_muted_players set unmuted_by_uuid=?, unmute_reason=?, end_timestamp=? WHERE ID=?")) {
            insert.setString(1, mute.getUnmutedByUuid().toString());
            insert.setString(2, mute.getUnmuteReason());
            insert.setLong(3, System.currentTimeMillis());
            insert.setInt(4, mute.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Mute buildMute(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        UUID issuerUuid = UUID.fromString(rs.getString("issuer_uuid"));
        UUID unmutedByUUID = rs.getString("unmuted_by_uuid") != null ? UUID.fromString(rs.getString("unmuted_by_uuid")) : null;

        String playerName = getPlayerName(playerUuid);
        String issuerName = getPlayerName(issuerUuid);

        String unmutedByName = null;
        if(unmutedByUUID != null) {
            unmutedByName = getPlayerName(unmutedByUUID);
        }

        int id = rs.getInt("ID");
        Long endTimestamp = rs.getLong("end_timestamp");
        endTimestamp = rs.wasNull() ? null : endTimestamp;

        return new Mute(
            id,
            rs.getString("reason"),
            rs.getLong("creation_timestamp"),
            endTimestamp,
            playerName,
            playerUuid,
            issuerName,
            issuerUuid,
            unmutedByName,
            unmutedByUUID,
            rs.getString("unmute_reason"));
    }

    private String getPlayerName(UUID uuid) {
        String issuerName;
        if (uuid.equals(StaffPlus.get().consoleUUID)) {
            issuerName = "Console";
        } else {
            Optional<SppPlayer> issuer = playerManager.getOnOrOfflinePlayer(uuid);
            issuerName = issuer.map(SppPlayer::getUsername).orElse(null);
        }
        return issuerName;
    }

}
