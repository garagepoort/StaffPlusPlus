package net.shortninja.staffplus.session;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.attribute.gui.IGui;
import net.shortninja.staffplus.server.chat.ChatAction;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.AlertType;
import net.shortninja.staffplus.unordered.VanishType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerSession {

    private final Options options = IocContainer.getOptions();
    private final UUID uuid;
    private final String name;
    private Material glassColor;
    private VanishType vanishType = VanishType.NONE;
    private IGui currentGui = null;
    private ChatAction chatAction = null;
    private Map<AlertType, Boolean> alertOptions = new HashMap<>();
    private List<String> playerNotes = new ArrayList<>();

    private boolean isChatting = false;
    private boolean isFrozen = false;
    private boolean isProtected = false;
    private boolean muted = false;

    public PlayerSession(UUID uuid, String name, boolean muted) {
        this.uuid = uuid;
        this.muted = muted;
        this.glassColor = Material.STAINED_GLASS_PANE;
        this.name = name;

        for (AlertType alertType : AlertType.values()) {
            setAlertOption(alertType, true);
        }
    }

    public PlayerSession(UUID uuid, String name, Material glassColor, List<String> playerNotes, Map<AlertType, Boolean> alertOptions, boolean muted) {
        this.uuid = uuid;
        this.name = name;
        this.glassColor = glassColor;
        this.playerNotes = playerNotes;
        this.alertOptions = alertOptions;
        this.muted = muted;
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayerExact(name));
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

    public void setGlassColor(Material glassColor) {
        this.glassColor = glassColor;
    }

    public List<String> getPlayerNotes() {
        return playerNotes;
    }

    public VanishType getVanishType() {
        return vanishType;
    }

    /**
     * This method should NOT be used if you want to update the user's vanish
     * type! Use the vanish handler!
     */
    public void setVanishType(VanishType vanishType) {
        this.vanishType = vanishType;
    }

    public Optional<IGui> getCurrentGui() {
        return Optional.ofNullable(currentGui);
    }

    public void setCurrentGui(IGui currentGui) {
        this.currentGui = currentGui;
    }

    public ChatAction getChatAction() {
        return chatAction;
    }

    public void setChatAction(ChatAction chatAction) {
        this.chatAction = chatAction;
    }

    public boolean shouldNotify(AlertType alertType) {
        return alertOptions.get(alertType) != null && alertOptions.get(alertType);
    }

    public boolean inStaffChatMode() {
        return isChatting;
    }

    public void setChatting(boolean isChatting) {
        this.isChatting = isChatting;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    public void setAlertOption(AlertType alertType, boolean isEnabled) {
        if (alertOptions.containsKey(alertType)) {
            alertOptions.replace(alertType, isEnabled);
        } else {
            alertOptions.put(alertType, isEnabled);
        }
    }

    public void addPlayerNote(String note) {
        playerNotes.add(note);
    }

    public boolean isVanished() {
        return this.getVanishType() == VanishType.TOTAL;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}