package net.shortninja.staffplus.core.domain.staff.ban.ipbans.gui.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBan;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanConfiguration;
import net.shortninja.staffplus.core.domain.staff.ban.ipbans.IpBanService;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@IocBean(conditionalOnProperty = "ban-module.ipban.enabled=true")
@IocMultiProvider(SppCommand.class)
public class IpBansViewCmd extends AbstractCmd {

    private final IpBanService banService;

    public IpBansViewCmd(Messages messages, Options options, IpBanConfiguration banConfiguration, IpBanService banService, CommandService commandService) {
        super(banConfiguration.commandIpBans, messages, options, commandService);
        this.banService = banService;
        setPermission(banConfiguration.permissionIpBanView);
        setDescription("List all ip banning rules");
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
    protected PlayerRetrievalStrategy getPlayerRetrievalStrategy() {
        return PlayerRetrievalStrategy.NONE;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        return Optional.empty();
    }
}
