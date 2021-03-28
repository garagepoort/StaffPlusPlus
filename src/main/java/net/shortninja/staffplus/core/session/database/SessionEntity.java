package net.shortninja.staffplus.core.session.database;

import net.shortninja.staffplusplus.vanish.VanishType;

import java.util.UUID;

public class SessionEntity {

    private int id;
    private UUID playerUuid;
    private VanishType vanishType;
    private boolean staffMode;
    private boolean staffChatMuted;

    public SessionEntity() {}

    public SessionEntity(int id, UUID playerUuid, VanishType vanishType, boolean staffMode, boolean staffChatMuted) {
        this.id = id;
        this.playerUuid = playerUuid;
        this.vanishType = vanishType;
        this.staffMode = staffMode;
        this.staffChatMuted = staffChatMuted;
    }

    public SessionEntity(UUID playerUuid, VanishType vanishType, boolean staffMode, boolean staffChatMuted) {
        this.playerUuid = playerUuid;
        this.vanishType = vanishType;
        this.staffMode = staffMode;
        this.staffChatMuted = staffChatMuted;
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

    public void setStaffChatMuted(boolean staffChatMuted) {
        this.staffChatMuted = staffChatMuted;
    }

    public boolean isStaffChatMuted() {
        return staffChatMuted;
    }
}
