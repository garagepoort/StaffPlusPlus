package net.shortninja.staffplus.session.bungee;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.shortninja.staffplus.unordered.VanishType;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class SessionBungeeDto {

    private UUID playerUuid;
    private VanishType vanishType;
    private Boolean staffMode;

    public SessionBungeeDto() {}

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

    public void setStaffMode(Boolean staffMode) {
        this.staffMode = staffMode;
    }

    public Boolean getStaffMode() {
        return staffMode;
    }
}
