package net.shortninja.staffplus.warn.database;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.ProvidedPlayer;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.infraction.Warning;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractSqlWarnRepository implements WarnRepository, IocContainer.Repository {

    private final UserManager userManager;

    protected AbstractSqlWarnRepository(UserManager userManager) {
        this.userManager = userManager;
    }

    protected abstract Connection getConnection() throws SQLException;

    @Override
    public int getTotalScore(UUID uuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT ifnull(sum(score), 0) sum FROM sp_warnings WHERE Player_UUID = ?")
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("sum");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Warning> getWarnings(UUID uuid) {
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings WHERE Player_UUID = ?")
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return warnings;
    }

    @Override
    public List<Warning> getWarnings() {
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings")
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return warnings;
    }

    @Override
    public void addWarning(Warning warning) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_warnings(Reason, Warner_UUID, Player_UUID, score, severity) " +
                     "VALUES(? ,?, ?, ?, ?);")) {
            insert.setString(1, warning.getReason());
            insert.setString(2, warning.getIssuerUuid().toString());
            insert.setString(3, warning.getUuid().toString());
            insert.setInt(4, warning.getScore());
            insert.setString(5, warning.getSeverity());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeWarnings(UUID playerUuid) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_warnings WHERE Player_UUID = ?");) {
            insert.setString(1, playerUuid.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeWarning(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_warnings WHERE ID = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Warning buildWarning(ResultSet rs) throws SQLException {
        UUID playerUUID = UUID.fromString(rs.getString("Player_UUID"));
        UUID warnerUuid = UUID.fromString(rs.getString("Warner_UUID"));
        int score = rs.getInt("score");
        String severity = rs.getString("severity") == null ? "No Severity" : rs.getString("severity");

        Optional<ProvidedPlayer> warner = userManager.getOnOrOfflinePlayer(warnerUuid);
        String warnerName = warnerUuid.equals(StaffPlus.get().consoleUUID) ? "Console" : warner.map(ProvidedPlayer::getUsername).orElse("Unknown user");
        int id = rs.getInt("ID");

        Optional<ProvidedPlayer> player = userManager.getOnOrOfflinePlayer(playerUUID);
        String name = player.map(ProvidedPlayer::getUsername).orElse("Unknown user");
        return new Warning(playerUUID, name, id, rs.getString("Reason"), warnerName, warnerUuid, System.currentTimeMillis(), score, severity);
    }

}
