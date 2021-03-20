package net.shortninja.staffplus.domain.staff.protect;

import org.bukkit.Location;

import java.util.UUID;

public class ProtectedArea {

    private int id;
    private String name;
    private Location cornerPoint1;
    private Location cornerPoint2;
    private UUID protectedByUuid;

    public ProtectedArea(int id, String name, Location cornerPoint1, Location cornerPoint2, UUID protectedByUuid) {
        this.id = id;
        this.name = name;
        this.cornerPoint1 = cornerPoint1;
        this.cornerPoint2 = cornerPoint2;
        this.protectedByUuid = protectedByUuid;
    }

    public ProtectedArea(String name, Location location1, Location location2, UUID protectedByUuid) {
        this.name = name;
        this.cornerPoint1 = location1;
        this.cornerPoint2 = location2;
        this.protectedByUuid = protectedByUuid;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCornerPoint1() {
        return cornerPoint1;
    }

    public Location getCornerPoint2() {
        return cornerPoint2;
    }

    public UUID getProtectedByUuid() {
        return protectedByUuid;
    }

    public boolean isInArea(Location location) {
        int minX = Math.min(cornerPoint1.getBlockX(), cornerPoint2.getBlockX());
        int minZ = Math.min(cornerPoint1.getBlockZ(), cornerPoint2.getBlockZ());
        int maxX = Math.max(cornerPoint1.getBlockX(), cornerPoint2.getBlockX());
        int maxZ = Math.max(cornerPoint1.getBlockZ(), cornerPoint2.getBlockZ());

        return location.getBlockX() >= minX && location.getBlockX() <= maxX && location.getBlockZ() >= minZ && location.getBlockZ() <= maxZ;
    }
}
