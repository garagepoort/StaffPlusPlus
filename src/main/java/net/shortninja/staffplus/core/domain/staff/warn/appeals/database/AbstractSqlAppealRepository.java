package net.shortninja.staffplus.core.domain.staff.warn.appeals.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.warnings.AppealStatus;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;

public abstract class AbstractSqlAppealRepository implements AppealRepository {

    private final PlayerManager playerManager;
    private final SqlConnectionProvider sqlConnectionProvider;
    private final boolean warningSyncEnabled;

    public AbstractSqlAppealRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options) {
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.playerManager = playerManager;
        this.warningSyncEnabled = options.serverSyncConfiguration.isWarningSyncEnabled();
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public List<Appeal> getAppeals(int warningId) {
        List<Appeal> appeals = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warning_appeals WHERE warning_id = ? ORDER BY timestamp DESC")
        ) {
            ps.setInt(1, warningId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appeal appeal = buildAppeal(rs);
                    appeals.add(appeal);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return appeals;
    }

    @Override
    public List<Appeal> getAppeals(int warningId, int offset, int amount) {
        List<Appeal> appeals = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warning_appeals WHERE warning_id = ? ORDER BY timestamp DESC LIMIT ?,?")
        ) {
            ps.setInt(1, warningId);
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appeal appeal = buildAppeal(rs);
                    appeals.add(appeal);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return appeals;
    }

    @Override
    public void addAppeal(Appeal appeal) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_warning_appeals(warning_id, reason, status, appealer_uuid, timestamp) " +
                 "VALUES(? ,?, ?, ?, ?);")) {
            insert.setInt(1, appeal.getAppealableId());
            insert.setString(2, appeal.getReason());
            insert.setString(3, appeal.getStatus().name());
            insert.setString(4, appeal.getAppealerUuid().toString());
            insert.setLong(5, appeal.getCreationTimestamp());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }


    @Override
    public void updateAppealStatus(int appealId, UUID resolverUuid, String resolveReason, AppealStatus status) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_warning_appeals set status=?, resolve_reason=?, resolver_uuid=? WHERE id=?;")) {
            insert.setString(1, status.name());
            if (resolveReason == null) {
                insert.setNull(2, Types.VARCHAR);
            } else {
                insert.setString(2, resolveReason);
            }
            insert.setString(3, resolverUuid.toString());
            insert.setInt(4, appealId);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Optional<Appeal> findAppeal(int appealId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warning_appeals WHERE id = ?")) {
            ps.setInt(1, appealId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildAppeal(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public int getCountOpenAppeals() {
        int count;
        String query = "SELECT count(*) as count FROM sp_warning_appeals WHERE status='OPEN'";
        if (!warningSyncEnabled) {
            query += " AND warning_id in (SELECT id from sp_warnings " + Constants.getServerNameFilterWithWhere(warningSyncEnabled) + ")";
        }
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return count;
    }

    @Override
    public void deleteAppealsForWarning(int warningId) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_warning_appeals WHERE warning_id = ? ");) {
            insert.setInt(1, warningId);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private Appeal buildAppeal(ResultSet rs) throws SQLException {
        UUID appealerUuid = UUID.fromString(rs.getString("appealer_uuid"));
        Optional<SppPlayer> appealer = playerManager.getOnOrOfflinePlayer(appealerUuid);
        String appealerName = appealerUuid.equals(CONSOLE_UUID) ? "Console" : appealer.map(SppPlayer::getUsername).orElse("Unknown user");

        String resolveReason = rs.getString("resolve_reason");
        String resolverStringUuid = rs.getString("resolver_uuid");
        UUID resolverUuid = null;
        String resolverName = null;
        if (StringUtils.isNotEmpty(resolverStringUuid)) {
            resolverUuid = UUID.fromString(resolverStringUuid);
            Optional<SppPlayer> resolver = playerManager.getOnOrOfflinePlayer(resolverUuid);
            resolverName = resolverUuid.equals(CONSOLE_UUID) ? "Console" : resolver.map(SppPlayer::getUsername).orElse("Unknown user");
        }

        int id = rs.getInt("ID");
        int warningId = rs.getInt("warning_id");
        AppealStatus status = AppealStatus.valueOf(rs.getString("status"));

        return new Appeal(
            id,
            warningId,
            appealerUuid,
            appealerName,
            resolverUuid,
            resolverName,
            rs.getString("reason"),
            resolveReason, status,
            rs.getLong("timestamp"));
    }

}
