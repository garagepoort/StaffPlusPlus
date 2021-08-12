package net.shortninja.staffplus.core.domain.staff.freeze;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;
import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.HEALTH;
import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.STRIP;
import static net.shortninja.staffplus.core.common.cmd.arguments.ArgumentType.TELEPORT;

@Command(
    command = "commands:freeze",
    permissions = "permissions:freeze",
    description = "Freezes or unfreezes the player",
    usage = "[enabled | disabled] [player]",
    delayable = true,
    playerRetrievalStrategy = ONLINE
)
@IocBean
@IocMultiProvider(SppCommand.class)
public class FreezeCmd extends AbstractCmd {

    private static final String ENABLED = "enabled";
    private static final String DISABLED = "disabled";

    private final PermissionHandler permissionHandler;
    private final FreezeHandler freezeHandler;
    private final PlayerManager playerManager;
    private final OnlineSessionsManager onlineSessionsManager;

    @ConfigProperty("permissions:freeze-bypass")
    private String permissionFreezeBypass;

    public FreezeCmd(PermissionHandler permissionHandler, Messages messages, FreezeHandler freezeHandler, CommandService commandService, PlayerManager playerManager, OnlineSessionsManager onlineSessionsManager) {
        super(messages, permissionHandler, commandService);
        this.permissionHandler = permissionHandler;
        this.freezeHandler = freezeHandler;
        this.playerManager = playerManager;
        this.onlineSessionsManager = onlineSessionsManager;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer sppPlayer, Map<String, String> optionalParameters) {
        freezeHandler.execute(buildFreezeRequest(sender, args, sppPlayer.getPlayer()));
        return true;
    }

    @Override
    protected List<ArgumentType> getPreExecutionSppArguments() {
        return Arrays.asList(TELEPORT, STRIP, HEALTH);
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase(ENABLED) || args[0].equalsIgnoreCase(DISABLED)) {
            return Optional.ofNullable(args[1]);
        }
        return Optional.of(args[0]);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(ENABLED) || args[0].equalsIgnoreCase(DISABLED)) {
                return 2;
            }
        }
        return 1;
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, permissionFreezeBypass);
    }

    private FreezeRequest buildFreezeRequest(CommandSender sender, String[] args, Player targetPlayer) {
        OnlinePlayerSession session = onlineSessionsManager.get(targetPlayer);
        boolean freeze = !session.isFrozen();

        if (args[0].equalsIgnoreCase(ENABLED) || args[0].equalsIgnoreCase(DISABLED)) {
            freeze = args[0].equalsIgnoreCase(ENABLED);
        }

        return new FreezeRequest(sender, targetPlayer, freeze);
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            if (!args[0].equalsIgnoreCase(ENABLED) && !args[0].equalsIgnoreCase(DISABLED)) {
                suggestions.add(ENABLED);
                suggestions.add(DISABLED);
            }
            suggestions.addAll(playerManager.getAllPlayerNames());
            return suggestions.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase(ENABLED) || args[0].equalsIgnoreCase(DISABLED)) {
                return playerManager.getAllPlayerNames().stream()
                    .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                    .collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }
}