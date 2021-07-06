package net.shortninja.staffplus.core.domain.staff.teleport.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;

import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.teleport.TeleportService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.HEALTH;
import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.STRIP;

@IocBean
@IocMultiProvider(SppCommand.class)
public class TeleportToLocationCmd extends AbstractCmd {

    private final PermissionHandler permissionHandler;
    private final PlayerManager playerManager;
    private final TeleportService teleportService;

    public TeleportToLocationCmd(PermissionHandler permissionHandler, Messages messages, PlayerManager playerManager, Options options, TeleportService teleportService, CommandService commandService) {
        super(options.commandTeleportToLocation, messages, options, commandService);
        this.permissionHandler = permissionHandler;
        this.playerManager = playerManager;
        this.teleportService = teleportService;
        setDescription("Teleports the player to predefined locations");
        setUsage("{player} {location}");
        setPermission(options.permissionTeleportToLocation);
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {
        String locationName = args[1];

        teleportService.teleportPlayerToLocation(sender, targetPlayer.getPlayer(), locationName);
        return true;
    }

    @Override
    protected List<ArgumentType> getPostExecutionSppArguments() {
        return Arrays.asList(STRIP, HEALTH);
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected boolean isDelayable() {
        return true;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, options.permissionTeleportBypass);
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }
        if (args.length == 2) {
            options.locations.forEach((k, v) -> {
                suggestions.add(k);
            });
            return suggestions.stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}