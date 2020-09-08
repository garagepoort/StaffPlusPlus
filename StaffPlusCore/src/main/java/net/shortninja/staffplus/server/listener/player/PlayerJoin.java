package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.InventorySerializer;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.FreezeHandler;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler;
import net.shortninja.staffplus.server.data.Load;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
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

import net.shortninja.staffplus.player.attribute.SecurityHandler;

public class PlayerJoin implements Listener {
    private PermissionHandler permission = StaffPlus.get().permission;
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().getUserManager();
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    private SecurityHandler securityHandler = StaffPlus.get().securityHandler;  //FIXME

    private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;
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

        /*if (options.loginEnabled && permission.has(player, options.permissionMember)) {
            if (securityHandler.hasPassword(player.getUniqueId())) {
                needLogin.add(event.getPlayer().getUniqueId());
                freezeHandler.addFreeze(player, player, false);
                message.send(player, messages.loginWaiting, messages.prefixGeneral);
            } else message.send(player, messages.loginRegister, messages.prefixGeneral);
        }
 FIXME*/

        loadInv(player);
    }

    private void manageUser(Player player) {
        UUID uuid = player.getUniqueId();

        if (userManager.has(uuid)) {
            userManager.get(uuid).setOnline(true);
        } else new Load().load(player);
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