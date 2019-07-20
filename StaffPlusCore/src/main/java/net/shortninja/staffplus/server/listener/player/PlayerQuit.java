package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.TicketHandler;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.factory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private MessageCoordinator message = StaffPlus.get().message;
    private Options options = StaffPlus.get().options;
    private Messages messages = StaffPlus.get().messages;
    private UserManager userManager = StaffPlus.get().getUserManager();
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
    private VanishHandler vanishHandler = StaffPlus.get().vanishHandler;
    private TicketHandler ticketHandler = StaffPlus.get().ticketHandler;

    public PlayerQuit() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        manageUser(player);
        modeCoordinator.removeMode(player);
        vanishHandler.removeVanish(player);
        ticketHandler.removeTicket(ticketHandler.getTicketByUuid(player.getUniqueId()), "", TicketHandler.TicketCloseReason.QUIT);
        if (userManager.get(player.getUniqueId()).isFrozen()) {
            Bukkit.getLogger().info("true");
            for (String command : options.logoutCommands) {
                Bukkit.getLogger().info("true 1");
                command = command.replace("%player%", player.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }
        if(!InventoryFactory.isInventoryEmpty(event.getPlayer().getEnderChest())){
            InventoryFactory.saveEnderChest(event.getPlayer());
        }
    }

    private void manageUser(Player player) {
        User user = userManager.get(player.getUniqueId());

        user.setOnline(false);

        if (user.isFrozen()) {
            message.sendGroupMessage(messages.freezeLogout.replace("%player%", player.getName()), options.permissionFreeze, messages.prefixGeneral);
        }
    }
}