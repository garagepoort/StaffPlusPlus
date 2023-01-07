package net.shortninja.staffplus.core.domain.commanddetection;

import be.garagepoort.mcioc.configuration.ConfigObjectList;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.tubingbukkit.annotations.IocBukkitListener;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.actions.ActionService;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandMapper;
import net.shortninja.staffplusplus.commanddetection.CommandDetectedEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.shortninja.staffplus.core.common.utils.BukkitUtils.sendEvent;

@IocBukkitListener(conditionalOnProperty = "command-detection.enabled=true")
public class CommandDetectionListener implements Listener {

    @ConfigProperty("permissions:command-detection-bypass")
    private String permissionCommandDetectionBypass;

    @ConfigProperty("command-detection.command-groups")
    @ConfigObjectList(CommandDetectionGroupConfiguration.class)
    private List<CommandDetectionGroupConfiguration> commandsToDetect;

    private final BukkitUtils bukkitUtils;
    private final ActionService actionService;
    private final ConfiguredCommandMapper configuredCommandMapper;
    private final PermissionHandler permissionHandler;

    public CommandDetectionListener(BukkitUtils bukkitUtils, ActionService actionService, ConfiguredCommandMapper configuredCommandMapper, PermissionHandler permissionHandler) {
        this.bukkitUtils = bukkitUtils;
        this.actionService = actionService;
        this.configuredCommandMapper = configuredCommandMapper;
        this.permissionHandler = permissionHandler;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(permissionHandler.has(player, permissionCommandDetectionBypass)) {
            return;
        }

        String command = event.getMessage().toLowerCase();

        for (CommandDetectionGroupConfiguration groupConfig : commandsToDetect) {
            if(groupConfig.commands.stream().anyMatch(prefix -> command.startsWith("/" + prefix))){
                executeActions(player, groupConfig.actions, command);
                sendEvent(new CommandDetectedEvent(player, event.getMessage(), event.getPlayer().getWorld(), System.currentTimeMillis()));
            }
        }
    }

    private void executeActions(Player player, List<ConfiguredCommand> commands, String detectedCommand) {
        bukkitUtils.runTaskAsync(() -> {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%player%", player.getName());
            placeholders.put("%command%", detectedCommand);

            Map<String, OfflinePlayer> targets = new HashMap<>();
            targets.put("player", player);

            actionService.createCommands(configuredCommandMapper.toCreateRequests(commands, placeholders, targets, new ArrayList<>()));
        });
    }

}
