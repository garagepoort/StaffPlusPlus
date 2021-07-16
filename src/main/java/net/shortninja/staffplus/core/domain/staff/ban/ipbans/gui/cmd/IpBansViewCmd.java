package net.shortninja.staffplus.core.domain.staff.ban.ipbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Command(
    command = "commands:commands.ipban.ipbans",
    permissions = "permissions:permissions.ipban.ban-view",
    description = "List all ip banning rules"
)
@IocBean(conditionalOnProperty = "ban-module.ipban.enabled=true")
@IocMultiProvider(SppCommand.class)
public class IpBansViewCmd extends AbstractCmd {

    private final IpBanService banService;

    public IpBansViewCmd(Messages messages, Options options, IpBanService banService, CommandService commandService, PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.banService = banService;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        List<IpBan> matchingIpBans = banService.getAllActiveBans();
        messages.send(sender, "&6All ip banning rules: ", messages.prefixBans);
        messages.send(sender, messages.LONG_LINE, messages.prefixBans);
        for (int i = 0, matchingIpBansSize = matchingIpBans.size(); i < matchingIpBansSize; i++) {
            IpBan matchingIpBan = matchingIpBans.get(i);
            messages.send(sender, "&c" + (i + 1) + ". &7" + matchingIpBan.getIp(), messages.prefixBans);
        }
        return true;
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        return 0;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}
