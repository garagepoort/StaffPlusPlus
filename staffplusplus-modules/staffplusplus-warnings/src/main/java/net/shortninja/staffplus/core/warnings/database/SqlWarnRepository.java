package net.shortninja.staffplus.core.warnings.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.appeals.Appeal;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplus.core.warnings.Warning;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.AppealableType;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.warnings.WarningFilters;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithWhere;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.insertFilterValues;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.mapFilters;

@IocBean
public class SqlWarnRepository implements WarnRepository {

    private final PlayerManager playerManager;
    private final Options options;
    private final ServerSyncConfig warningSyncServers;
    private final QueryBuilderFactory query;

    public SqlWarnRepository(PlayerManager playerManager, Options options, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.options = options;
        warningSyncServers = options.serverSyncConfiguration.warningSyncServers;
        this.query = query;
    }

    @Override
    public int addWarning(Warning warning) {
        return query.create().insertQuery("INSERT INTO sp_warnings(Reason, Warner_UUID, warner_name, Player_UUID, player_name, score, severity, timestamp, server_name) " +
                "VALUES(? ,?, ?, ?, ?, ?, ?, ?, ?);",
            (insert) -> {
                insert.setString(1, warning.getReason());
                insert.setString(2, warning.getIssuerUuid().toString());
                insert.setString(3, warning.getIssuerName());
                insert.setString(4, warning.getTargetUuid().toString());
                insert.setString(5, warning.getTargetName());
                insert.setInt(6, warning.getScore());
                insert.setString(7, warning.getSeverity());
                insert.setLong(8, warning.getCreationTimestamp());
                insert.setString(9, options.serverName);
            });
    }

    @Override
    public int getTotalScore(UUID uuid) {
        return query.create().getOne(
            "SELECT ifnull(sum(score), 0) sum FROM sp_warnings " +
                "WHERE Player_UUID = ? AND is_expired=? AND id not in (select appealable_id from sp_appeals where status = 'APPROVED' AND type='WARNING') " + getServerNameFilterWithAnd(warningSyncServers),
            (ps) -> {
                ps.setString(1, uuid.toString());
                ps.setBoolean(2, false);
            },
            (rs) -> rs.getInt("sum"));
    }

    @Override
    public int getTotalScore(String name) {
        return query.create().getOne(
            "SELECT ifnull(sum(score), 0) sum FROM sp_warnings " +
                "WHERE player_name = ? AND is_expired=? AND id not in (select appealable_id from sp_appeals where status = 'APPROVED' AND type='WARNING') " + getServerNameFilterWithAnd(warningSyncServers),
            (ps) -> {
                ps.setString(1, name);
                ps.setBoolean(2, false);
            },
            (rs) -> rs.getInt("sum"));
    }

    @Override
    public List<Warning> getWarnings(UUID uuid) {
        return query.create().find(
            "SELECT * FROM sp_warnings w " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = w.id AND type = 'WARNING' LIMIT 1) " +
                "WHERE w.Player_UUID = ? " + getServerNameFilterWithAnd(warningSyncServers),
            (ps) -> ps.setString(1, uuid.toString()),
            this::buildWarning);
    }

    @Override
    public List<Warning> getWarnings(UUID uuid, int offset, int amount) {
        return query.create().find(
            "SELECT * FROM sp_warnings w " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = w.id AND type = 'WARNING' LIMIT 1) " +
                "WHERE Player_UUID = ? " + getServerNameFilterWithAnd(warningSyncServers) + " ORDER BY w.timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, uuid.toString());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            }, this::buildWarning);
    }

    @Override
    public List<Warning> getAllWarnings(int offset, int amount) {
        return query.create().find(
            "SELECT * FROM sp_warnings w " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = w.id AND type = 'WARNING' LIMIT 1) "
                + getServerNameFilterWithWhere(options.serverSyncConfiguration.warningSyncServers) + " ORDER BY w.timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setInt(1, offset);
                ps.setInt(2, amount);
            }, this::buildWarning);
    }

    @Override
    public List<Warning> findWarnings(WarningFilters warningFilters, int offset, int amount) {
        String filterQuery = mapFilters(warningFilters, false);
        String query =
            "SELECT * FROM sp_warnings w " +
                "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = w.id AND type = 'WARNING' LIMIT 1) " +
                " WHERE 1=1 AND " + filterQuery + " ORDER BY w.timestamp DESC LIMIT ?,?";
        return this.query.create().find(query,
            (ps) -> {
                int index = insertFilterValues(warningFilters, ps, 1);
                ps.setInt(index, offset);
                ps.setInt(index + 1, amount);
            }, this::buildWarning);
    }

    @Override
    public List<Warning> getAppealedWarnings(int offset, int amount) {
        return this.query.create().find(
            "SELECT * FROM sp_warnings INNER JOIN sp_appeals appeals on sp_warnings.id = appeals.appealable_id AND appeals.status = 'OPEN' AND appeals.type = ?"
                + getServerNameFilterWithWhere(options.serverSyncConfiguration.warningSyncServers) +
                " ORDER BY sp_warnings.timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, AppealableType.WARNING.name());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            }, this::buildWarning);
    }

    @Override
    public long getWarnCount(WarningFilters warningFilters) {
        String filterQuery = mapFilters(warningFilters, true);
        String query = "SELECT count(*) as count FROM sp_warnings w WHERE 1=1 " + filterQuery + getServerNameFilterWithAnd(options.serverSyncConfiguration.warningSyncServers);
        return this.query.create().getOne(query, (ps) -> insertFilterValues(warningFilters, ps, 1), (rs) -> rs.getLong("count"));
    }

    @Override
    public List<Warning> getWarnings() {
        return query.create().find(
            "SELECT * FROM sp_warnings w " +
            "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = w.id AND type = 'WARNING' LIMIT 1) "
            + getServerNameFilterWithWhere(warningSyncServers), this::buildWarning);
    }

    @Override
    public void removeWarning(int id) {
        query.create().deleteQuery("DELETE FROM sp_warnings WHERE ID = ?", (insert) -> insert.setInt(1, id));
    }

    @Override
    public void expireWarnings(String severityLevel, long timestamp) {
        query.create().updateQuery(
            "UPDATE sp_warnings set is_expired=? WHERE is_expired=? AND severity=? AND timestamp < ? " + getServerNameFilterWithAnd(warningSyncServers),
            (statement) -> {
                statement.setBoolean(1, true);
                statement.setBoolean(2, false);
                statement.setString(3, severityLevel);
                statement.setLong(4, timestamp);
            });
    }

    @Override
    public void expireWarning(int id) {
        query.create().updateQuery(
            "UPDATE sp_warnings set is_expired=? WHERE ID=? " + getServerNameFilterWithAnd(warningSyncServers) + ";",
            (insert) -> {
                insert.setBoolean(1, true);
                insert.setInt(2, id);
            });
    }

    @Override
    public void markWarningsRead(UUID uniqueId) {
        query.create().updateQuery(
            "UPDATE sp_warnings set is_read=? WHERE Player_UUID=? " + getServerNameFilterWithAnd(warningSyncServers) + ";",
            (insert) -> {
                insert.setBoolean(1, true);
                insert.setString(2, uniqueId.toString());
            });
    }

    @Override
    public Map<UUID, Integer> getCountByPlayer() {
        return query.create().findMap("SELECT Player_UUID, count(*) as count FROM sp_warnings WHERE id not in (select appealable_id from sp_appeals where status = 'APPROVED' AND type='WARNING') "
                + getServerNameFilterWithAnd(options.serverSyncConfiguration.warningSyncServers)
                + " GROUP BY Player_UUID ORDER BY count DESC",
            rs -> UUID.fromString(rs.getString("Player_UUID")), rs -> rs.getInt("count"));
    }

    @Override
    public Optional<Warning> findWarning(int warningId) {
        return query.create().findOne("SELECT * FROM sp_warnings w " +
            "LEFT JOIN sp_appeals ap ON ap.id = (select id from sp_appeals ap2 WHERE ap2.appealable_id = w.id AND type = 'WARNING' LIMIT 1) " + " WHERE w.id = ?",
            (select) -> select.setInt(1, warningId),
            this::buildWarning);
    }

    private Warning buildWarning(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String reason = rs.getString(2);
        UUID warnerUuid = UUID.fromString(rs.getString(3));
        UUID playerUUID = UUID.fromString(rs.getString(4));
        int score = rs.getInt(5);
        String severity = rs.getString(6) == null ? "No Severity" : rs.getString(6);

        boolean read = rs.getBoolean(7);
        long timestamp = rs.getLong(8);
        String serverName = rs.getString(9) == null ? "[Unknown]" : rs.getString(9);
        boolean expired = rs.getBoolean(10);

        Optional<SppPlayer> warner = playerManager.getOnOrOfflinePlayer(warnerUuid);
        String warnerName = warnerUuid.equals(CONSOLE_UUID) ? "Console" : warner.map(SppPlayer::getUsername).orElse("Unknown user");
        Optional<SppPlayer> player = playerManager.getOnOrOfflinePlayer(playerUUID);
        String name = player.map(SppPlayer::getUsername).orElse("Unknown user");

        Appeal appeal = null;
        Integer appealId = rs.getInt(13);
        appealId = rs.wasNull() ? null : appealId;
        if(appealId != null) {
            int appealableId = rs.getInt(14);
            UUID appealerUuid = UUID.fromString(rs.getString(15));
            String resolverStringUuid = rs.getString(16);
            String appealReason = rs.getString(17);
            String resolveReason = rs.getString(18);
            AppealStatus status = AppealStatus.valueOf(rs.getString(19));
            long appealTimestamp = rs.getLong(20);
            AppealableType type = AppealableType.valueOf(rs.getString(21));
            String appealerName = rs.getString(22);

            UUID resolverUuid = null;
            String resolverName = null;
            if (StringUtils.isNotEmpty(resolverStringUuid)) {
                resolverUuid = UUID.fromString(resolverStringUuid);
                resolverName = rs.getString(23);
            }


            appeal = new Appeal(
                id,
                appealableId,
                appealerUuid,
                appealerName,
                resolverUuid,
                resolverName,
                appealReason,
                resolveReason, status,
                appealTimestamp,
                type);
        }

        return new Warning(playerUUID,
            name,
            id,
            reason,
            warnerName,
            warnerUuid,
            timestamp,
            score,
            severity,
            read,
            serverName,
            appeal,
            expired);
    }
}
