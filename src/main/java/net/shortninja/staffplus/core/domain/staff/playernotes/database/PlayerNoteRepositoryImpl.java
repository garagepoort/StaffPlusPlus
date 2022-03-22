package net.shortninja.staffplus.core.domain.staff.playernotes.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.staff.playernotes.PlayerNote;
import net.shortninja.staffplusplus.playernotes.PlayerNoteFilters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static net.shortninja.staffplus.core.common.Constants.getServerNameFilterWithAnd;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.insertFilterValues;
import static net.shortninja.staffplus.core.common.utils.DatabaseUtil.mapFilters;

@IocBean
public class PlayerNoteRepositoryImpl implements PlayerNoteRepository {

    private final Options options;
    private final QueryBuilderFactory query;

    public PlayerNoteRepositoryImpl(Options options, QueryBuilderFactory query) {
        this.options = options;
        this.query = query;
    }

    @Override
    public long createPlayerNote(PlayerNote playerNote) {
        return query.create().insertQuery("INSERT INTO sp_player_notes (note, target_name, target_uuid, noted_by_name, noted_by_uuid, is_private_note, creation_timestamp, server_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
            (insert) -> {
                insert.setString(1, playerNote.getNote());
                insert.setString(2, playerNote.getTargetName());
                insert.setString(3, playerNote.getTargetUuid().toString());
                insert.setString(4, playerNote.getNotedByName());
                insert.setString(5, playerNote.getNotedByUuid().toString());
                insert.setBoolean(6, playerNote.isPrivateNote());
                insert.setLong(7, playerNote.getCreationTimestamp());
                insert.setString(8, options.serverName);
            });
    }

    @Override
    public List<PlayerNote> getPlayerNotesForTarget(UUID notedByUuid, UUID targetUuid, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_player_notes WHERE (is_private_note = ? OR noted_by_uuid = ?) AND target_uuid = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.notesSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?",
            (ps) -> {
                ps.setBoolean(1, false);
                ps.setString(2, notedByUuid.toString());
                ps.setString(3, targetUuid.toString());
                ps.setInt(4, offset);
                ps.setInt(5, amount);
            }, this::buildNote);
    }

    @Override
    public Optional<PlayerNote> findNote(int noteId) {
        return query.create().findOne("SELECT * FROM sp_player_notes WHERE ID = ? " + getServerNameFilterWithAnd(options.serverSyncConfiguration.notesSyncServers),
            (ps) -> ps.setInt(1, noteId),
            this::buildNote);
    }

    @Override
    public void deleteNote(int noteId) {
        query.create().deleteQuery("DELETE FROM sp_player_notes WHERE ID = ?", (insert) -> insert.setInt(1, noteId));
    }

    @Override
    public List<PlayerNote> findPlayerNotes(UUID notedByUuid, PlayerNoteFilters playerNoteFilters, int offset, int amount) {
        String filterQuery = mapFilters(playerNoteFilters, true);
        String query = "SELECT * FROM sp_player_notes WHERE (is_private_note = ? OR noted_by_uuid = ?)" + filterQuery + getServerNameFilterWithAnd(options.serverSyncConfiguration.notesSyncServers) + " ORDER BY creation_timestamp DESC LIMIT ?,?";

        return this.query.create().find(query, (ps) -> {
            ps.setBoolean(1, false);
            ps.setString(2, notedByUuid.toString());

            int index = insertFilterValues(playerNoteFilters, ps, 3);
            ps.setInt(index, offset);
            ps.setInt(index + 1, amount);
        }, this::buildNote);
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
