package net.shortninja.staffplus.core.domain.staff.investigate.database.notes;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.investigate.NoteEntity;
import net.shortninja.staffplusplus.session.SppPlayer;

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
public class SqlInvestigationNotesRepository implements InvestigationNotesRepository {

    private static final String ID_COLUMN = "ID";
    private static final String INVESTIGATION_ID_COLUMN = "investigation_id";
    private static final String NOTE_COLUMN = "note";
    private static final String NOTED_BY_UUID_COLUMN = "noted_by_uuid";
    private static final String CREATION_TIMESTAMP_COLUMN = "timestamp";

    private final PlayerManager playerManager;
    private final SqlConnectionProvider sqlConnectionProvider;
    protected final Options options;

    public SqlInvestigationNotesRepository(PlayerManager playerManager, SqlConnectionProvider sqlConnectionProvider, Options options) {
        this.playerManager = playerManager;
        this.sqlConnectionProvider = sqlConnectionProvider;
        this.options = options;
    }

    protected Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }


    @Override
    public Optional<NoteEntity> find(int id) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigation_notes WHERE ID = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                boolean first = rs.next();
                if (first) {
                    return Optional.of(buildNote(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public void addNote(NoteEntity noteEntity) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("INSERT INTO sp_investigation_notes(investigation_id, noted_by_uuid, note, timestamp) " +
                 "VALUES(?, ?, ?, ?);")) {
            insert.setInt(1, noteEntity.getInvestigationId());
            insert.setString(2, noteEntity.getNotedByUuid().toString());
            insert.setString(3, noteEntity.getNote());
            insert.setLong(4, noteEntity.getCreationTimestamp());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<NoteEntity> getAllNotes(int investigationId) {
        List<NoteEntity> evidences = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigation_notes WHERE investigation_id = ? ORDER BY timestamp DESC")) {
            ps.setInt(1, investigationId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    evidences.add(buildNote(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return evidences;
    }

    @Override
    public List<NoteEntity> getAllNotes(int investigationId, int offset, int amount) {
        List<NoteEntity> evidences = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_investigation_notes WHERE investigation_id = ? ORDER BY timestamp DESC LIMIT ?,?")) {
            ps.setInt(1, investigationId);
            ps.setInt(2, offset);
            ps.setInt(3, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    evidences.add(buildNote(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return evidences;
    }

    @Override
    public void removeNote(int id) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_investigation_notes WHERE ID = ?");) {
            insert.setInt(1, id);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private NoteEntity buildNote(ResultSet rs) throws SQLException {
        int id = rs.getInt(ID_COLUMN);
        UUID linkedByUuid = UUID.fromString(rs.getString(NOTED_BY_UUID_COLUMN));
        int investigationId = rs.getInt(INVESTIGATION_ID_COLUMN);
        String note = rs.getString(NOTE_COLUMN);

        return new NoteEntity(
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
