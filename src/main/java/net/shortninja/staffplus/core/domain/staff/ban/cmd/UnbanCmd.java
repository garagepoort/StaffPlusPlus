package net.shortninja.staffplus.core.domain.staff.ban.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;

import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.ban.BanService;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IocBean(conditionalOnProperty = "ban-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class UnbanCmd extends AbstractCmd {

    private final BanService banService;
    private final BanConfiguration banConfiguration;
    private final PlayerManager playerManager;
    private final PermissionHandler permissionHandler;

    public UnbanCmd(Messages messages, BanConfiguration banConfiguration, Options options, BanService banService, CommandService commandService, PlayerManager playerManager, PermissionHandler permissionHandler) {
        super(banConfiguration.getCommandUnbanPlayer(), messages, options, commandService);
        this.banConfiguration = banConfiguration;
        this.playerManager = playerManager;
        this.banService = banService;
        this.permissionHandler = permissionHandler;
        setPermission(banConfiguration.getPermissionUnbanPlayer());
        setDescription("Unban a player");
        setUsage("[player] [reason]");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        boolean isSilent = Arrays.stream(args).anyMatch(a -> a.equalsIgnoreCase("-silent"));
        if(isSilent && !permissionHandler.has(sender, banConfiguration.getPermissionBanSilent())) {
            throw new NoPermissionException("You don't have the permission to execute a silent unban");
        }

        args = Arrays.stream(args).filter(a -> !a.equalsIgnoreCase("-silent")).toArray(String[]::new);
        String reason = JavaUtils.compileWords(args, 1);

        banService.unban(sender, player, reason, isSilent);
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 2;
    }

    @Override
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.BOTH;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        String[] finalArgs = Arrays.stream(args).filter(a -> !a.equalsIgnoreCase("-silent")).toArray(String[]::new);
        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> finalArgs[0].isEmpty() || s.contains(finalArgs[0]))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
