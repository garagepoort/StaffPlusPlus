package net.shortninja.staffplus.core.domain.staff.investigate.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;

public abstract class AbstractSqlInvestigationsRepository implements InvestigationsRepository {

    private static final String ID_COLUMN = "ID";
    private static final String INVESTIGATOR_UUID_COLUMN = "investigator_uuid";
    private static final String INVESTIGATED_UUID_COLUMN = "investigated_uuid";
    private static final String SERVER_NAME_COLUMN = "server_name";
    private static final String CREATION_TIMESTAMP_COLUMN = "creation_timestamp";
    private static final String CONCLUSION_TIMESTAMP_COLUMN = "conclusion_timestamp";

    private final PlayerManager playerManager;
    private final SqlConnectionProvider sqlConnectionProvider;
    protected final Options options;
    private final String serverNameFilter;

    public AbstractSqlInvestigationsRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options) {
        this.playerManager = playerManager;
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.options = options;
        serverNameFilter = !options.serverSyncConfiguration.isInvestigationSyncEnabled() ? "AND (server_name='" + options.serverName + "')" : "";
    }

    protected Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public void updateInvestigation(Investigation investigation) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("UPDATE sp_investigations set status=?, conclusion_timestamp=?, investigator_uuid=? WHERE ID=? " + serverNameFilter + ";")) {
            insert.setString(1, investigation.getStatus().name());
            if (investigation.getConclusionDate().isPresent()) {
                insert.setLong(2, investigation.getConclusionTimestamp().get());
            } else {
                insert.setNull(2, Types.BIGINT);
            }
            insert.setString(3, investigation.getInvestigatorUuid().toString());
            insert.setInt(4, investigation.getId());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Optional<Investigation> getOpenedInvestigationForInvestigator(UUID investigatorUuid) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigations WHERE investigator_uuid = ? AND status=? " + serverNameFilter + " ORDER BY creation_timestamp DESC")) {
            ps.setString(1, investigatorUuid.toString());
            ps.setString(2, InvestigationStatus.OPEN.name());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildInvestigation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Investigation> findInvestigation(int id) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigations WHERE id = ? " + serverNameFilter + " ORDER BY creation_timestamp DESC")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildInvestigation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Investigation> getInvestigationForInvestigated(UUID investigatedUuid, List<InvestigationStatus> investigationStatuses) {
        if(investigationStatuses.isEmpty()) {
            return Optional.empty();
        }

        List<String> questionMarks = investigationStatuses.stream().map(p -> "?").collect(Collectors.toList());
        String query = "SELECT * FROM sp_investigations WHERE investigated_uuid = ? AND status in (%s) " + serverNameFilter + " ORDER BY creation_timestamp DESC";

        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement(String.format(query, String.join(", ", questionMarks)))) {
            ps.setString(1, investigatedUuid.toString());
            int index = 2;
            for (InvestigationStatus status : investigationStatuses) {
                ps.setString(index, status.name());
                index++;
            }

            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildInvestigation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Investigation> getInvestigationsForInvestigator(UUID investigatorUuid) {
        List<Investigation> investigations = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigations WHERE investigator_uuid = ? " + serverNameFilter + " ORDER BY creation_timestamp DESC")) {
            ps.setString(1, investigatorUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    investigations.add(buildInvestigation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return investigations;
    }

    @Override
    public List<Investigation> getInvestigationsForInvestigated(UUID investigatedUuid) {
        List<Investigation> investigations = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigations WHERE investigated_uuid = ? " + serverNameFilter + " ORDER BY creation_timestamp DESC")) {
            ps.setString(1, investigatedUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    investigations.add(buildInvestigation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return investigations;
    }

    @Override
    public List<Investigation> getAllInvestigations(int offset, int amount) {
        List<Investigation> investigations = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigations " + Constants.getServerNameFilterWithWhere(options.serverSyncConfiguration.isInvestigationSyncEnabled()) + " ORDER BY creation_timestamp DESC LIMIT ?,?")
        ) {
            ps.setInt(1, offset);
            ps.setInt(2, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    investigations.add(buildInvestigation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return investigations;
    }

    @Override
    public List<Investigation> getInvestigationsForInvestigated(UUID investigatedUuid, int offset, int amount) {
        List<Investigation> investigations = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigations WHERE investigated_uuid = ? " + serverNameFilter + " ORDER BY creation_timestamp DESC LIMIT ?,?")) {
            ps.setString(1, investigatedUuid.toString());
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    investigations.add(buildInvestigation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return investigations;
    }


    private Investigation buildInvestigation(ResultSet rs) throws SQLException {
        UUID investigatorUuid = UUID.fromString(rs.getString(INVESTIGATOR_UUID_COLUMN));
        UUID investigatedUuid = UUID.fromString(rs.getString(INVESTIGATED_UUID_COLUMN));

        String investigatorName = getPlayerName(investigatorUuid);
        String investigatedName = getPlayerName(investigatedUuid);

        int id = rs.getInt(ID_COLUMN);
        String serverName = rs.getString(SERVER_NAME_COLUMN);
        InvestigationStatus investigationStatus = InvestigationStatus.valueOf(rs.getString("status"));

        return new Investigation(
            id,
            rs.getLong(CREATION_TIMESTAMP_COLUMN),
            rs.getLong(CONCLUSION_TIMESTAMP_COLUMN),
            investigatorName,
            investigatorUuid,
            investigatedName,
            investigatedUuid,
            serverName,
            investigationStatus);
    }

    private String getPlayerName(UUID uuid) {
        String issuerName;
        if (uuid.equals(CONSOLE_UUID)) {
            issuerName = "Console";
        } else {
            Optional<SppPlayer> issuer = playerManager.getOnOrOfflinePlayer(uuid);
            issuerName = issuer.map(SppPlayer::getUsername).orElse("[Unknown player]");
        }
        return issuerName;
    }

}
