package net.shortninja.staffplus.player.attribute.mode.handler;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeDataVault;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.lib.JavaUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReviveHandler {
    private static Map<UUID, ModeDataVault> savedInventories = new HashMap<UUID, ModeDataVault>();
    private MessageCoordinator message = StaffPlus.get().message;
    private Messages messages = StaffPlus.get().messages;

    public boolean hasSavedInventory(UUID uuid) {
        return savedInventories.containsKey(uuid);
    }

    public void cacheInventory(Player player) {
        UUID uuid = player.getUniqueId();
        ModeDataVault modeDataVault = new ModeDataVault(uuid, player.getInventory().getContents(), player.getInventory().getArmorContents());

        savedInventories.put(uuid, modeDataVault);
    }

    public void restoreInventory(Player player) {
        UUID uuid = player.getUniqueId();
        ModeDataVault modeDataVault = savedInventories.get(uuid);

        JavaUtils.clearInventory(player);
        player.getInventory().setContents(modeDataVault.getItems());
        player.getInventory().setArmorContents(modeDataVault.getArmor());
        message.send(player, messages.revivedUser, messages.prefixGeneral);
        savedInventories.remove(uuid);
    }
}