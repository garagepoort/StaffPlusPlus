package net.shortninja.staffplus.core.application;

import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.application.config.SplitByComma;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.cmd.BaseCmd;
import net.shortninja.staffplus.core.common.cmd.CmdHandler;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.tracing.TraceService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.domain.staff.tracing.TraceType.COMMANDS;

@IocBukkitListener
public class PlayerCommandPreprocess implements Listener {

    @ConfigProperty("blocked-commands")
    @ConfigTransformer(SplitByComma.class)
    private List<String> blockedCommands = new ArrayList<>();

    @ConfigProperty("blocked-mode-commands")
    @ConfigTransformer(SplitByComma.class)
    private List<String> blockedModeCommands = new ArrayList<>();

    private final PermissionHandler permission;

    private final Options options;
    private final Messages messages;
    private final CmdHandler cmdHandler;
    private final TraceService traceService;
    private final OnlineSessionsManager sessionManager;

    @ConfigProperty("permissions:block")
    private String permissionBlock;

    public PlayerCommandPreprocess(PermissionHandler permission,
                                   Options options,
                                   Messages messages,
                                   CmdHandler cmdHandler,
                                   TraceService traceService, OnlineSessionsManager sessionManager) {
        this.permission = permission;
        this.options = options;
        this.messages = messages;
        this.cmdHandler = cmdHandler;
        this.traceService = traceService;
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String command = event.getMessage().toLowerCase();
        traceService.sendTraceMessage(COMMANDS, uuid, "Player invoked command: [" + command + "]");

        if (command.startsWith("/help staffplusplus") || command.startsWith("/help staff++")) {
            sendHelp(player);
            event.setCancelled(true);
            return;
        }

        OnlinePlayerSession session = sessionManager.get(player);
        if (blockedCommands.contains(command) && permission.hasOnly(player, permissionBlock)) {
            messages.send(player, messages.commandBlocked, messages.prefixGeneral);
            event.setCancelled(true);
        } else if (session.isInStaffMode() && blockedModeCommands.contains(command)) {
            messages.send(player, messages.modeCommandBlocked, messages.prefixGeneral);
            event.setCancelled(true);
        }
    }


    private void sendHelp(Player player) {
        int count = 0;

        messages.send(player, "&7" + messages.LONG_LINE, "");

        List<BaseCmd> sortedCommands = cmdHandler.commands.stream()
            .sorted(Comparator.comparing(o -> o.getCommand().getName()))
            .collect(Collectors.toList());

        for (BaseCmd baseCmd : sortedCommands) {
            if (baseCmd.getPermissions().isEmpty()) {
                messages.send(player, "&b/" + baseCmd.getCommand().getName() + " &7: " + baseCmd.getDescription().toLowerCase(), "");
                count++;
            } else {
                for (String permission : baseCmd.getPermissions()) {
                    if (this.permission.has(player, permission)) {
                        messages.send(player, "&b/" + baseCmd.getCommand().getName() + " &7: " + baseCmd.getDescription().toLowerCase(), "");
                        count++;
                        break;
                    }
                }
            }
        }

        if (count == 0) {
            messages.send(player, messages.noPermission, messages.prefixGeneral);
        }

        messages.send(player, "&7" + messages.LONG_LINE, "");
    }
}