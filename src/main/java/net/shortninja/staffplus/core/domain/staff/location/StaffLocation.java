package net.shortninja.staffplus.core.domain.staff.location;

import net.shortninja.staffplusplus.stafflocations.IStaffLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class StaffLocation implements IStaffLocation {

    private int id;
    private final String name;
    private final String creatorName;
    private final UUID creatorUuid;
    private final Location location;
    private String serverName;
    private final long creationTimestamp;
    private StaffLocationNote newestNote;

    public StaffLocation(int id, String name, String creatorName, UUID creatorUuid, Location location, String serverName, long creationTimestamp, StaffLocationNote newestNote) {
        this.id = id;
        this.name = name;
        this.creatorName = creatorName;
        this.creatorUuid = creatorUuid;
        this.location = location;
        this.serverName = serverName;
        this.creationTimestamp = creationTimestamp;
        this.newestNote = newestNote;
    }

    public StaffLocation(String name, Player player, Location location) {
        this.name = name;
        this.creatorName = player.getName();
        this.creatorUuid = player.getUniqueId();
        this.location = location;
        this.creationTimestamp = System.currentTimeMillis();
    }

    @Override
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getCreatorName() {
        return creatorName;
    }

    public Optional<StaffLocationNote> getNewestNote() {
        return Optional.ofNullable(newestNote);
    }

    @Override
    public UUID getCreatorUuid() {
        return creatorUuid;
    }

    @Override
    public String getServerName() {
        return serverName;
    }
}
