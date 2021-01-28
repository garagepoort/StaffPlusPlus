package net.shortninja.staffplus.session.database;

import net.shortninja.staffplus.unordered.VanishType;

import java.util.UUID;

public class SessionEntity {

    private int id;
    private UUID playerUuid;
    private VanishType vanishType;
    private boolean staffMode;

    public SessionEntity() {}

    public SessionEntity(int id, UUID playerUuid, VanishType vanishType, boolean staffMode) {
        this.id = id;
        this.playerUuid = playerUuid;
        this.vanishType = vanishType;
        this.staffMode = staffMode;
    }

    public SessionEntity(UUID playerUuid, VanishType vanishType, boolean staffMode) {
        this.playerUuid = playerUuid;
        this.vanishType = vanishType;
        this.staffMode = staffMode;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public VanishType getVanishType() {
        return vanishType;
    }

    public void setVanishType(VanishType vanishType) {
        this.vanishType = vanishType;
    }

    public void setStaffMode(boolean staffMode) {
        this.staffMode = staffMode;
    }

    public boolean getStaffMode() {
        return staffMode;
    }

    public int getId() {
        return id;
    }
}
