package net.shortninja.staffplus.core.domain.staff.ban.playerbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.NoPermissionException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.BanService;
import net.shortninja.staffplus.core.domain.staff.ban.playerbans.config.BanConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.BOTH;

@Command(
    command = "commands:commands.unban",
    permissions = "permissions:permissions.unban",
    description = "Unban a player",
    usage = "[player] [reason]",
    playerRetrievalStrategy = BOTH
)
@IocBean(conditionalOnProperty = "ban-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class UnbanCmd extends AbstractCmd {

    private final BanService banService;
    private final BanConfiguration banConfiguration;
    private final PlayerManager playerManager;
    private final PermissionHandler permissionHandler;

    public UnbanCmd(Messages messages,
                    BanConfiguration banConfiguration,
                    Options options,
                    BanService banService,
                    CommandService commandService,
                    PlayerManager playerManager,
                    PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.banConfiguration = banConfiguration;
        this.playerManager = playerManager;
        this.banService = banService;
        this.permissionHandler = permissionHandler;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        boolean isSilent = Arrays.stream(args).anyMatch(a -> a.equalsIgnoreCase("-silent"));
        if(isSilent && !permissionHandler.has(sender, banConfiguration.permissionBanSilent)) {
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
