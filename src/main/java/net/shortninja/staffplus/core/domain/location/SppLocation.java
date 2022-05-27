package net.shortninja.staffplus.core.domain.location;

import net.shortninja.staffplusplus.ILocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SppLocation implements ILocation {
    private int id;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private String serverName;

    public SppLocation(int id, String worldName, double x, double y, double z, String serverName) {
        this.id = id;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.serverName = serverName;
    }

    public SppLocation(Location location, String serverName) {
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.serverName = serverName;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getWorldName() {
        return worldName;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Location toLocation() {
        World world = Bukkit.getWorld(this.worldName);
        return new Location(world, x, y, z);
    }
}
