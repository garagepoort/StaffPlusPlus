package net.shortninja.staffplus.unordered;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPlayerSession {

    UUID getUuid();

    String getName();

    void setVanishType(VanishType vanishType);

    VanishType getVanishType();

    default boolean isVanished() {
        return this.getVanishType() == VanishType.TOTAL;
    }

    void setGlassColor(short color);

    short getGlassColor();

    List<String> getPlayerNotes();

    boolean shouldNotify(AlertType alertType);

    void setFrozen(boolean frozen);

    boolean isFrozen();

    Optional<Player> getPlayer();

    void setCurrentGui(IGui gui);

    Optional<IGui> getCurrentGui();

    void setQueuedAction(IAction action);

    void addPlayerNote(String s);

    IAction getQueuedAction();

    boolean inStaffChatMode();

    void setChatting(boolean b);

    void setAlertOption(AlertType alertType, boolean isEnabled);

}
