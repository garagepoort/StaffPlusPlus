package net.shortninja.staffplus.core.domain.staff.investigate;

import net.shortninja.staffplusplus.investigate.IInvestigationNote;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class NoteEntity implements IInvestigationNote {

    private int id;
    private int investigationId;
    private String note;
    private UUID notedByUuid;
    private String notedByName;
    private long timestamp;

    public NoteEntity(int investigationId, String note, UUID notedByUuid, String notedByName) {
        this.investigationId = investigationId;
        this.note = note;
        this.notedByUuid = notedByUuid;
        this.notedByName = notedByName;
        this.timestamp = System.currentTimeMillis();
    }

    public NoteEntity(int id, int investigationId, String note, UUID notedByUuid, String notedByName, long timestamp) {
        this.id = id;
        this.investigationId = investigationId;
        this.note = note;
        this.notedByUuid = notedByUuid;
        this.notedByName = notedByName;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getInvestigationId() {
        return investigationId;
    }

    @Override
    public String getNote() {
        return note;
    }

    public UUID getNotedByUuid() {
        return notedByUuid;
    }

    public Long getCreationTimestamp() {
        return timestamp;
    }

    @Override
    public ZonedDateTime getCreationDate() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public String getNotedByName() {
        return notedByName;
    }
}
