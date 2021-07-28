package net.shortninja.staffplus.core.domain.staff.kick.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.gui.GuiActionBuilder;
import be.garagepoort.mcioc.gui.GuiActionService;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.kick.KickService;
import net.shortninja.staffplus.core.domain.staff.kick.config.KickReasonConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.ONLINE;

@Command(
    command = "commands:kick",
    permissions = "permissions:kick",
    description = "Kick a player",
    usage = "[player] [reason]",
    playerRetrievalStrategy = ONLINE
)
@IocBean(conditionalOnProperty = "kick-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class KickCmd extends AbstractCmd {

    private final PermissionHandler permissionHandler;
    private final KickService kickService;
    private final List<KickReasonConfiguration> kickReasonConfigurations;
    private final Options options;
    private final GuiActionService guiActionService;

    @ConfigProperty("permissions:kick-bypass")
    private String permissionKickByPass;

    public KickCmd(PermissionHandler permissionHandler, Messages messages, Options options, KickService kickService, CommandService commandService, Options options1, GuiActionService guiActionService) {
        super(messages, permissionHandler, commandService);
        this.permissionHandler = permissionHandler;
        this.kickService = kickService;
        this.options = options1;
        this.kickReasonConfigurations = options.kickConfiguration.getKickReasons();
        this.guiActionService = guiActionService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {

        if (kickReasonConfigurations.isEmpty() || (!options.kickConfiguration.isFixedReason() && args.length == 2)) {
            String reason = JavaUtils.compileWords(args, 1);
            kickService.kick(sender, player, reason);
            return true;
        }

        if (kickReasonConfigurations.size() == 1) {
            kickService.kick(sender, player, kickReasonConfigurations.get(0).getReason());
            return true;
        }

        if (args.length == 2) {
            String reason = JavaUtils.compileWords(args, 1);
            if (kickReasonConfigurations.stream().anyMatch(k -> k.getReason().equalsIgnoreCase(reason))) {
                kickService.kick(sender, player, reason);
            }
        }

        validateIsPlayer(sender);

        guiActionService.executeAction((Player) sender, GuiActionBuilder.builder()
            .action("manage-kicks/view/kick/reason-select")
            .param("targetPlayerName", player.getUsername())
            .build());
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (kickReasonConfigurations.isEmpty()) {
            return 2;
        }
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, permissionKickByPass);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }

        if (args.length == 2 && !kickReasonConfigurations.isEmpty()) {
            return kickReasonConfigurations.stream()
                .map(KickReasonConfiguration::getReason)
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
