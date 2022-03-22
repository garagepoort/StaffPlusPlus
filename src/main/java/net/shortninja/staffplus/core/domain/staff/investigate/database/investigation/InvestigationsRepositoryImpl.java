package net.shortninja.staffplus.core.domain.staff.investigate.database.investigation;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;
import net.shortninja.staffplus.core.domain.synchronization.ServerSyncConfig;
import net.shortninja.staffplusplus.investigate.InvestigationStatus;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;
import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;

@IocBean
public class InvestigationsRepositoryImpl implements InvestigationsRepository {

    private static final String ID_COLUMN = "ID";
    private static final String INVESTIGATOR_UUID_COLUMN = "investigator_uuid";
    private static final String INVESTIGATED_UUID_COLUMN = "investigated_uuid";
    private static final String SERVER_NAME_COLUMN = "server_name";
    private static final String CREATION_TIMESTAMP_COLUMN = "creation_timestamp";
    private static final String CONCLUSION_TIMESTAMP_COLUMN = "conclusion_timestamp";

    private final PlayerManager playerManager;
    private final Options options;
    private final ServerSyncConfig investigationSyncServers;
    private final QueryBuilderFactory query;

    public InvestigationsRepositoryImpl(PlayerManager playerManager, Options options, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.options = options;
        this.investigationSyncServers = options.serverSyncConfiguration.investigationSyncServers;
        this.query = query;
    }

    @Override
    public int addInvestigation(Investigation investigation) {
        return this.query.create().insertQuery("INSERT INTO sp_investigations(investigator_uuid, investigator_name, investigated_uuid, investigated_name, status, creation_timestamp, server_name) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?);", (insert) -> {
            insert.setString(1, investigation.getInvestigatorUuid().toString());
            insert.setString(2, investigation.getInvestigatorName());
            if (investigation.getInvestigatedUuid().isPresent()) {
                insert.setString(3, investigation.getInvestigatedUuid().get().toString());
                insert.setString(4, investigation.getInvestigatedName().get());
            } else {
                insert.setNull(3, Types.VARCHAR);
                insert.setNull(4, Types.VARCHAR);
            }
            insert.setString(5, investigation.getStatus().name());
            insert.setLong(6, investigation.getCreationTimestamp());
            insert.setString(7, options.serverName);
        });
    }

    @Override
    public void updateInvestigation(Investigation investigation) {
        this.query.create().updateQuery("UPDATE sp_investigations set status=?, conclusion_timestamp=?, investigator_uuid=? WHERE ID=? " + getServerNameFilterWithAnd(investigationSyncServers) + ";", (insert) -> {
            insert.setString(1, investigation.getStatus().name());
            if (investigation.getConclusionDate().isPresent()) {
                insert.setLong(2, investigation.getConclusionTimestamp().get());
            } else {
                insert.setNull(2, Types.BIGINT);
            }
            insert.setString(3, investigation.getInvestigatorUuid().toString());
            insert.setInt(4, investigation.getId());
        });
    }

    @Override
    public void pauseAllInvestigations() {
        this.query.create().updateQuery("UPDATE sp_investigations set status=? WHERE status=? AND server_name=?;", (insert) -> {
            insert.setString(1, InvestigationStatus.PAUSED.name());
            insert.setString(2, InvestigationStatus.OPEN.name());
            insert.setString(3, options.serverName);
        });
    }

    @Override
    public Optional<Investigation> findInvestigation(int id) {
        return this.query.create().findOne(
            "SELECT * FROM sp_investigations WHERE id = ? " + getServerNameFilterWithAnd(investigationSyncServers) + " ORDER BY creation_timestamp DESC",
            (ps) -> ps.setInt(1, id),
            this::buildInvestigation);
    }

    @Override
    public Optional<Investigation> findInvestigationForInvestigated(UUID investigatorUuid, UUID investigatedUuid, List<InvestigationStatus> investigationStatuses) {
        if (investigationStatuses.isEmpty()) {
            return Optional.empty();
        }
        List<String> questionMarks = investigationStatuses.stream().map(p -> "?").collect(Collectors.toList());
        String query = "SELECT * FROM sp_investigations WHERE investigator_uuid = ? AND investigated_uuid = ? AND status in (%s) " + getServerNameFilterWithAnd(investigationSyncServers) + " ORDER BY creation_timestamp DESC";

        return this.query.create().findOne(String.format(query, String.join(", ", questionMarks)),
            (ps) -> {
                ps.setString(1, investigatorUuid.toString());
                ps.setString(2, investigatedUuid.toString());
                int index = 3;
                for (InvestigationStatus status : investigationStatuses) {
                    ps.setString(index, status.name());
                    index++;
                }
            }, this::buildInvestigation);
    }

    @Override
    public List<Investigation> findAllInvestigationForInvestigated(UUID investigatedUuid, List<InvestigationStatus> investigationStatuses) {
        return getInvestigations(investigatedUuid, investigationStatuses, "SELECT * FROM sp_investigations WHERE investigated_uuid = ? ");
    }

    @Override
    public List<Investigation> findAllInvestigationsForInvestigator(UUID investigatorUuid, List<InvestigationStatus> investigationStatuses) {
        return getInvestigations(investigatorUuid, investigationStatuses, "SELECT * FROM sp_investigations WHERE investigator_uuid = ? ");
    }

    @Override
    public List<Investigation> findAllInvestigationsForInvestigator(UUID investigatorUuid, List<InvestigationStatus> investigationStatuses, int offset, int amount) {
        return getInvestigationsPaged(investigatorUuid, investigationStatuses, "SELECT * FROM sp_investigations WHERE investigator_uuid = ? ", offset, amount);
    }

    @Override
    public Optional<Investigation> getInvestigationForInvestigator(UUID investigatorUuid, List<InvestigationStatus> investigationStatuses) {
        if (investigationStatuses.isEmpty()) {
            return Optional.empty();
        }

        List<String> questionMarks = investigationStatuses.stream().map(p -> "?").collect(Collectors.toList());
        String query = "SELECT * FROM sp_investigations WHERE investigator_uuid = ? AND status in (%s) " + getServerNameFilterWithAnd(investigationSyncServers) + " ORDER BY creation_timestamp DESC";

        return this.query.create().findOne(String.format(query, String.join(", ", questionMarks)),
            (ps) -> {
                ps.setString(1, investigatorUuid.toString());
                int index = 2;
                for (InvestigationStatus status : investigationStatuses) {
                    ps.setString(index, status.name());
                    index++;
                }
            }, this::buildInvestigation);
    }

    @Override
    public List<Investigation> getInvestigationsForInvestigated(UUID investigatedUuid, List<InvestigationStatus> investigationStatuses) {
        return getInvestigations(investigatedUuid, investigationStatuses, "SELECT * FROM sp_investigations WHERE investigated_uuid = ? ");
    }

    @Override
    public List<Investigation> getAllInvestigations(int offset, int amount) {
        return this.query.create().find("SELECT * FROM sp_investigations " + Constants.getServerNameFilterWithWhere(options.serverSyncConfiguration.investigationSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setInt(1, offset);
                ps.setInt(2, amount);
            }, this::buildInvestigation);
    }

    @Override
    public List<Investigation> getInvestigationsForInvestigated(UUID investigatedUuid, int offset, int amount) {
        return this.query.create().find("SELECT * FROM sp_investigations WHERE investigated_uuid = ? " + getServerNameFilterWithAnd(investigationSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setString(1, investigatedUuid.toString());
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            }, this::buildInvestigation);
    }

    @NotNull
    private List<Investigation> getInvestigations(UUID investigatorUuid, List<InvestigationStatus> investigationStatuses, String s) {
        if (investigationStatuses.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> questionMarks = investigationStatuses.stream().map(p -> "?").collect(Collectors.toList());
        String query = s + " AND status in (%s) " + getServerNameFilterWithAnd(investigationSyncServers) + " ORDER BY creation_timestamp DESC";

        return this.query.create().find(String.format(query, String.join(", ", questionMarks)),
            (ps) -> {
                ps.setString(1, investigatorUuid.toString());
                int index = 2;
                for (InvestigationStatus status : investigationStatuses) {
                    ps.setString(index, status.name());
                    index++;
                }
            }, this::buildInvestigation);
    }

    @NotNull
    private List<Investigation> getInvestigationsPaged(UUID investigatorUuid, List<InvestigationStatus> investigationStatuses, String s, int offset, int amount) {
        if (investigationStatuses.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> questionMarks = investigationStatuses.stream().map(p -> "?").collect(Collectors.toList());
        String query = s + " AND status in (%s) " + getServerNameFilterWithAnd(investigationSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?";

        return this.query.create().find(String.format(query, String.join(", ", questionMarks)),
            (ps) -> {
                ps.setString(1, investigatorUuid.toString());
                int index = 2;
                for (InvestigationStatus status : investigationStatuses) {
                    ps.setString(index, status.name());
                    index++;
                }

                ps.setInt(index, offset);
                ps.setInt(index + 1, amount);
            }, this::buildInvestigation);
    }

    private Investigation buildInvestigation(ResultSet rs) throws SQLException {
        UUID investigatorUuid = UUID.fromString(rs.getString(INVESTIGATOR_UUID_COLUMN));
        UUID investigatedUuid = StringUtils.isEmpty(rs.getString(INVESTIGATED_UUID_COLUMN)) ? null : UUID.fromString(rs.getString(INVESTIGATED_UUID_COLUMN));

        String investigatorName = getPlayerName(investigatorUuid);
        String investigatedName = investigatedUuid != null ? getPlayerName(investigatedUuid) : null;

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
