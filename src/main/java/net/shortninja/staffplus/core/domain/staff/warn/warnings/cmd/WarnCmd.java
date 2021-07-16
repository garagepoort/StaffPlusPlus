package net.shortninja.staffplus.core.domain.staff.warn.warnings.cmd;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMultiProvider;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import net.shortninja.staffplus.core.application.config.Messages;
import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.common.cmd.AbstractCmd;
import net.shortninja.staffplus.core.common.cmd.Command;
import net.shortninja.staffplus.core.common.cmd.CommandService;
import net.shortninja.staffplus.core.common.cmd.SppCommand;
import net.shortninja.staffplus.core.common.exceptions.BusinessException;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.WarnService;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningConfiguration;
import net.shortninja.staffplus.core.domain.staff.warn.warnings.config.WarningSeverityConfiguration;
import net.shortninja.staffplusplus.session.SppPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static net.shortninja.staffplus.core.common.cmd.PlayerRetrievalStrategy.BOTH;

@Command(
    command = "commands:commands.warn",
    permissions = "permissions:permissions.warn",
    description = "Issues a warning.",
    usage = "[severity] [player] [reason]",
    playerRetrievalStrategy = BOTH
)
@IocBean(conditionalOnProperty = "warnings-module.enabled=true")
@IocMultiProvider(SppCommand.class)
public class WarnCmd extends AbstractCmd {

    @ConfigProperty("permissions:permissions.warn-bypass")
    private String permissionWarnBypass;

    private final WarningConfiguration warningConfiguration;
    private final WarnService warnService;
    private final PlayerManager playerManager;
    private final PermissionHandler permissionHandler;

    public WarnCmd(Messages messages,
                   WarningConfiguration warningConfiguration,
                   WarnService warnService,
                   CommandService commandService,
                   PlayerManager playerManager,
                   PermissionHandler permissionHandler) {
        super(messages, permissionHandler, commandService);
        this.warningConfiguration = warningConfiguration;
        this.warnService = warnService;
        this.playerManager = playerManager;
        this.permissionHandler = permissionHandler;
    }

    @Override
    protected boolean executeCmd(CommandSender sender, String alias, String[] args, SppPlayer player, Map<String, String> optionalParameters) {
        List<WarningSeverityConfiguration> severityLevels = warningConfiguration.getSeverityLevels();
        if (severityLevels.isEmpty()) {
            String reason = JavaUtils.compileWords(args, 1);
            warnService.sendWarning(sender, player, reason);
            return true;
        }

        //Handle warning with severity
        String severityLevel = args[0];
        String reason = JavaUtils.compileWords(args, 2);

        WarningSeverityConfiguration severity = warningConfiguration.getSeverityConfiguration(severityLevel)
            .orElseThrow(() -> new BusinessException("&CCannot find severity level: [" + severityLevel + "]", messages.prefixWarnings));

        warnService.sendWarning(sender, player, reason, severity);
        return true;
    }

    @Override
    protected boolean canBypass(Player player) {
        return permissionHandler.has(player, permissionWarnBypass);
    }

    @Override
    protected int getMinimumArguments(CommandSender sender, String[] args) {
        List<WarningSeverityConfiguration> severityLevels = warningConfiguration.getSeverityLevels();
        if (severityLevels.isEmpty()) {
            return 2;
        }

        if(args.length == 0) {
            return 3;
        }

        String severityLevel = args[0];
        WarningSeverityConfiguration severityConfiguration = warningConfiguration.getSeverityConfiguration(severityLevel)
            .orElseThrow(() -> new BusinessException("&CCannot find severity level: [" + severityLevel + "]", messages.prefixWarnings));

        if (severityConfiguration.hasDefaultReason()) {
            return 2;
        }
        return 3;
    }

    @Override
    protected Optional<String> getPlayerName(CommandSender sender, String[] args) {
        List<WarningSeverityConfiguration> severityLevels = warningConfiguration.getSeverityLevels();
        if (severityLevels.isEmpty()) {
            return Optional.of(args[0]);
        }
        return Optional.of(args[1]);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<WarningSeverityConfiguration> severityLevels = warningConfiguration.getSeverityLevels();
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            if (severityLevels.isEmpty()) {
                suggestions.addAll(playerManager.getAllPlayerNames());
            } else {
                List<String> severityNames = severityLevels.stream().map(WarningSeverityConfiguration::getName).collect(Collectors.toList());
                suggestions.addAll(severityNames);
            }
            return suggestions.stream()
                .filter(s -> args[0].isEmpty() || s.contains(args[0]))
                .collect(Collectors.toList());
        }

        if (args.length == 2 && !severityLevels.isEmpty()) {
            return playerManager.getAllPlayerNames().stream()
                .filter(s -> args[1].isEmpty() || s.contains(args[1]))
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}