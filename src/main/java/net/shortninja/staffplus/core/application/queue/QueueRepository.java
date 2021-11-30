package net.shortninja.staffplus.core.application.queue;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.database.SqlRepository;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;

@IocBean(conditionalOnProperty = "storage.type=mysql")
public class QueueRepository extends SqlRepository {

    private final SqlConnectionProvider sqlConnectionProvider;

    public QueueRepository(SqlConnectionProvider sqlConnectionProvider) {
        this.sqlConnectionProvider = sqlConnectionProvider;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    public void updateStatus(int id, QueueStatus status, String statusMessage) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_queue set status=?, status_message=? WHERE id=?;")) {
            insert.setString(1, status.name());
            insert.setString(2, statusMessage);
            insert.setInt(3, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public Optional<QueueMessage> findNextQueueMessage(String processingGroup, ServerSyncConfig serverSyncConfig) {
        UUID processId = UUID.randomUUID();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("UPDATE sp_queue SET process_id = ? WHERE process_id IS NULL AND `type` LIKE ? "+ getServerNameFilterWithAnd(serverSyncConfig) + " ORDER BY timestamp ASC LIMIT 1")) {
            ps.setString(1, processId.toString());
            ps.setString(2, processingGroup + "%");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_queue where process_id=?")) {
            ps.setString(1, processId.toString());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildQueueMessage(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
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
