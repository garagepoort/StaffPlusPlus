package net.shortninja.staffplus.core.investigate.database.notes;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.investigate.InvestigationNoteEntity;
import net.shortninja.staffplusplus.session.SppPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.CONSOLE_UUID;

@IocBean
public class InvestigationNotesRepositoryImpl implements InvestigationNotesRepository {

    private static final String ID_COLUMN = "ID";
    private static final String INVESTIGATION_ID_COLUMN = "investigation_id";
    private static final String NOTE_COLUMN = "note";
    private static final String NOTED_BY_UUID_COLUMN = "noted_by_uuid";
    private static final String CREATION_TIMESTAMP_COLUMN = "timestamp";

    private final PlayerManager playerManager;
    private final QueryBuilderFactory query;

    public InvestigationNotesRepositoryImpl(PlayerManager playerManager, QueryBuilderFactory query) {
        this.playerManager = playerManager;
        this.query = query;
    }

    @Override
    public Optional<InvestigationNoteEntity> find(int id) {
        return query.create().findOne("SELECT * FROM sp_investigation_notes WHERE ID = ?",
            ps -> ps.setInt(1, id),
            this::buildNote);
    }

    @Override
    public void addNote(InvestigationNoteEntity investigationNoteEntity) {
        query.create().insertQuery("INSERT INTO sp_investigation_notes(investigation_id, noted_by_uuid, note, timestamp) " +
                "VALUES(?, ?, ?, ?);",
            insert -> {
                insert.setInt(1, investigationNoteEntity.getInvestigationId());
                insert.setString(2, investigationNoteEntity.getNotedByUuid().toString());
                insert.setString(3, investigationNoteEntity.getNote());
                insert.setLong(4, investigationNoteEntity.getCreationTimestamp());
            });
    }

    @Override
    public List<InvestigationNoteEntity> getAllNotes(int investigationId) {
        return query.create().find("SELECT * FROM sp_investigation_notes WHERE investigation_id = ? ORDER BY timestamp DESC",
            ps -> ps.setInt(1, investigationId),
            this::buildNote);
    }

    @Override
    public List<InvestigationNoteEntity> getAllNotes(int investigationId, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_investigation_notes WHERE investigation_id = ? ORDER BY timestamp DESC LIMIT ?,?",
            ps -> {
                ps.setInt(1, investigationId);
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            }, this::buildNote);
    }

    @Override
    public void removeNote(int id) {
        query.create().deleteQuery("DELETE FROM sp_investigation_notes WHERE ID = ?", insert -> insert.setInt(1, id));
    }

    private InvestigationNoteEntity buildNote(ResultSet rs) throws SQLException {
        int id = rs.getInt(ID_COLUMN);
        UUID linkedByUuid = UUID.fromString(rs.getString(NOTED_BY_UUID_COLUMN));
        int investigationId = rs.getInt(INVESTIGATION_ID_COLUMN);
        String note = rs.getString(NOTE_COLUMN);

        return new InvestigationNoteEntity(
            id,
            investigationId,
            note,
            linkedByUuid,
            getPlayerName(linkedByUuid),
            rs.getLong(CREATION_TIMESTAMP_COLUMN));
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
