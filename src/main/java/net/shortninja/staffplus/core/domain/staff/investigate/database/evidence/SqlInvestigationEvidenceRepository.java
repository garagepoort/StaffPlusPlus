package net.shortninja.staffplus.core.domain.staff.investigate.database.evidence;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.investigate.evidence.Evidence;
import net.shortninja.staffplus.core.domain.staff.investigate.EvidenceEntity;
import net.shortninja.staffplus.core.domain.staff.investigate.Investigation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;

@IocBean
public class SqlInvestigationEvidenceRepository implements InvestigationEvidenceRepository {

    private static final String ID_COLUMN = "ID";
    private static final String INVESTIGATION_ID_COLUMN = "investigation_id";
    private static final String EVIDENCE_ID_COLUMN = "evidence_id";
    private static final String EVIDENCE_TYPE_COLUMN = "evidence_type";
    private static final String EVIDENCE_DESCRIPTION_COLUMN = "description";
    private static final String LINKED_BY_UUID_COLUMN = "linked_by_uuid";
    private static final String CREATION_TIMESTAMP_COLUMN = "timestamp";

    private final PlayerManager playerManager;
    private final SqlConnectionProvider sqlConnectionProvider;
    protected final Options options;

    public SqlInvestigationEvidenceRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options) {
        this.playerManager = playerManager;
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.options = options;
    }

    protected Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public Optional<EvidenceEntity> findLinkedEvidence(Investigation investigation, Evidence evidence) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigation_evidence WHERE investigation_id = ? AND evidence_id = ? AND evidence_type = ?")) {
            ps.setInt(1, investigation.getId());
            ps.setInt(2, evidence.getId());
            ps.setString(3, evidence.getEvidenceType());
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildEvidence(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<EvidenceEntity> find(int id) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigation_evidence WHERE ID = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildEvidence(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public void addEvidence(EvidenceEntity evidenceEntity) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_investigation_evidence(investigation_id, evidence_id, evidence_type, linked_by_uuid, description, timestamp) " +
                 "VALUES(?, ?, ?, ?, ?, ?);")) {
            insert.setInt(1, evidenceEntity.getInvestigationId());
            insert.setInt(2, evidenceEntity.getEvidenceId());
            insert.setString(3, evidenceEntity.getEvidenceType());
            insert.setString(4, evidenceEntity.getLinkedByUuid().toString());
            insert.setString(5, evidenceEntity.getDescription());
            insert.setLong(6, evidenceEntity.getCreationTimestamp());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<EvidenceEntity> getAllEvidence(int investigationId) {
        List<EvidenceEntity> evidences = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigation_evidence WHERE investigation_id = ? ORDER BY timestamp DESC")) {
            ps.setInt(1, investigationId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    evidences.add(buildEvidence(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return evidences;
    }

    @Override
    public List<EvidenceEntity> getAllEvidence(int investigationId, int offset, int amount) {
        List<EvidenceEntity> evidences = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigation_evidence WHERE investigation_id = ? ORDER BY timestamp DESC LIMIT ?,?")) {
            ps.setInt(1, investigationId);
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    evidences.add(buildEvidence(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return evidences;
    }

    @Override
    public void removeEvidence(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_investigation_evidence WHERE ID = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private EvidenceEntity buildEvidence(ResultSet rs) throws SQLException {
        int id = rs.getInt(ID_COLUMN);
        UUID linkedByUuid = UUID.fromString(rs.getString(LINKED_BY_UUID_COLUMN));

        int investigationId = rs.getInt(INVESTIGATION_ID_COLUMN);
        int evidenceId = rs.getInt(EVIDENCE_ID_COLUMN);
        String evidenceType = rs.getString(EVIDENCE_TYPE_COLUMN);
        String description = rs.getString(EVIDENCE_DESCRIPTION_COLUMN);

        return new EvidenceEntity(
            id,
            investigationId,
            evidenceId,
            evidenceType,
            linkedByUuid,
            getPlayerName(linkedByUuid),
            description, rs.getLong(CREATION_TIMESTAMP_COLUMN));
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
