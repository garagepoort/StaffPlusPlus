package net.shortninja.staffplus.core.stafflocations;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.helpers.QueryBuilderFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@IocBean
public class StaffLocationNotesRepository {

    private static final String ID_COLUMN = "ID";
    private static final String LOCATION_ID_COLUMN = "staff_location_id";
    private static final String NOTE_COLUMN = "note";
    private static final String NOTED_BY_UUID_COLUMN = "noted_by_uuid";
    private static final String NOTED_BY_NAME_COLUMN = "noted_by_name";
    private static final String CREATION_TIMESTAMP_COLUMN = "timestamp";

    private final QueryBuilderFactory query;

    public StaffLocationNotesRepository(QueryBuilderFactory query) {
        this.query = query;
    }

    public Optional<StaffLocationNote> find(int id) {
        return query.create().findOne("SELECT * FROM sp_staff_location_notes WHERE ID = ?",
            ps -> ps.setInt(1, id),
            this::buildNote);
    }

    public void addNote(StaffLocationNote staffLocationNote) {
        query.create().insertQuery("INSERT INTO sp_staff_location_notes(staff_location_id, noted_by_uuid, noted_by_name, note, timestamp) " +
                "VALUES(?, ?, ?, ?, ?);",
            insert -> {
                insert.setInt(1, staffLocationNote.getLocationId());
                insert.setString(2, staffLocationNote.getNotedByUuid().toString());
                insert.setString(3, staffLocationNote.getNotedByName());
                insert.setString(4, staffLocationNote.getNote());
                insert.setLong(5, staffLocationNote.getCreationTimestamp());
            });
    }

    public List<StaffLocationNote> getAllNotes(int locationId) {
        return query.create().find("SELECT * FROM sp_staff_location_notes WHERE staff_location_id = ? ORDER BY timestamp DESC",
            ps -> ps.setInt(1, locationId),
            this::buildNote);
    }

    public List<StaffLocationNote> getAllNotes(int locationId, int offset, int amount) {
        return query.create().find("SELECT * FROM sp_staff_location_notes WHERE staff_location_id = ? ORDER BY timestamp DESC LIMIT ?,?",
            ps -> {
                ps.setInt(1, locationId);
                ps.setInt(2, offset);
                ps.setInt(3, amount);
            }, this::buildNote);
    }

    public void removeNote(int id) {
        query.create().deleteQuery("DELETE FROM sp_staff_location_notes WHERE ID = ?", insert -> insert.setInt(1, id));
    }

    private StaffLocationNote buildNote(ResultSet rs) throws SQLException {
        int id = rs.getInt(ID_COLUMN);
        UUID linkedByUuid = UUID.fromString(rs.getString(NOTED_BY_UUID_COLUMN));
        String notedByName = rs.getString(NOTED_BY_NAME_COLUMN);
        int locationId = rs.getInt(LOCATION_ID_COLUMN);
        String note = rs.getString(NOTE_COLUMN);

        return new StaffLocationNote(
            id,
            locationId,
            note,
            linkedByUuid,
            notedByName,
            rs.getLong(CREATION_TIMESTAMP_COLUMN));
    }
}
