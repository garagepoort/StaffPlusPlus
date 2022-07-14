package net.shortninja.staffplus.core.application.queue;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class QueueRepository {
    private final QueryBuilderFactory query;

    public QueueRepository(QueryBuilderFactory query) {
        this.query = query;
    }

    public void updateStatus(int id, QueueStatus status, String statusMessage) {
        query.create().updateQuery("UPDATE sp_queue set status=?, status_message=? WHERE id=?;", (insert) -> {
            insert.setString(1, status.name());
            insert.setString(2, statusMessage);
            insert.setInt(3, id);
        });
    }

    public Optional<QueueMessage> findNextQueueMessage(String processingGroup, ServerSyncConfig serverSyncConfig) {
        UUID processId = UUID.randomUUID();

        query.create().updateQuery("UPDATE sp_queue SET process_id = ? WHERE process_id IS NULL AND `type` LIKE ? " + getServerNameFilterWithAnd(serverSyncConfig) + " ORDER BY timestamp ASC LIMIT 1",
            (ps) -> {
                ps.setString(1, processId.toString());
                ps.setString(2, processingGroup + "%");
            });

        return query.create().findOne("SELECT * FROM sp_queue where process_id=?", (ps) -> ps.setString(1, processId.toString()), this::buildQueueMessage);
    }

    private QueueMessage buildQueueMessage(ResultSet rs) throws SQLException {
        return new QueueMessage(
            rs.getInt("ID"),
            QueueStatus.valueOf(rs.getString("status")),
            rs.getString("type"),
            rs.getString("data"),
            rs.getString("status_message"),
            rs.getLong("timestamp"));
    }
}
