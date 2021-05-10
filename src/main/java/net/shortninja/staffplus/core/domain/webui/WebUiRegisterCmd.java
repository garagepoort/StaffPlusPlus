package net.shortninja.staffplus.core.domain.webui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@IocBean(conditionalOnProperty = "webui-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class WebUiRegisterCmd extends AbstractCmd {

    private final WebUiRegistrationRepository webUiRegistrationRepository;

    public WebUiRegisterCmd(Messages messages, Options options, CommandService commandService, WebUiRegistrationRepository webUiRegistrationRepository) {
        super(options.webuiConfiguration.getRegistrationCmd(), messages, options, commandService);
        this.webUiRegistrationRepository = webUiRegistrationRepository;
        setPermission(options.webuiConfiguration.getRegistrationPermission());
        setDescription("Create registration link for the web interface");
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        String authenticationKey = RandomStringUtils.randomAlphanumeric(32);
        String serverName = options.serverName;
        String uuid = ((Player) sender).getUniqueId().toString();
        String applicationKey = options.webuiConfiguration.getApplicationKey();
        webUiRegistrationRepository.addRegistrationRequest(sender.getName(), ((Player) sender).getUniqueId(), authenticationKey, options.webuiConfiguration.getRole());

        String registrationLink = String.format(options.webuiConfiguration.getHost() + "/register?applicationKey=%s&&uuid=%s&&authenticationKey=%s&&serverName=%s", applicationKey, uuid, authenticationKey, serverName);
        messages.send(sender, "&bRegistration link: &6" + registrationLink, messages.prefixGeneral);
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
