package net.shortninja.staffplus.core.stafflocations;

import net.shortninja.staffplus.core.domain.location.SppLocation;
import net.shortninja.staffplusplus.stafflocations.IStaffLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class StaffLocation implements IStaffLocation {

    private int id;
    private String name;
    private final String creatorName;
    private final UUID creatorUuid;
    private SppLocation sppLocation;
    private String serverName;
    private final long creationTimestamp;
    private StaffLocationNote newestNote;
    private Material icon;

    public StaffLocation(int id,
                         String name,
                         String creatorName,
                         UUID creatorUuid,
                         SppLocation sppLocation,
                         String serverName,
                         long creationTimestamp,
                         StaffLocationNote newestNote,
                         Material icon) {
        this.id = id;
        this.name = name;
        this.creatorName = creatorName;
        this.creatorUuid = creatorUuid;
        this.sppLocation = sppLocation;
        this.serverName = serverName;
        this.creationTimestamp = creationTimestamp;
        this.newestNote = newestNote;
        this.icon = icon;
    }

    public StaffLocation(String name, Player player, SppLocation sppLocation, Material icon) {
        this.name = name;
        this.creatorName = player.getName();
        this.creatorUuid = player.getUniqueId();
        this.sppLocation = sppLocation;
        this.icon = icon;
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

    public SppLocation getSppLocation() {
        return sppLocation;
    }

    public Location getLocation() {
        return sppLocation.toLocation();
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

    public Material getIcon() {
        return icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public void setSppLocation(SppLocation sppLocation) {
        this.sppLocation = sppLocation;
    }
}
