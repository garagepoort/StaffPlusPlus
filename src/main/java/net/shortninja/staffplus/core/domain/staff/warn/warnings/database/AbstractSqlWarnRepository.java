package net.shortninja.staffplus.core.domain.staff.warn.warnings.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.Appeal;
import net.shortninja.staffplus.core.domain.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.Warning;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.warnings.WarningFilters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithWhere;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.insertFilterValues;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.mapFilters;

public abstract class AbstractSqlWarnRepository implements WarnRepository {

    private final PlayerManager playerManager;
    private final AppealRepository appealRepository;
    private final SqlConnectionProvider sqlConnectionProvider;
    protected final Options options;
    private final ServerSyncConfig warningSyncServers;

    public AbstractSqlWarnRepository(PlayerManager playerManager, AppealRepository appealRepository, SqlConnectionProvider sqlConnectionProvider, Options options) {
        this.playerManager = playerManager;
        this.appealRepository = appealRepository;
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.options = options;
        warningSyncServers = options.serverSyncConfiguration.warningSyncServers;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public int getTotalScore(UUID uuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT ifnull(sum(score), 0) sum FROM sp_warnings WHERE Player_UUID = ? AND is_expired=? AND id not in (select appealable_id from sp_appeals where status = 'APPROVED') " + getServerNameFilterWithAnd(warningSyncServers))
        ) {
            ps.setString(1, uuid.toString());
            ps.setBoolean(2, false);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("sum");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int getTotalScore(String name) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT ifnull(sum(score), 0) sum FROM sp_warnings WHERE player_name = ? AND is_expired=? AND id not in (select appealable_id from sp_appeals where status = 'APPROVED') " + getServerNameFilterWithAnd(warningSyncServers))
        ) {
            ps.setString(1, name);
            ps.setBoolean(2, false);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("sum");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Warning> getWarnings(UUID uuid) {
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings WHERE Player_UUID = ? " + getServerNameFilterWithAnd(warningSyncServers))
        ) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return warnings;
    }

    @Override
    public List<Warning> getWarnings(UUID uuid, int offset, int amount) {
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings WHERE Player_UUID = ? " + getServerNameFilterWithAnd(warningSyncServers) + " ORDER BY timestamp DESC LIMIT ?,?")
        ) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return warnings;
    }


    @Override
    public List<Warning> getAllWarnings(int offset, int amount) {
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings " + getServerNameFilterWithWhere(options.serverSyncConfiguration.warningSyncServers) + " ORDER BY timestamp DESC LIMIT ?,?")
        ) {
            ps.setInt(1, offset);
            ps.setInt(2, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return warnings;
    }

    @Override
    public List<Warning> findWarnings(WarningFilters warningFilters, int offset, int amount) {
        List<Warning> warnings = new ArrayList<>();
        String filterQuery = mapFilters(warningFilters, false);

        String query = "SELECT * FROM sp_warnings WHERE 1=1 AND " + filterQuery + " ORDER BY timestamp DESC LIMIT ?,?";
        try (Connection sql = getConnection(); PreparedStatement ps = sql.prepareStatement(query)) {
            int index = insertFilterValues(warningFilters, ps, 1);
            ps.setInt(index, offset);
            ps.setInt(index + 1, amount);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return warnings;
    }

    @Override
    public List<Warning> getAppealedWarnings(int offset, int amount) {
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT sp_warnings.* FROM sp_warnings INNER JOIN sp_appeals appeals on sp_warnings.id = appeals.appealable_id AND appeals.status = 'OPEN' "
                 + getServerNameFilterWithWhere(options.serverSyncConfiguration.warningSyncServers) +
                 " ORDER BY sp_warnings.timestamp DESC LIMIT ?,?")
        ) {
            ps.setInt(1, offset);
            ps.setInt(2, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return warnings;
    }

    @Override
    public long getWarnCount(WarningFilters warningFilters) {
        String filterQuery = mapFilters(warningFilters, true);

        String query = "SELECT count(*) as count FROM sp_warnings WHERE 1=1 " + filterQuery + getServerNameFilterWithAnd(options.serverSyncConfiguration.warningSyncServers);
        try (Connection sql = getConnection(); PreparedStatement ps = sql.prepareStatement(query)) {
            insertFilterValues(warningFilters, ps, 1);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("count");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Warning> getWarnings() {
        String sqlStatement = "SELECT * FROM sp_warnings " + getServerNameFilterWithWhere(warningSyncServers);
        List<Warning> warnings = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(sqlStatement)
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Warning warning = buildWarning(rs);
                    warnings.add(warning);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return warnings;
    }

    @Override
    public void removeWarning(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_warnings WHERE ID = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void expireWarnings(String severityLevel, long timestamp) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_warnings set is_expired=? WHERE is_expired=? AND severity=? AND timestamp < ? " + getServerNameFilterWithAnd(warningSyncServers))) {
            insert.setBoolean(1, true);
            insert.setBoolean(2, false);
            insert.setString(3, severityLevel);
            insert.setLong(4, timestamp);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void expireWarning(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_warnings set is_expired=? WHERE ID=? " + getServerNameFilterWithAnd(warningSyncServers) + ";")) {
            insert.setBoolean(1, true);
            insert.setInt(2, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void markWarningsRead(UUID uniqueId) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_warnings set is_read=? WHERE Player_UUID=? " + getServerNameFilterWithAnd(warningSyncServers) + ";")) {
            insert.setBoolean(1, true);
            insert.setString(2, uniqueId.toString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        Map<UUID, Integer> count = new HashMap<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT Player_UUID, count(*) as count FROM sp_warnings WHERE id not in (select appealable_id from sp_appeals where status = 'APPROVED') " + getServerNameFilterWithAnd(options.serverSyncConfiguration.warningSyncServers) + " GROUP BY Player_UUID ORDER BY count DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    count.put(UUID.fromString(rs.getString("Player_UUID")), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return count;
    }

    @Override
    public Optional<Warning> findWarning(int warningId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_warnings WHERE id = ?")) {
            ps.setInt(1, warningId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildWarning(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }


    private Warning buildWarning(ResultSet rs) throws SQLException {
        UUID playerUUID = UUID.fromString(rs.getString("Player_UUID"));
        UUID warnerUuid = UUID.fromString(rs.getString("Warner_UUID"));
        int score = rs.getInt("score");
        String severity = rs.getString("severity") == null ? "No Severity" : rs.getString("severity");

        Optional<SppPlayer> warner = playerManager.getOnOrOfflinePlayer(warnerUuid);
        String warnerName = warnerUuid.equals(CONSOLE_UUID) ? "Console" : warner.map(SppPlayer::getUsername).orElse("Unknown user");
        int id = rs.getInt("ID");
        boolean read = rs.getBoolean("is_read");
        boolean expired = rs.getBoolean("is_expired");

        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerUUID);
        String name = player.map(SppPlayer::getUsername).orElse("Unknown user");
        String serverName = rs.getString("server_name") == null ? "[Unknown]" : rs.getString("server_name");

        List<Appeal> appeals = appealRepository.getAppeals(id, AppealableType.WARNING);

        return new Warning(playerUUID,
            name,
            id,
            rs.getString("Reason"),
            warnerName,
            warnerUuid,
            rs.getLong("timestamp"),
            score,
            severity,
            read,
            serverName, appeals.size() > 0 ? appeals.get(0) : null, expired);
    }

}
