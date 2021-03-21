package net.shortninja.staffplus.domain.staff.warn.warnings.database;

import net.shortninja.staffplus.application.database.migrations.mysql.MySQLConnection;
import net.shortninja.staffplus.common.config.Options;
import net.shortninja.staffplus.common.exceptions.DatabaseException;
import net.shortninja.staffplus.domain.player.PlayerManager;
import net.shortninja.staffplus.domain.staff.warn.appeals.database.AppealRepository;
import net.shortninja.staffplus.domain.staff.warn.warnings.Warning;

import java.sql.*;

public class MysqlWarnRepository extends AbstractSqlWarnRepository {

    public MysqlWarnRepository(PlayerManager playerManager, AppealRepository appealRepository, Options options) {
        super(playerManager, appealRepository, options);
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return MySQLConnection.getConnection();
    }

    @Override
    public int addWarning(Warning warning) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_warnings(Reason, Warner_UUID, Player_UUID, score, severity, timestamp, server_name) " +
                 "VALUES(? ,?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, warning.getReason());
            insert.setString(2, warning.getIssuerUuid().toString());
            insert.setString(3, warning.getTargetUuid().toString());
            insert.setInt(4, warning.getScore());
            insert.setString(5, warning.getSeverity());
            insert.setLong(6, warning.getCreationTimestamp());
            insert.setString(7, options.serverName);
            insert.executeUpdate();

            ResultSet generatedKeys = insert.getGeneratedKeys();
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }

            return generatedKey;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
