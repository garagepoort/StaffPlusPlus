package net.shortninja.staffplus.core.domain.player.settings;

import net.shortninja.staffplus.core.domain.staff.mode.config.GeneralModeConfiguration;
import net.shortninja.staffplusplus.alerts.AlertType;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.Material;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class PlayerSettings {

    private final UUID uuid;
    private String name;
    private Material glassColor;
    private Set<AlertType> alertOptions;
    private VanishType vanishType;
    private List<String> playerNotes;
    private boolean inStaffMode;
    private String modeName;
    private Set<String> mutedStaffChatChannels;

    public PlayerSettings(UUID uuid, String name, Material glassColor, Set<AlertType> alertOptions, VanishType vanishType, List<String> playerNotes, boolean inStaffMode, String modeName, Set<String> mutedStaffChatChannels) {
        this.uuid = uuid;
        this.name = name;
        this.glassColor = glassColor;
        this.alertOptions = alertOptions;
        this.vanishType = vanishType;
        this.playerNotes = playerNotes;
        this.inStaffMode = inStaffMode;
        this.modeName = modeName;
        this.mutedStaffChatChannels = mutedStaffChatChannels;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Material getGlassColor() {
        return glassColor;
    }

    public Set<AlertType> getAlertOptions() {
        return alertOptions;
    }

    public VanishType getVanishType() {
        return vanishType;
    }

    public List<String> getPlayerNotes() {
        return playerNotes;
    }

    public boolean isInStaffMode() {
        return inStaffMode;
    }

    public Optional<String> getModeName() {
        return Optional.ofNullable(modeName);
    }

    public Set<String> getMutedStaffChatChannels() {
        return mutedStaffChatChannels;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVanished() {
        return vanishType == VanishType.TOTAL || vanishType == VanishType.PLAYER;
    }

    public void setVanishType(VanishType vanishType) {
        this.vanishType = vanishType;
    }

    public void addPlayerNote(String note) {
        this.playerNotes.add(note);
    }

    public void setGlassColor(Material glassColor) {
        this.glassColor = glassColor;
    }

    public boolean isStaffChatMuted(String channelName) {
        return mutedStaffChatChannels.contains(channelName);
    }

    public void setModeConfiguration(GeneralModeConfiguration modeConfiguration) {
        this.modeName = modeConfiguration.getName();
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public void setInStaffMode(boolean staffMode) {
        this.inStaffMode = staffMode;
    }

    public void setStaffChatMuted(String name, boolean muted) {
        if (muted) {
            mutedStaffChatChannels.add(name);
        } else {
            mutedStaffChatChannels.remove(name);
        }
    }

    public void setAlertOption(AlertType alertType, boolean isEnabled) {
        if(isEnabled) {
            alertOptions.add(alertType);
        }else{
            alertOptions.remove(alertType);
        }
    }
}
