package net.shortninja.staffplus.core.domain.staff.appeals.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.appeals.Appeal;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.session.SppPlayer;
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

    public AbstractSqlAppealRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider) {
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.playerManager = playerManager;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public List<Appeal> getAppeals(int appealableId, AppealableType appealableType) {
        List<Appeal> appeals = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_appeals WHERE appealable_id = ? AND type = ? ORDER BY timestamp DESC")
        ) {
            ps.setInt(1, appealableId);
            ps.setString(2, appealableType.name());
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
    public List<Appeal> getAppeals(int appealableId, int offset, int amount) {
        List<Appeal> appeals = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_appeals WHERE appealable_id = ? ORDER BY timestamp DESC LIMIT ?,?")
        ) {
            ps.setInt(1, appealableId);
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
    public void addAppeal(Appeal appeal, AppealableType appealableType) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_appeals(appealable_id, reason, status, appealer_uuid, timestamp, type) " +
                 "VALUES(? ,?, ?, ?, ?, ?);")) {
            insert.setInt(1, appeal.getAppealableId());
            insert.setString(2, appeal.getReason());
            insert.setString(3, appeal.getStatus().name());
            insert.setString(4, appeal.getAppealerUuid().toString());
            insert.setLong(5, appeal.getCreationTimestamp());
            insert.setString(6, appealableType.name());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateAppealStatus(int appealId, UUID resolverUuid, String resolveReason, AppealStatus status, AppealableType appealableType) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_appeals set status=?, resolve_reason=?, resolver_uuid=?, type=? WHERE id=?;")) {
            insert.setString(1, status.name());
            if (resolveReason == null) {
                insert.setNull(2, Types.VARCHAR);
            } else {
                insert.setString(2, resolveReason);
            }
            insert.setString(3, resolverUuid.toString());
            insert.setString(4, appealableType.name());
            insert.setInt(5, appealId);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Optional<Appeal> findAppeal(int appealId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_appeals WHERE id = ?")) {
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
    public int getCountOpenAppeals(AppealableType appealableType, String syncTable, ServerSyncConfig syncConfig) {
        int count;
        String query = "SELECT count(*) as count FROM sp_appeals WHERE status='OPEN' AND type = ? AND appealable_id in (SELECT id from " + syncTable + " " + Constants.getServerNameFilterWithWhere(syncConfig) + ")";
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(query)) {
            ps.setString(1, appealableType.name());
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
    public void deleteAppeals(int appealableId, AppealableType appealableType) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_appeals WHERE appealable_id = ? and type = ?");) {
            insert.setInt(1, appealableId);
            insert.setString(2, appealableType.name());
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
        int appealableId = rs.getInt("appealable_id");
        AppealStatus status = AppealStatus.valueOf(rs.getString("status"));
        AppealableType type = AppealableType.valueOf(rs.getString("type"));

        return new Appeal(
            id,
            appealableId,
            appealerUuid,
            appealerName,
            resolverUuid,
            resolverName,
            rs.getString("reason"),
            resolveReason, status,
            rs.getLong("timestamp"),
            type);
    }
}
