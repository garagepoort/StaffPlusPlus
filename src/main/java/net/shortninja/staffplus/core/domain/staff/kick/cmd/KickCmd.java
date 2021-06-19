package net.shortninja.staffplus.core.domain.staff.kick.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.kick.KickService;
import net.shortninja.staffplus.core.domain.staff.kick.config.KickReasonConfiguration;
import net.shortninja.staffplus.core.domain.staff.kick.gui.KickReasonSelectGui;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean(conditionalOnProperty = "kick-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class KickCmd extends AbstractCmd {

    private final PermissionHandler permissionHandler;
    private final KickService kickService;
    private final List<KickReasonConfiguration> kickReasonConfigurations;

    public KickCmd(PermissionHandler permissionHandler, Messages messages, Options options, KickService kickService, CommandService commandService) {
        super(options.kickConfiguration.getCommandKickPlayer(), messages, options, commandService);
        this.permissionHandler = permissionHandler;
        this.kickService = kickService;
        setPermission(options.kickConfiguration.getPermissionKickPlayer());
        setDescription("Kick a player");
        setUsage("[player] [reason?]");
        kickReasonConfigurations = options.kickConfiguration.getKickReasons();
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {

        if (kickReasonConfigurations.isEmpty() || (!options.kickConfiguration.isFixedReason() && args.length == 2)) {
            String reason = JavaUtils.compileWords(args, 1);
            kickService.kick(sender, player, reason);
            return true;
        }

        if (kickReasonConfigurations.size() == 1) {
            kickService.kick(sender, player, kickReasonConfigurations.get(0).getReason());
            return true;
        }

        validateIsPlayer(sender);
        new KickReasonSelectGui((Player) sender, player, kickReasonConfigurations);
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
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.ONLINE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, options.kickConfiguration.getPermissionKickByPass());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
