package net.shortninja.staffplus.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.player.attribute.gui.IGui;
import net.shortninja.staffplus.server.chat.ChatAction;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class PlayerSession {

    private final Options options = IocContainer.getOptions();
    private final UUID uuid;
    private final String name;
    protected short glassColor;
    private VanishType vanishType = VanishType.NONE;
    private IGui currentGui = null;
    private ChatAction chatAction = null;
    private Map<AlertType, Boolean> alertOptions = new HashMap<>();
    private List<String> playerNotes = new ArrayList<>();

    private boolean isChatting = false;
    private boolean isFrozen = false;


    private static Class<?> craftPlayerClass;
    private static Class<?> entityPlayerClass;
    private static Class<?> playerConnectionClass;
    private static Method getHandleMethod;
    private static Field playerConnectionField;
    private static Field pingField;

    /*static { Causes issues will fix latter
        try {
            final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
//            final String version = StaffPlus.get().versionProtocol.getVersion();

            craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            entityPlayerClass = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
            playerConnectionClass = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
            getHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
            playerConnectionField = entityPlayerClass.getDeclaredField("playerConnection");
            pingField = playerConnectionClass.getDeclaredField("ping");
        } catch (ReflectiveOperationException  e) {
            //throw new RuntimeException(e);
        }
    }*/

    public PlayerSession(UUID uuid, String name, short glassColor, List<String> playerNotes, Map<AlertType, Boolean> alertOptions) {
        this.uuid = uuid;
        this.name = name;
        this.glassColor = glassColor;
        this.playerNotes = playerNotes;
        this.alertOptions = alertOptions;
    }

    public PlayerSession(UUID uuid, String name) {
        this.uuid = uuid;
        this.glassColor = options.glassColor;
        this.name = name;

        for (AlertType alertType : AlertType.values()) {
            setAlertOption(alertType, true);
        }
    }

    public PlayerSession(UUID uuid, String name, short glassColor, List<String> playerNotes, Map<AlertType, Boolean> alertOptions, Player player) {
        this.uuid = uuid;
        this.name = name;
        this.glassColor = glassColor;
        this.playerNotes = playerNotes;
        this.alertOptions = alertOptions;
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public short getGlassColor() {
        return IocContainer.getStorage().getGlassColor(this);
    }

    public void setGlassColor(short glassColor) {
        IocContainer.getStorage().setGlassColor(this, glassColor);
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


    public static int getPing(Player player) {
        try {
            Object entityPlayer = getHandleMethod.invoke(player);
            Object playerConnection = playerConnectionField.get(entityPlayer);

            return (int) pingField.get(playerConnection);
        } catch (ReflectiveOperationException e) {
            Bukkit.getLogger().warning(e.getMessage());
            return -1;
        }
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
}