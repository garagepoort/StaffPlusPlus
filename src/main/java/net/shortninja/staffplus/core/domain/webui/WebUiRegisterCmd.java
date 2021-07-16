package net.shortninja.staffplus.core.domain.webui;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.webui.config.WebUiConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

@Command(
    command = "commands:webui.register",
    permissions = "permissions:webui.register",
    description = "Create registration link for the web interface",
    usage = "[player] [enable | disable]"
)
@IocBean(conditionalOnProperty = "webui-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class WebUiRegisterCmd extends AbstractCmd {

    private final Options options;
    private final WebUiRegistrationRepository webUiRegistrationRepository;
    private final WebUiConfiguration webUiConfiguration;

    public WebUiRegisterCmd(Messages messages, WebUiConfiguration webUiConfiguration, CommandService commandService, WebUiRegistrationRepository webUiRegistrationRepository, PermissionHandler permissionHandler, Options options) {
        super(messages, permissionHandler, commandService);
        this.webUiConfiguration = webUiConfiguration;
        this.webUiRegistrationRepository = webUiRegistrationRepository;
        this.options = options;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        if (!(sender instanceof Player)) {
            throw new BusinessException(messages.onlyPlayers);
        }

        String authenticationKey = RandomStringUtils.randomAlphanumeric(32);
        String serverName = options.serverName;
        String uuid = ((Player) sender).getUniqueId().toString();
        String applicationKey = webUiConfiguration.applicationKey;
        webUiRegistrationRepository.addRegistrationRequest(sender.getName(), ((Player) sender).getUniqueId(), authenticationKey, webUiConfiguration.role);

        String registrationLink = String.format(webUiConfiguration.host + "/register?applicationKey=%s&&uuid=%s&&authenticationKey=%s&&serverName=%s", applicationKey, uuid, authenticationKey, serverName);
        messages.send(sender, "&bRegistration link: &6" + registrationLink, messages.prefixGeneral);
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
