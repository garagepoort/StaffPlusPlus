package net.shortninja.staffplus.staff.warn.warnings.database;

import net.shortninja.staffplus.player.PlayerManager;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplus.staff.warn.warnings.Warning;
import net.shortninja.staffplus.util.database.migrations.sqlite.SqlLiteConnection;

import java.sql.*;

public class SqliteWarnRepository extends AbstractSqlWarnRepository {

    public SqliteWarnRepository(PlayerManager playerManager, AppealRepository appealRepository, Options options) {
        super(playerManager, appealRepository, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return SqlLiteConnection.connect();
    }

    @Override
    public int addWarning(Warning warning) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_warnings(Reason, Warner_UUID, Player_UUID, score, severity, timestamp, server_name) " +
                 "VALUES(? ,?, ?, ?, ?, ?, ?);")) {
            sql.setAutoCommit(false);
            insert.setString(1, warning.getReason());
            insert.setString(2, warning.getIssuerUuid().toString());
            insert.setString(3, warning.getTargetUuid().toString());
            insert.setInt(4, warning.getScore());
            insert.setString(5, warning.getSeverity());
            insert.setLong(6, warning.getCreationTimestamp());
            insert.setString(7, options.serverName);
            insert.executeUpdate();
            
            Statement statement = sql.createStatement();
            ResultSet generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }
            sql.commit(); // Commits transaction.

            return generatedKey;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
