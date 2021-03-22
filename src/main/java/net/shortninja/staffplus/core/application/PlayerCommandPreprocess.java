package net.shortninja.staffplus.core.application;

import be.garagepoort.mcioc.IocContainer;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.cmd.BaseCmd;
import net.shortninja.staffplus.core.common.cmd.CmdHandler;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.freeze.FreezeHandler;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import net.shortninja.staffplus.core.session.SessionManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.COMMANDS;


public class PlayerCommandPreprocess implements Listener {
    private final PermissionHandler permission = IocContainer.get(PermissionHandler.class);
    private final MessageCoordinator message = IocContainer.get(MessageCoordinator.class);
    private final Options options = IocContainer.get(Options.class);
    private final Messages messages = IocContainer.get(Messages.class);
    private final FreezeHandler freezeHandler = IocContainer.get(FreezeHandler.class);
    private final CmdHandler cmdHandler = StaffPlus.get().cmdHandler;
    private final TraceService traceService = IocContainer.get(TraceService.class);
    private final SessionManagerImpl sessionManager = IocContainer.get(SessionManagerImpl.class);

    public PlayerCommandPreprocess() {
        Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String command = event.getMessage().toLowerCase();
        traceService.sendTraceMessage(COMMANDS, uuid, "Player invoked command: [" + command + "]");

        if (command.startsWith("/help staffplus") || command.startsWith("/help staff+")) {
            sendHelp(player);
            event.setCancelled(true);
            return;
        }

        if (options.blockedCommands.contains(command) && permission.hasOnly(player, options.permissionBlock)) {
            message.send(player, messages.commandBlocked, messages.prefixGeneral);
            event.setCancelled(true);
        } else if (sessionManager.get(uuid).isInStaffMode() && options.blockedModeCommands.contains(command)) {
            message.send(player, messages.modeCommandBlocked, messages.prefixGeneral);
            event.setCancelled(true);
        } else if (freezeHandler.isFrozen(uuid) && (!options.modeConfiguration.getFreezeModeConfiguration().isModeFreezeChat() && !command.startsWith("/" + options.commandLogin))) {
            message.send(player, messages.chatPrevented, messages.prefixGeneral);
            event.setCancelled(true);
        }
    }


    private void sendHelp(Player player) {
        int count = 0;

        message.send(player, "&7" + message.LONG_LINE, "");

        List<BaseCmd> sortedCommands = Arrays.stream(cmdHandler.commands)
            .sorted(Comparator.comparing(o -> o.getCommand().getName()))
            .collect(Collectors.toList());

        for (BaseCmd baseCmd : sortedCommands) {
            if (baseCmd.getPermissions().isEmpty()) {
                message.send(player, "&b/" + baseCmd.getCommand().getName() + " &7: " + baseCmd.getDescription().toLowerCase(), "");
                count++;
            } else {
                for (String permission : baseCmd.getPermissions()) {
                    if (this.permission.has(player, permission)) {
                        message.send(player, "&b/" + baseCmd.getCommand().getName() + " &7: " + baseCmd.getDescription().toLowerCase(), "");
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