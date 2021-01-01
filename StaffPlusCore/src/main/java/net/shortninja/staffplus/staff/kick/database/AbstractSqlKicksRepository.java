package net.shortninja.staffplus.staff.kick.database;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.player.SppPlayer;
import net.shortninja.staffplus.staff.kick.Kick;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractSqlKicksRepository implements KicksRepository {

    private final PlayerManager playerManager;

    protected AbstractSqlKicksRepository(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public List<Kick> getKicksForPlayer(UUID playerUuid) {
        List<Kick> kicks = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_kicked_players WHERE player_uuid = ? ORDER BY creation_timestamp DESC")) {
            ps.setString(1, playerUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    kicks.add(buildKick(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return kicks;
    }

    private Kick buildKick(ResultSet rs) throws SQLException {
        UUID playerUuid = UUID.fromString(rs.getString("player_uuid"));
        UUID issuerUuid = UUID.fromString(rs.getString("issuer_uuid"));

        String playerName = getPlayerName(playerUuid);
        String issuerName = getPlayerName(issuerUuid);

        String unbannedByName = null;
        int id = rs.getInt("ID");
        return new Kick(
            id,
            rs.getString("reason"),
            rs.getLong("creation_timestamp"),
            playerName,
            playerUuid,
            issuerName,
            issuerUuid);
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
