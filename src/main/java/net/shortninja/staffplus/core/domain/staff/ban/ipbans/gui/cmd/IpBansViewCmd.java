package net.shortninja.staffplus.core.domain.staff.ban.ipbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.utils.BukkitUtils;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpRecord;
import net.shortninja.staffplus.core.domain.player.ip.PlayerIpService;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Command(
    command = "commands:ipban.ipbans",
    permissions = "permissions:ipban.ban-view",
    description = "List all ip banning rules"
)
@IocBean(conditionalOnProperty = "ban-module.ipban.enabled=true")
@IocMultiProvider(SppCommand.class)
public class IpBansViewCmd extends AbstractCmd {

    private static final String PLAYERS = "-players";
    private final IpBanService banService;
    private final BukkitUtils bukkitUtils;
    private final PlayerIpService playerIpService;

    public IpBansViewCmd(Messages messages, IpBanService banService, CommandService commandService, PermissionHandler permissionHandler, BukkitUtils bukkitUtils, PlayerIpService playerIpService) {
        super(messages, permissionHandler, commandService);
        this.banService = banService;
        this.bukkitUtils = bukkitUtils;
        this.playerIpService = playerIpService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        bukkitUtils.runTaskAsync(sender, () -> {
            if (optionalParameters.containsKey(PLAYERS)) {
                listBannedPlayers(sender);
            } else {
                listIps(sender);
            }
        });
        return true;
    }

    private void listBannedPlayers(CommandSender sender) {
        List<PlayerIpRecord> bannedPlayers = banService.getAllActiveBans()
            .stream()
            .flatMap(ipBan -> ipBan.isSubnet() ? playerIpService.getMatchedBySubnet(ipBan.getIp()).stream() : playerIpService.getMatchedByIp(ipBan.getIp()).stream())
            .collect(Collectors.toList());

        messages.send(sender, "&6All players that are matching active ip bans: ", messages.prefixBans);
        messages.send(sender, messages.LONG_LINE, messages.prefixBans);
        for (int i = 0; i < bannedPlayers.size(); i++) {
            PlayerIpRecord playerIpRecord = bannedPlayers.get(i);
            messages.send(sender, "&c" + (i + 1) + ". &7" + playerIpRecord.getPlayerName(), messages.prefixBans);
        }
    }

    private void listIps(CommandSender sender) {
        List<IpBan> matchingIpBans = banService.getAllActiveBans();
        messages.send(sender, "&6All ip banning rules: ", messages.prefixBans);
        messages.send(sender, messages.LONG_LINE, messages.prefixBans);
        for (int i = 0; i < matchingIpBans.size(); i++) {
            IpBan matchingIpBan = matchingIpBans.get(i);
            messages.send(sender, "&c" + (i + 1) + ". &7" + matchingIpBan.getIp(), messages.prefixBans);
        }
    }

    @Override
    protected List<String> getOptionalParameters() {
        return Collections.singletonList(PLAYERS);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }

    @Override
    protected List<String> autoComplete(CommandSender sender, String[] args, String[] sppArgs) throws IllegalArgumentException {
        if(args.length == 1) {
            return Collections.singletonList(PLAYERS);
        }
        return Collections.emptyList();
    }
}
