package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.InventorySerializer;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler;
import net.shortninja.staffplus.server.data.Load;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.unordered.IUser;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerJoin implements Listener {
    private PermissionHandler permission = IocContainer.getPermissionHandler();
    private Options options = IocContainer.getOptions();
    private UserManager userManager = StaffPlus.get().getUserManager();
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    private VanishHandler vanishHandler = StaffPlus.get().vanishHandler;
    public static ArrayList<UUID> needLogin = new ArrayList<>();

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

        IUser iUser = userManager.has(uuid) ? userManager.get(uuid) : new Load().load(player);
        iUser.setOnline(true);
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