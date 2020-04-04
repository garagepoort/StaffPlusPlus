package net.shortninja.staffplus.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.*;
import net.shortninja.staffplus.util.MessageCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class User implements IUser {
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UUID uuid;
    private String name;
    protected short glassColor;
    private List<IReport> reports = new ArrayList<>();
    private List<IWarning> warnings = new ArrayList<>();
    private VanishType vanishType = VanishType.NONE;
    private List<String> playerNotes = new ArrayList<String>();
    private IGui currentGui = null;
    private IAction queuedAction = null;
    private Map<AlertType, Boolean> alertOptions = new HashMap<AlertType, Boolean>();
    private boolean isOnline = true;
    private boolean isChatting = false;
    private boolean isFrozen = false;

    public User(UUID uuid, String name, short glassColor, List<IReport> reports, List<IWarning> warnings, List<String> playerNotes, Map<AlertType, Boolean> alertOptions) {
        this.uuid = uuid;
        this.name = name;
        this.glassColor = glassColor;
        this.reports = reports;
        this.warnings = warnings;
        this.playerNotes = playerNotes;
        this.alertOptions = alertOptions;
    }

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.glassColor = options.glassColor;
        this.name = name;

        for (AlertType alertType : AlertType.values()) {
            setAlertOption(alertType, true);
        }
    }

    /**
     * This method can return a null player if the user is not online, so be sure
     * to check!
     *
     * @return
     */
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }


    private short getColorColor() {
        return glassColor;
    }

    public short getGlassColor() {
        return StaffPlus.get().storage.getGlassColor(this);
    }

    public void setGlassColor(short glassColor) {
        StaffPlus.get().storage.setGlassColor(this, glassColor);
    }

    public List<IReport> getReports() {
        return StaffPlus.get().storage.getReports(getUuid());
    }

    public List<IWarning> getWarnings() {
        return StaffPlus.get().storage.getWarnings(getUuid());
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

    public IAction getQueuedAction() {
        return queuedAction;
    }

    public void setQueuedAction(IAction queuedAction) {
        this.queuedAction = queuedAction;
    }

    public boolean shouldNotify(AlertType alertType) {
        return alertOptions.get(alertType) == null ? false : alertOptions.get(alertType);
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isChatting() {
        return isChatting;
    }

    public void setChatting(boolean isChatting) {
        this.isChatting = isChatting;
    }

    public boolean isFrozen() {
        return isFrozen;
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

    public void addReport(IReport report) {
        StaffPlus.get().storage.addReport(report);
    }

    public void removeReport(String uuid) {
        reports.remove(uuid);
    }

    public void addWarning(IWarning warning) {
        StaffPlus.get().storage.addWarning(warning);
    }

    public void removeWarning(UUID uuid) {
        StaffPlus.get().storage.removeWarning(uuid);
    }

    public void addPlayerNote(String note) {
        playerNotes.add(note);
    }
}