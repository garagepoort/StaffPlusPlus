package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.PlayerSession;
import net.shortninja.staffplus.player.attribute.InventorySerializer;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.AlertCoordinator;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.session.SessionManager;
import net.shortninja.staffplus.staff.vanish.VanishHandler;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerJoin implements Listener {
    private final PermissionHandler permission = IocContainer.getPermissionHandler();
    private final Options options = IocContainer.getOptions();
    private final SessionManager sessionManager = IocContainer.getSessionManager();
    private final ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
    private final VanishHandler vanishHandler = IocContainer.getVanishHandler();
    private final AlertCoordinator alertCoordinator = IocContainer.getAlertCoordinator();

    public PlayerJoin() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        StaffPlus.get().versionProtocol.inject(event.getPlayer());

        Player player = event.getPlayer();

        manageUser(player);
        vanishHandler.updateVanish();

        if (permission.has(player, options.permissionMode) && options.modeEnableOnLogin) {
            modeCoordinator.addMode(player);
        }

        loadInv(player);
        delayedActions(player);
    }

    private void manageUser(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerSession playerSession = sessionManager.get(uuid);

        if (!playerSession.getName().equals(player.getName())) {
            alertCoordinator.onNameChange(playerSession.getName(), player.getName());
        }
    }

    private void delayedActions(Player player) {
        List<String> delayedActions = IocContainer.getDelayedActionsRepository().getDelayedActions(player.getUniqueId());
        delayedActions.forEach(delayedAction -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), delayedAction.replace("%player%", player.getName()));
        });
        IocContainer.getDelayedActionsRepository().clearDelayedActions(player.getUniqueId());
    }

    private void loadInv(Player p) {
        InventorySerializer serializer = new InventorySerializer(p.getUniqueId());
        if (serializer.shouldLoad()) {HashMap<String, ItemStack> items = serializer.getContents();
            for (String num : items.keySet())
                p.getInventory().setItem(Integer.parseInt(num), items.get(num));

            serializer.deleteFile();
        }
    }
}