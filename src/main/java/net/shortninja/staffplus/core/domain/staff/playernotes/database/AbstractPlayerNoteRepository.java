package net.shortninja.staffplus.core.domain.staff.playernotes.database;

import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNote;
import net.shortninja.staffplusplus.playernotes.PlayerNoteFilters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.insertFilterValues;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.mapFilters;

public abstract class AbstractPlayerNoteRepository implements PlayerNoteRepository {

    protected final Options options;
    private final SqlConnectionProvider sqlConnectionProvider;

    public AbstractPlayerNoteRepository(Options options, SqlConnectionProvider sqlConnectionProvider) {
        this.options = options;
        this.sqlConnectionProvider = sqlConnectionProvider;
    }

    public Connection getConnection() {
        return sqlConnectionProvider.getConnection();
    }

    @Override
    public List<PlayerNote> getPlayerNotesForTarget(UUID notedByUuid, UUID targetUuid, int offset, int amount) {
        List<PlayerNote> playerNotes = new ArrayList<>();
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_player_notes WHERE (is_private_note = ? OR noted_by_uuid = ?) AND target_uuid = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.notesSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?")) {
            ps.setBoolean(1, false);
            ps.setString(2, notedByUuid.toString());
            ps.setString(3, targetUuid.toString());
            ps.setInt(4, offset);
            ps.setInt(5, amount);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    playerNotes.add(buildNote(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return playerNotes;
    }

    @Override
    public Optional<PlayerNote> findNote(int noteId) {
        try (Connection sql = getConnection();
             PreparedStatement ps = sql.prepareStatement("SELECT * FROM sp_player_notes WHERE ID = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.notesSyncServers))) {
            ps.setInt(1, noteId);
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
    public void deleteNote(int noteId) {
        try (Connection sql = getConnection();
             PreparedStatement insert = sql.prepareStatement("DELETE FROM sp_player_notes WHERE ID = ?");) {
            insert.setInt(1, noteId);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<PlayerNote> findPlayerNotes(UUID notedByUuid, PlayerNoteFilters playerNoteFilters, int offset, int amount) {
        List<PlayerNote> notes = new ArrayList<>();
        String filterQuery = mapFilters(playerNoteFilters, true);

        String query = "SELECT * FROM sp_player_notes WHERE (is_private_note = ? OR noted_by_uuid = ?)" + filterQuery + getServerNameFilterWithAnd(options.serverSyncConfiguration.notesSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?";
        try (Connection sql = getConnection(); PreparedStatement ps = sql.prepareStatement(query)) {
            ps.setBoolean(1, false);
            ps.setString(2, notedByUuid.toString());

            int index = insertFilterValues(playerNoteFilters, ps, 3);
            ps.setInt(index, offset);
            ps.setInt(index + 1, amount);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notes.add(buildNote(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return notes;
    }
    private PlayerNote buildNote(ResultSet rs) throws SQLException {
        return new PlayerNote(rs.getLong("ID"),
            rs.getString("note"),
            rs.getString("noted_by_name"),
            UUID.fromString(rs.getString("noted_by_uuid")),
            rs.getString("target_name"),
            UUID.fromString(rs.getString("target_uuid")),
            rs.getLong("creation_timestamp"),
            rs.getBoolean("is_private_note"),
            rs.getString("server_name"));
    }
}
