package net.shortninja.staffplus.core.domain.staff.ban.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import net.shortninja.staffplus.core.authentication.AuthenticationService;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.config.Messages;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.utils.MessageCoordinator;
import net.shortninja.staffplus.core.common.utils.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.player.SppPlayer;
import net.shortninja.staffplus.core.domain.staff.ban.BanService;
import net.shortninja.staffplus.core.domain.staff.ban.config.BanReasonConfiguration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.domain.staff.ban.BanType.PERM_BAN;

@IocBean(conditionalOnProperty = "ban-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class BanCmd extends AbstractCmd {

    private static final String TEMPLATE_FILE = "-template=";

    private final BanService banService;

    public BanCmd(PermissionHandler permissionHandler, AuthenticationService authenticationService, Messages messages, MessageCoordinator message, PlayerManager playerManager, Options options, BanService banService) {
        super(options.banConfiguration.getCommandBanPlayer(), permissionHandler, authenticationService, messages, message, playerManager, options);
        this.banService = banService;
        setPermission(options.banConfiguration.getPermissionBanPlayer());
        setDescription("Permanent ban a player");
        setUsage("[player] [-template=?] [reason]");
    }


    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player) {

        if (args[1].toLowerCase().startsWith(TEMPLATE_FILE.toLowerCase())) {
            String template = getTemplateName(args[1]);
            String reason = JavaUtils.compileWords(args, 2);
            banService.permBan(sender, player, reason, template);
            return true;
        }

        String reason = JavaUtils.compileWords(args, 1);
        banService.permBan(sender, player, reason);
        return true;
    }

    private String getTemplateName(String arg) {
        String[] templateParams = arg.split("=");
        if (templateParams.length != 2) {
            throw new BusinessException("&CInvalid template provided");
        }
        return templateParams[1];
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        if (args.length >= 2 && args[1].toLowerCase().contains(TEMPLATE_FILE.toLowerCase())) {
            return 3;
        }
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
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, options.banConfiguration.getPermissionBanByPass());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        String currentArg = args.length > 0 ? args[args.length - 1] : "";

        if (args.length == 1) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
                .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if (currentArg.startsWith("-")) {
                return getTemplateCompletion();
            } else if (!options.banConfiguration.getBanReasons(PERM_BAN).isEmpty()) {
                return getBanReasonCompletion(currentArg);
            }
        }

        if (args.length == 3 && !options.banConfiguration.getBanReasons(PERM_BAN).isEmpty()) {
            return getBanReasonCompletion(currentArg);
        }

        return Collections.emptyList();
    }

    private List<String> getTemplateCompletion() {
        return options.banConfiguration.getTemplates().keySet().stream()
            .map(k -> TEMPLATE_FILE + k)
            .collect(Collectors.toList());
    }

    private List<String> getBanReasonCompletion(String currentArg) {
        return options.banConfiguration.getBanReasons(PERM_BAN).stream()
            .map(BanReasonConfiguration::getName)
            .filter(s -> currentArg.isEmpty() || s.contains(currentArg))
            .collect(Collectors.toList());
    }
}
