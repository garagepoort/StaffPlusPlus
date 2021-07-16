package net.shortninja.staffplus.core.domain.staff.ban.ipbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.cmd.*;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.ip.database.PlayerIpRepository;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.BOTH;
import static net.shortninja.staffplus.core.common.utils.BukkitUtils.getIpFromPlayer;

@Command(
    command = "commands:commands.ipban.bancheck",
    permissions = "permissions:permissions.ipban.ban-check",
    description = "Check if a player is ip banned",
    usage = "[player]",
    playerRetrievalStrategy = BOTH
)
@IocBean(conditionalOnProperty = "ban-module.ipban.enabled=true")
@IocMultiProvider(SppCommand.class)
public class IpBanCheckCmd extends AbstractCmd {

    private final IpBanService banService;
    private final PlayerManager playerManager;
    private final PlayerIpRepository playerIpRepository;

    public IpBanCheckCmd(Messages messages, Options options, IpBanService banService, CommandService commandService, PlayerManager playerManager, PlayerIpRepository playerIpRepository, PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.banService = banService;
        this.playerManager = playerManager;
        this.playerIpRepository = playerIpRepository;
    }


    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        Optional<String> ipAddress = getIpAddress(player);
        List<IpBan> matchingIpBans = ipAddress.isPresent() ? banService.findMatchingIpBans(ipAddress.get()) : Collections.emptyList();
        if (matchingIpBans.isEmpty()) {
            messages.send(sender, "&6This player is &2not ip banned.", messages.prefixBans);
        } else {
            messages.send(sender, "&6This player is &cip banned &6by following rules: ", messages.prefixBans);
            messages.send(sender, messages.LONG_LINE, messages.prefixBans);
            for (int i = 0, matchingIpBansSize = matchingIpBans.size(); i < matchingIpBansSize; i++) {
                IpBan matchingIpBan = matchingIpBans.get(i);
                messages.send(sender, "&c" + (i + 1) + ". &7" + matchingIpBan.getIp(), messages.prefixBans);
            }
        }
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 1;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.ofNullable(args[0]);
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private Optional<String> getIpAddress(SppPlayer sppPlayer) {
        return sppPlayer.isOnline() ? Optional.of(getIpFromPlayer(sppPlayer.getPlayer())) : playerIpRepository.getLastIp(sppPlayer.getId());
    }
}
