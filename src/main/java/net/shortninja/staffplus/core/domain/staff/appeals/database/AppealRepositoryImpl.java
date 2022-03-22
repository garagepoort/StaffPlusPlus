package net.shortninja.staffplus.core.domain.staff.appeals.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.domain.staff.appeals.Appeal;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.appeals.AppealStatus;
import net.shortninja.staffplusplus.appeals.AppealableType;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@IocBean
public class AppealRepositoryImpl implements AppealRepository {
    private final QueryBuilderFactory query;

    public AppealRepositoryImpl(QueryBuilderFactory query) {
        this.query = query;
    }

    @Override
    public List<Appeal> getAppeals(int appealableId, AppealableType appealableType) {
        return query.create().find("SELECT * FROM sp_appeals WHERE appealable_id = ? AND type = ? ORDER BY timestamp DESC", (ps) -> {
            ps.setInt(1, appealableId);
            ps.setString(2, appealableType.name());
        }, this::buildAppeal);
    }

    @Override
    public List<Appeal> getAppeals(int appealableId, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_appeals WHERE appealable_id = ? ORDER BY timestamp DESC LIMIT ?,?", (ps) -> {
            ps.setInt(1, appealableId);
            ps.setInt(2, offset);
            ps.setInt(3, amount);
        }, this::buildAppeal);
    }

    @Override
    public void addAppeal(Appeal appeal, AppealableType appealableType) {
        query.create().insertQuery("INSERT INTO sp_appeals(appealable_id, reason, status, appealer_uuid, appealer_name, timestamp, type) " +
            "VALUES(? ,?, ?, ?, ?, ?, ?);", (insert) -> {
            insert.setInt(1, appeal.getAppealableId());
            insert.setString(2, appeal.getReason());
            insert.setString(3, appeal.getStatus().name());
            insert.setString(4, appeal.getAppealerUuid().toString());
            insert.setString(5, appeal.getAppealerName());
            insert.setLong(6, appeal.getCreationTimestamp());
            insert.setString(7, appealableType.name());
        });
    }

    @Override
    public void updateAppealStatus(int appealId, UUID resolverUuid, String resolverName, String resolveReason, AppealStatus status, AppealableType appealableType) {
        query.create().updateQuery("UPDATE sp_appeals set status=?, resolve_reason=?, resolver_uuid=?, resolver_name=?, type=? WHERE id=?;", (insert) -> {
            insert.setString(1, status.name());
            if (resolveReason == null) {
                insert.setNull(2, Types.VARCHAR);
            } else {
                insert.setString(2, resolveReason);
            }
            insert.setString(3, resolverUuid.toString());
            insert.setString(4, resolverName);
            insert.setString(5, appealableType.name());
            insert.setInt(6, appealId);
        });
    }

    @Override
    public Optional<Appeal> findAppeal(int appealId) {
        return query.create().findOne("SELECT * FROM sp_appeals WHERE id = ?",
            (ps) -> ps.setInt(1, appealId),
            this::buildAppeal);
    }

    @Override
    public Optional<Appeal> findAppeal(int appealableId, AppealableType type) {
        return query.create().findOne("SELECT * FROM sp_appeals WHERE appealable_id = ? AND type = ?",
            (ps) -> {
                ps.setInt(1, appealableId);
                ps.setString(2, type.name());
            },
            this::buildAppeal);
    }

    @Override
    public int getCountOpenAppeals(AppealableType appealableType, String syncTable, ServerSyncConfig syncConfig) {
        return query.create().getOne(
            "SELECT count(*) as count FROM sp_appeals WHERE status='OPEN' AND type = ? AND appealable_id in (SELECT id from " + syncTable + " " + Constants.getServerNameFilterWithWhere(syncConfig) + ")",
            (ps) -> ps.setString(1, appealableType.name()),
            (rs) -> rs.getInt("count"));
    }

    @Override
    public void deleteAppeals(int appealableId, AppealableType appealableType) {
        query.create().deleteQuery("DELETE FROM sp_appeals WHERE appealable_id = ? and type = ?", (insert) -> {
            insert.setInt(1, appealableId);
            insert.setString(2, appealableType.name());
            insert.executeUpdate();
        });
    }

    private Appeal buildAppeal(ResultSet rs) throws SQLException {
        UUID appealerUuid = UUID.fromString(rs.getString("appealer_uuid"));
        String appealerName = rs.getString("appealer_name");

        String resolveReason = rs.getString("resolve_reason");
        String resolverStringUuid = rs.getString("resolver_uuid");
        UUID resolverUuid = null;
        String resolverName = null;
        if (StringUtils.isNotEmpty(resolverStringUuid)) {
            resolverUuid = UUID.fromString(resolverStringUuid);
            resolverName = rs.getString("resolver_name");
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
