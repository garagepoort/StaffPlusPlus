package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.session.PlayerSession;
import net.shortninja.staffplus.core.application.session.SessionManagerImpl;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishServiceImpl;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;

@Command(
    command = "commands:vanish",
    permissions = "permissions:vanish",
    description = "Enables or disables the type of vanish for the player.",
    usage = "[total | list | player] {player} {enable | disable}",
    playerRetrievalStrategy = ONLINE
)
@IocBean(conditionalOnProperty = "vanish-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class VanishCmd extends AbstractCmd {
    private final VanishServiceImpl vanishServiceImpl;
    private final PermissionHandler permissionHandler;
    private final PlayerManager playerManager;
    private final VanishConfiguration vanishConfiguration;
    private final SessionManagerImpl sessionManager;
    private final BukkitUtils bukkitUtils;

    public VanishCmd(Messages messages, VanishServiceImpl vanishServiceImpl, CommandService commandService, PermissionHandler permissionHandler, PlayerManager playerManager, VanishConfiguration vanishConfiguration, SessionManagerImpl sessionManager, BukkitUtils bukkitUtils) {
        super(messages, permissionHandler, commandService);
        this.vanishServiceImpl = vanishServiceImpl;
        this.permissionHandler = permissionHandler;
        this.playerManager = playerManager;
        this.vanishConfiguration = vanishConfiguration;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
    }


    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {

        VanishType vanishType = VanishType.valueOf(args[0].toUpperCase());

        if (args.length >= 3 && permissionHandler.isOp(sender)) {
            bukkitUtils.runTaskAsync(sender, () -> {
                String option = args[2];
                if (option.equalsIgnoreCase("enable")) {
                    handleVanishArgument(vanishType, targetPlayer.getPlayer(), false);
                } else {
                    vanishServiceImpl.removeVanish(targetPlayer.getPlayer());
                }
            });
            return true;
        }

        if (args.length == 2 && permissionHandler.isOp(sender)) {
            bukkitUtils.runTaskAsync(sender, () -> handleVanishArgument(vanishType, targetPlayer.getPlayer(), false));
            return true;
        }

        if (args.length == 1) {
            validateIsPlayer(sender);
            bukkitUtils.runTaskAsync(sender, () -> handleVanishArgument(vanishType, (Player) sender, true));
            return true;
        }

        sendHelp(sender);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return 1;
        }
        return 2;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        if (args.length > 1) {
            return Optional.of(args[1]);
        }
        if (sender instanceof Player) {
            return Optional.of(sender.getName());
        }
        return Optional.empty();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Arrays.stream(VanishType.values()).map(Enum::name)
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }


        if (args.length == 3) {
            return Stream.of("enable", "disable")
                .filter(s -> args[2].isEmpty() || s.contains(args[2]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private void handleVanishArgument(VanishType vanishType, Player player, boolean shouldCheckPermission) {

        switch (vanishType) {
            case TOTAL:
                setVanish(vanishType, player, shouldCheckPermission, vanishConfiguration.permissionVanishTotal);
                break;
            case LIST:
                setVanish(vanishType, player, shouldCheckPermission, vanishConfiguration.permissionVanishList);
                break;
            case PLAYER:
                setVanish(vanishType, player, shouldCheckPermission, vanishConfiguration.permissionVanishPlayer);
                break;
            case NONE:
                if (permissionHandler.hasAny(player, vanishConfiguration.permissionVanishList, vanishConfiguration.permissionVanishPlayer, vanishConfiguration.permissionVanishTotal) || !shouldCheckPermission) {
                    vanishServiceImpl.removeVanish(player);
                } else messages.send(player, messages.noPermission, messages.prefixGeneral);
                break;
        }
    }

    private void setVanish(VanishType vanishType, Player player, boolean shouldCheckPermission, String permissionVanishTotal) {
        PlayerSession session = sessionManager.get(player.getUniqueId());
        if (shouldCheckPermission && !permissionHandler.has(player, permissionVanishTotal)) {
            messages.send(player, messages.noPermission, messages.prefixGeneral);
            return;
        }

        if (session.getVanishType() != vanishType) {
            vanishServiceImpl.addVanish(player, vanishType);
        } else {
            vanishServiceImpl.removeVanish(player);
        }
    }

    private void sendHelp(CommandSender sender) {
        messages.send(sender, "&7" + messages.LONG_LINE, "");
        messages.send(sender, "&b/" + getName() + " &7" + getUsage(), messages.prefixGeneral);
        messages.send(sender, "&7" + messages.LONG_LINE, "");
    }
}