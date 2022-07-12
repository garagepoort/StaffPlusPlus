package net.shortninja.staffplus.core.domain.staff.vanish.gui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import be.garagepoort.mcioc.configuration.transformers.ToEnum;
import net.shortninja.staffplus.core.application.config.messages.Messages;
import net.shortninja.staffplus.core.application.session.OnlinePlayerSession;
import net.shortninja.staffplus.core.application.session.OnlineSessionsManager;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishConfiguration;
import net.shortninja.staffplus.core.domain.staff.vanish.VanishService;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplusplus.vanish.VanishType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
    public static final String ENABLE = "enable";
    private final VanishService vanishService;
    private final PermissionHandler permissionHandler;
    private final PlayerManager playerManager;
    private final VanishConfiguration vanishConfiguration;
    private final OnlineSessionsManager sessionManager;
    private final BukkitUtils bukkitUtils;

    @ConfigProperty("permissions:vanish-others.total")
    private String permissionVanishOthersTotal;
    @ConfigProperty("permissions:vanish-others.list")
    private String permissionVanishOthersList;
    @ConfigProperty("permissions:vanish-others.player")
    private String permissionVanishOthersPlayer;

    @ConfigProperty("vanish-module.default-mode")
    @ConfigTransformer(ToEnum.class)
    private VanishType defaultVanishType;

    public VanishCmd(Messages messages,
                     VanishService vanishService,
                     CommandService commandService,
                     PermissionHandler permissionHandler,
                     PlayerManager playerManager,
                     VanishConfiguration vanishConfiguration,
                     OnlineSessionsManager sessionManager,
                     BukkitUtils bukkitUtils) {
        super(messages, permissionHandler, commandService);
        this.vanishService = vanishService;
        this.permissionHandler = permissionHandler;
        this.playerManager = playerManager;
        this.vanishConfiguration = vanishConfiguration;
        this.sessionManager = sessionManager;
        this.bukkitUtils = bukkitUtils;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer targetPlayer, Map<String, String> optionalParameters) {

        VanishType vanishType = args.length != 0 ? VanishType.valueOf(args[0].toUpperCase()) : defaultVanishType;

        if (args.length >= 3) {
            permissionHandler.validateAny(sender, permissionVanishOthersPlayer, permissionVanishOthersTotal, permissionVanishOthersList);
            String enableDisable = args[2];
            validatePermissionOther(sender, vanishType);
            bukkitUtils.runTaskAsync(sender, () -> {
                if (enableDisable.equalsIgnoreCase(ENABLE)) {
                    vanish(targetPlayer.getPlayer(), vanishType);
                } else {
                    vanishService.removeVanish(targetPlayer.getPlayer());
                }
            });
            return true;
        }

        if (args.length == 2) {
            validatePermissionOther(sender, vanishType);
            bukkitUtils.runTaskAsync(sender, () -> vanish(targetPlayer.getPlayer(), vanishType));
            return true;
        }

        validateIsPlayer(sender);
        validatePermissionSelf(sender, vanishType);
        bukkitUtils.runTaskAsync(sender, () -> vanish((Player) sender, vanishType));
        return true;
    }

    private void validatePermissionSelf(CommandSender sender, VanishType vanishType) {
        switch (vanishType) {
            case LIST:
                permissionHandler.validate(sender, vanishConfiguration.permissionVanishList);
                break;
            case PLAYER:
                permissionHandler.validate(sender, vanishConfiguration.permissionVanishPlayer);
                break;
            case TOTAL:
                permissionHandler.validate(sender, vanishConfiguration.permissionVanishTotal);
                break;
        }
    }

    private void validatePermissionOther(CommandSender sender, VanishType vanishType) {
        switch (vanishType) {
            case LIST:
                permissionHandler.validate(sender, permissionVanishOthersList);
                break;
            case PLAYER:
                permissionHandler.validate(sender, permissionVanishOthersPlayer);
                break;
            case TOTAL:
                permissionHandler.validate(sender, permissionVanishOthersTotal);
                break;
        }
    }

    private void vanish(Player player, VanishType vanishType) {
        switch (vanishType) {
            case TOTAL:
            case LIST:
            case PLAYER:
                setVanish(vanishType, player);
                break;
            case NONE:
                vanishService.removeVanish(player);
                break;
            default:
                throw new BusinessException("No vanishtype " + vanishType.name());
        }
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return 0;
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
            return getAllowedVanishTypes(sender).stream().map(Enum::name)
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (permissionHandler.hasAny(sender, permissionVanishOthersPlayer, permissionVanishOthersTotal, permissionVanishOthersList)) {
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
        }

        return Collections.emptyList();
    }

    private List<VanishType> getAllowedVanishTypes(CommandSender player) {
        List<VanishType> allowedVanishTypes = new ArrayList<>();
        if (permissionHandler.has(player, vanishConfiguration.permissionVanishTotal)) {
            allowedVanishTypes.add(VanishType.TOTAL);
        }
        if (permissionHandler.has(player, vanishConfiguration.permissionVanishList)) {
            allowedVanishTypes.add(VanishType.LIST);
        }
        if (permissionHandler.has(player, vanishConfiguration.permissionVanishPlayer)) {
            allowedVanishTypes.add(VanishType.PLAYER);
        }
        return allowedVanishTypes;
    }

    private void setVanish(VanishType vanishType, Player player) {
        OnlinePlayerSession session = sessionManager.get(player);
        if (session.getVanishType() != vanishType) {
            vanishService.addVanish(player, vanishType);
        } else {
            vanishService.removeVanish(player);
        }
    }
}