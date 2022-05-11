package net.shortninja.staffplus.core.domain.staff.location;

import net.shortninja.staffplusplus.stafflocations.IStaffLocationNote;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class StaffLocationNote implements IStaffLocationNote {

    private int id;
    private final int locationId;
    private final String note;
    private final UUID notedByUuid;
    private final String notedByName;
    private final long timestamp;

    public StaffLocationNote(int locationId, String note, Player notedBy) {
        this.locationId = locationId;
        this.note = note;
        this.notedByUuid = notedBy.getUniqueId();
        this.notedByName = notedBy.getName();
        this.timestamp = System.currentTimeMillis();
    }

    public StaffLocationNote(int id, int locationId, String note, UUID notedByUuid, String notedByName, long timestamp) {
        this.id = id;
        this.locationId = locationId;
        this.note = note;
        this.notedByUuid = notedByUuid;
        this.notedByName = notedByName;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getLocationId() {
        return locationId;
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
