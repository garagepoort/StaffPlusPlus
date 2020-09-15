package net.shortninja.staffplus.server.listener.player;

import net.shortninja.staffplus.IocContainer;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.player.attribute.mode.handler.freeze.FreezeHandler;
import net.shortninja.staffplus.server.command.BaseCmd;
import net.shortninja.staffplus.server.command.CmdHandler;
import net.shortninja.staffplus.server.data.config.Messages;
import net.shortninja.staffplus.server.data.config.Options;
import net.shortninja.staffplus.util.MessageCoordinator;
import net.shortninja.staffplus.util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;


public class PlayerCommandPreprocess implements Listener {
    private PermissionHandler permission = IocContainer.getPermissionHandler();
    private MessageCoordinator message = IocContainer.getMessage();
    private Options options = IocContainer.getOptions();
    private Messages messages = IocContainer.getMessages();
    private FreezeHandler freezeHandler = StaffPlus.get().freezeHandler;
    private CmdHandler cmdHandler = StaffPlus.get().cmdHandler;
    private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;

    public PlayerCommandPreprocess() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String command = event.getMessage().toLowerCase();

        if (command.startsWith("/help staffplus") || command.startsWith("/help staff+")) {
            sendHelp(player);
            event.setCancelled(true);
            return;
        }


        if(PlayerJoin.needLogin.contains(event.getPlayer().getUniqueId()) && options.preLoginBlock.contains(command))
            event.setCancelled(true);
        if (options.blockedCommands.contains(command) && permission.hasOnly(player, options.permissionBlock)) {
            message.send(player, messages.commandBlocked, messages.prefixGeneral);
            event.setCancelled(true);
        } else if (modeCoordinator.isInMode(uuid) && options.blockedModeCommands.contains(command)) {
            message.send(player, messages.modeCommandBlocked, messages.prefixGeneral);
            event.setCancelled(true);
        } else if (freezeHandler.isFrozen(uuid) && (!options.modeFreezeChat || (freezeHandler.isLoggedOut(uuid)) && !command.startsWith("/" + options.commandLogin))) {
            message.send(player, messages.chatPrevented, messages.prefixGeneral);
            event.setCancelled(true);
        }
    }


    private void sendHelp(Player player) {
        int count = 0;

        message.send(player, "&7" + message.LONG_LINE, "");

        for (BaseCmd baseCmd : cmdHandler.BASES) {
            if (baseCmd.getPermissions().isEmpty()) {
                message.send(player, "&b/" + baseCmd.getMatch() + " &8: " + baseCmd.getDescription().toLowerCase(), "");
                count++;
            } else {
                for (String permission : baseCmd.getPermissions()) {
                    if (this.permission.has(player, permission)) {
                        message.send(player, "&b/" + baseCmd.getMatch() + " &8: " + baseCmd.getDescription().toLowerCase(), "");
                        count++;
                        break;
                    }
                }
            }
        }

        if (count == 0) {
            message.send(player, messages.noPermission, messages.prefixGeneral);
        }

        message.send(player, "&7" + message.LONG_LINE, "");
    }
}